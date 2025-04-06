package com.heyrudy.mybatissample.domain.spi;

public sealed interface ServiceKey<T>
    permits ServiceKey.DbServiceKey/*, ServiceKey.RestServiceKey*/ {

    Class<T> getType();

    sealed interface DbServiceKey<T> extends ServiceKey<T> permits CityDbKey {

    }

    enum CityDbKey implements DbServiceKey<ICityDbSPI> {
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

//    sealed interface RestServiceKey<T> extends ServiceKey<T> permits CityRestKey {
//
//    }

//    enum CityRestKey implements RestServiceKey<ICityRestSPI> {
//        INSTANCE;
//
//        @Override
//        public Class<ICityRestSPI> getType() {
//            return ICityRestSPI.class;
//        }
//
//        @Override
//        public String toString() {
//            return "CityRestKey{}";
//        }
//    }
}
