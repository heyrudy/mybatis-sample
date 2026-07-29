package com.heyrudy.mybatissample.application.context;

import com.heyrudy.mybatissample.application.context.CityContextModule.CapabilityProgram;
import com.heyrudy.mybatissample.gateway.AuditModule.AuditContext;

public record AuditAction<E, T>(
    AuditContext<E, T> context
) implements CapabilityProgram {

}
