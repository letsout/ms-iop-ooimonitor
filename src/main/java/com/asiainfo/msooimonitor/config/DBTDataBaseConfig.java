package com.asiainfo.msooimonitor.config;

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
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * @Author H
 * @Date 2019/7/1 17:49
 * @Desc
 **/
@Configuration
@EnableTransactionManagement
@MapperScan(basePackages = "com.asiainfo.msooimonitor.mapper.dbt", sqlSessionFactoryRef = "DBTSessionFactory")
public class DBTDataBaseConfig {

    @Bean(name = "DBTDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.secondary")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "DBTSessionFactory")
    public SqlSessionFactory sqlSessionFactoryBean(@Qualifier("DBTDataSource") DataSource dataSource)
            throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources("classpath:mapper/dbt/*/*.xml"));
        /** 设置typeAlias 包扫描路径 */
        SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBean.getObject();
        sqlSessionFactory.getConfiguration().setMapUnderscoreToCamelCase(true);
        return sqlSessionFactory;
    }

    @Bean(name = "loadJdbc")
    public NamedParameterJdbcTemplate loadJdbc(){

        DataSource dataSource = DataSourceBuilder.create()
                .driverClassName("com.gbase.jdbc.Driver")
                .url("jdbc:gbase://10.101.167.28:5258/iop?rewriteBatchedStatements=true")
                .username("gbase")
                .password("gbase")
                .build();
      /*  DataSource dataSource = DataSourceBuilder.create()
                .driverClassName("com.gbase.jdbc.Driver")
                .url("jdbc:gbase://10.113.148.2:5258/iop?rewriteBatchedStatements=true")
                .username("gbinst1")
                .password("@_kx%a3p")
                .build();*/
        NamedParameterJdbcTemplate loadJdbc = new NamedParameterJdbcTemplate(dataSource);
        return loadJdbc;
    }

    @Bean(name = "DBTTransactionManager")
    public DataSourceTransactionManager transactionManager(@Qualifier("DBTDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
