package com.gocle.lxp.tracking.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

@Configuration
@MapperScan(
    basePackages = "com.gocle.lxp.tracking.mapper.tracking",
    sqlSessionFactoryRef = "trackingSqlSessionFactory",
	basePackageClasses = {
			com.gocle.lxp.tracking.mapper.LearningLogMapper.class
    }
)
public class TrackingDataSourceConfig {

    @Primary
    @Bean(name = "trackingDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.tracking")
    public DataSource trackingDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Primary
    @Bean(name = "trackingSqlSessionFactory")
    public SqlSessionFactory trackingSqlSessionFactory(
            @Qualifier("trackingDataSource") DataSource dataSource
    ) throws Exception {

        SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
        factory.setDataSource(dataSource);

        // 📦 tracking DB용 mapper XML
        factory.setMapperLocations(
            new PathMatchingResourcePatternResolver()
                .getResources("classpath:/mapper/tracking/*.xml")
        );

        factory.setTypeAliasesPackage(
            "com.gocle.lxp.tracking.domain"
        );

        return factory.getObject();
    }
}
