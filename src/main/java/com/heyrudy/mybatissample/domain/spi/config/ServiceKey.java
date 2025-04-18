package com.heyrudy.mybatissample.domain.spi.config;

import com.heyrudy.mybatissample.domain.spi.IAuditSPI;
import com.heyrudy.mybatissample.domain.spi.ICityDbSPI;
import com.heyrudy.mybatissample.domain.spi.config.ServiceKey.AuditSPIServiceKey;
import com.heyrudy.mybatissample.domain.spi.config.ServiceKey.CriticalRepositoryKey;
import com.heyrudy.mybatissample.domain.spi.config.ServiceKey.DbCriticalServiceKey;
import com.heyrudy.mybatissample.gateway.db.spring.relational.repository.CityRepository;

public sealed interface ServiceKey<T>
    permits CriticalRepositoryKey,
    DbCriticalServiceKey,
    /*ServiceKey.RestSPICriticalServiceKey*/
    AuditSPIServiceKey {

    Class<T> getType();

    sealed interface CriticalRepositoryKey<T> extends ServiceKey<T> permits CityRepositoryKey {

    }

    enum CityRepositoryKey implements CriticalRepositoryKey<CityRepository> {
        INSTANCE;

        @Override
        public Class<CityRepository> getType() {
            return CityRepository.class;
        }

        @Override
        public String toString() {
            return "CityRepositoryKey{}";
        }
    }

    sealed interface DbCriticalServiceKey<T> extends ServiceKey<T> permits CityDbSPIKey {

    }

    enum CityDbSPIKey implements DbCriticalServiceKey<ICityDbSPI> {
        INSTANCE;

        @Override
        public Class<ICityDbSPI> getType() {
            return ICityDbSPI.class;
        }

        @Override
        public String toString() {
            return "CityDbKey{}";
        }
    }

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

    sealed interface AuditSPIServiceKey<T> extends ServiceKey<T> permits AuditSPIKey {

    }

    enum AuditSPIKey implements AuditSPIServiceKey<IAuditSPI> {
        INSTANCE;

        @Override
        public Class<IAuditSPI> getType() {
            return IAuditSPI.class;
        }

        @Override
        public String toString() {
            return "AuditSPIKey{}";
        }
    }
}
