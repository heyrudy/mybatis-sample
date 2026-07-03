package com.heyrudy.mybatissample.application.context;

public sealed interface CapabilityKey<T>
    permits DependencyKey
    , ProgramHandlerKey {

//    sealed interface RestSPICriticalServiceKey<T> extends ServiceKey<T> permits CityRestSPIKey {
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