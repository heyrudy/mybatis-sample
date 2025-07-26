package com.heyrudy.mybatissample.application.context;

import com.heyrudy.mybatissample.domain.UtilsModule.MutatorOption;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.util.Arrays;
import java.util.Objects;
import javax.sql.DataSource;

public enum HikariDataSourceBuilder {
    INSTANCE;

    @SafeVarargs
    public static DataSource of(MutatorOption<HikariDataSource>... options) {
        return Arrays.stream(options)
            .filter(Objects::nonNull)
            .reduce(new HikariDataSource(), (model, option) -> option.apply(model), (a, b) -> a);
    }

    public enum DataSourceMutatorOptions {
        INSTANCE;

        public MutatorOption<HikariDataSource> config(HikariConfig config) {
            return MutatorOption.of(
                config,
                (it, v) -> new HikariDataSource(config)
            );
        }
    }
}