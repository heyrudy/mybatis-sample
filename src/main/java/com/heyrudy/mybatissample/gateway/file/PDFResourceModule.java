package com.heyrudy.mybatissample.gateway.file;

import com.heyrudy.mybatissample.domain.DomainServiceSPIError.PDFDocumentCreationError;
import io.vavr.control.Either;
import io.vavr.control.Try;
import java.io.ByteArrayOutputStream;
import java.util.function.Function;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

public interface PDFResourceModule {

    enum CreatePdfUtil {
        INSTANCE;

        public Either<PDFDocumentCreationError, byte[]> createPdf() {
            return Try.withResources(() -> new PDDocument())
                .of(pdfDoc -> {
                    PDPage pdfPage = createPDPage(PDRectangle.A3);
                    pdfDoc.addPage(pdfPage);

                    return ecrireDuTexteDansLaPAgePdf(pdfDoc, pdfPage)
                        .flatMap(ignored -> Try.of(() -> {
                            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                            pdfDoc.save(byteArrayOutputStream);
                            return byteArrayOutputStream.toByteArray();
                        }));
                })
                .flatMap(Function.identity())
                .toEither()
                .mapLeft(throwable -> new PDFDocumentCreationError(
                    "Erreur lors de la création d'un document PDF de test"));
        }

        /**
         * Créer une nouvelle page d'un document PDF avec la taille choisie
         *
         * @param pdRectangle La taille de la page du PDF
         * @return une instance d'une page PDF
         */
        private static PDPage createPDPage(PDRectangle pdRectangle) {
            return new PDPage(pdRectangle);
        }

        /**
         * Écrire dans la page d'un document PDF
         *
         * @param pdfDoc Instance d'un document PDF
         * @param pdfPage Instance de la page d'un document PDF
         * @return Try containing success or failure
         */
        private static Try<Void> ecrireDuTexteDansLaPAgePdf(PDDocument pdfDoc, PDPage pdfPage) {
            return Try.of(() -> {
                int positionDuTexte = (int) (pdfPage.getBBox().getHeight() * (60f / 100f));

                PDPageContentStream pdfPageContentStream = new PDPageContentStream(pdfDoc, pdfPage);
                pdfPageContentStream.beginText();
                pdfPageContentStream.setFont(PDType1Font.HELVETICA, 12);
                pdfPageContentStream.newLineAtOffset(10, positionDuTexte);
                pdfPageContentStream.showText(
                    "Ceci est un test d'écriture dans la page d'un PDF vierge.");
                pdfPageContentStream.endText();
                pdfPageContentStream.close();

                return null;
            });
        }
    }
}
