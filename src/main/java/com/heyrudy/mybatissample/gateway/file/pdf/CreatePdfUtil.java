package com.heyrudy.mybatissample.gateway.file.pdf;

import com.heyrudy.mybatissample.domain.model.ApplicationRuntimeException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

public class CreatePdfUtil {

    public static final CreatePdfUtil INSTANCE = new CreatePdfUtil();

    private CreatePdfUtil() {
        super();
    }

    public byte[] createPdf() {
        try (PDDocument pdfDoc = new PDDocument()) {
            PDPage pdfPage = createPDPage(PDRectangle.A3);
            pdfDoc.addPage(pdfPage);
            ecrireDuTexteDansLaPAgePdf(pdfDoc, pdfPage);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            pdfDoc.save(byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw new ApplicationRuntimeException(
                "Erreur lors de la création d'un document PDF de test", e);
        }
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
     */
    private static void ecrireDuTexteDansLaPAgePdf(PDDocument pdfDoc, PDPage pdfPage)
        throws IOException {
        int positionDuTexte = (int) (pdfPage.getBBox().getHeight() * (60f / 100f));

        PDPageContentStream pdfPageContentStream = new PDPageContentStream(pdfDoc, pdfPage);
        pdfPageContentStream.beginText();
        pdfPageContentStream.setFont(PDType1Font.HELVETICA, 12);
        pdfPageContentStream.newLineAtOffset(10, positionDuTexte);
        pdfPageContentStream.showText("Ceci est un test d'écriture dans la page d'un PDF vierge.");
        pdfPageContentStream.endText();
        pdfPageContentStream.close();
    }
}
