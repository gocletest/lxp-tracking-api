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
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import com.gocle.lxp.tracking.mapper.backend.ApiKeyUsageMapper;
import com.gocle.lxp.tracking.mapper.backend.LxpApiKeyMapper;

@Configuration
@MapperScan(
    basePackages = "com.gocle.lxp.tracking.mapper.backend",
    sqlSessionFactoryRef = "backendSqlSessionFactory",
    basePackageClasses = {
        ApiKeyUsageMapper.class,
        LxpApiKeyMapper.class
    }
)
public class BackendDataSourceConfig {

    @Bean(name = "backendDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.backend")
    public DataSource backendDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "backendSqlSessionFactory")
    public SqlSessionFactory backendSqlSessionFactory(
            @Qualifier("backendDataSource") DataSource dataSource
    ) throws Exception {

        SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
        factory.setDataSource(dataSource);

        // 🔐 backend DB용 mapper XML
        factory.setMapperLocations(
            new PathMatchingResourcePatternResolver()
                .getResources("classpath:/mapper/backend/*.xml")
        );

        factory.setTypeAliasesPackage(
            "com.gocle.lxp.tracking.domain"
        );

        return factory.getObject();
    }
}
