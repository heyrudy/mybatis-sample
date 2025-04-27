package com.heyrudy.mybatissample.domain.spi.config;

public sealed interface ServiceKey<T>
    permits SecretKey,
    ConfigKey,
    CriticalRepositoryKey,
    DbCriticalServiceKey,
    AuditSPIServiceKey {

    Class<T> getType();

//    sealed interface estSPICriticalServiceKey<T> extends ServiceKey<T> permits CityRestSPIKey {
//
//    }
//
//    enum CityRestSPIKey implements RestSPICriticalServiceKey<ICityRestSPI> {
//        INSTANCE;
//
//        @Override
//        public Class<ICityRestSPI> getType() {
//            return ICityRestSPI.class;
//        }
//
//        @Override
//        public String toString() {
//            return "CityRestSPIKey{}";
//        }
//    }
}
