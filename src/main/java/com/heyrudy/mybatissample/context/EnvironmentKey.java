package com.heyrudy.mybatissample.context;

public sealed interface EnvironmentKey
    permits DependencyKey {

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