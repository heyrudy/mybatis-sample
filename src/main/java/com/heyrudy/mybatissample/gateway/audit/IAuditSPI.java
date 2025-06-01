package com.heyrudy.mybatissample.gateway.audit;

public interface IAuditSPI {

    void auditAction(String action, String resourceId, String result);
}