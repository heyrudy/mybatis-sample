package com.heyrudy.mybatissample.configuration;

import com.heyrudy.mybatissample.dto.mapper.CityMapper;
import com.heyrudy.mybatissample.dto.mapper.CityMapperImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfiguration {

//    @ConfigurationProperties(prefix = "spring.datasource")
//    public DataSource dataSource() {
//        return DataSourceBuilder.create().build();
//    }
//
//    @Bean
//    public SqlSessionFactory sqlSessionFactory() throws Exception {
//        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
//        factoryBean.setDataSource(dataSource());
//        return factoryBean.getObject();
//    }
//
//    @Bean
//    public SqlSessionTemplate sqlSession() throws Exception {
//        return new SqlSessionTemplate(sqlSessionFactory());
//    }
//
//    @Bean
//    public CityRepository cityRepository() throws Exception {
//        try (SqlSessionTemplate sqlSessionTemplate = new SqlSessionTemplate(sqlSessionFactory())) {
//            return sqlSessionTemplate.getMapper(CityRepository.class);
//        }
//    }

    @Bean
    public CityMapper mapper() {
        return new CityMapperImpl();
    }
}
