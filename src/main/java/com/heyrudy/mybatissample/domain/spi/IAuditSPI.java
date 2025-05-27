package com.heyrudy.mybatissample.domain.spi;

public interface IAuditSPI {

    void auditAction(String action, String resourceId, String result);
}