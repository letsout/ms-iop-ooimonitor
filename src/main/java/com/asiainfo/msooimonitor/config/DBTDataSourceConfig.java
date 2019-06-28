package com.asiainfo.msooimonitor.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.batch.MyBatisCursorItemReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.NavigableMap;

/**
 * @Author H
 * @Date 2019/1/11 10:41
 * @Desc db2数据源配置
 **/
@Configuration
/**
 * 扫描Mapper接口进行容器管理
 */
@MapperScan(basePackages = DBTDataSourceConfig.PACKAGE,sqlSessionFactoryRef = "dbtSqlSessionFactory")
public class DBTDataSourceConfig {

    private static final Logger logger = LoggerFactory.getLogger(DBTDataSourceConfig.class);
    /**
     * 精确到包，便于跟其他数据隔离
     */
    static final String PACKAGE="com.asiainfo.msooimonitor.mapper.dbt";
    static final String MAPPER_LOCATION="classpath:mapper/dbt/*/*.xml";

    @Value("${spring.datasource.dbt.url}")
    private String dbUrl;
    @Value("${spring.datasource.dbt.username}")
    private String username;
    @Value("${spring.datasource.dbt.password}")
    private String password;
    @Value("${spring.datasource.dbt.driver-class-name}")
    private String driverClassName;
    @Value("${spring.datasource.dbt.validationQuery}")
    private String validationQuery;
    @Value("${spring.datasource.initialSize}")
    private int initialSize;
    @Value("${spring.datasource.minIdle}")
    private int minIdle;
    @Value("${spring.datasource.maxActive}")
    private int maxActive;
    @Value("${spring.datasource.maxWait}")
    private int maxWait;
    @Value("${spring.datasource.timeBetweenEvictionRunsMillis}")
    private int timeBetweenEvictionRunsMillis;
    @Value("${spring.datasource.minEvictableIdleTimeMillis}")
    private int minEvictableIdleTimeMillis;
    @Value("${spring.datasource.testWhileIdle}")
    private boolean testWhileIdle;
    @Value("${spring.datasource.testOnBorrow}")
    private boolean testOnBorrow;
    @Value("${spring.datasource.testOnReturn}")
    private boolean testOnReturn;
    @Value("${spring.datasource.poolPreparedStatements}")
    private boolean poolPreparedStatements;
    @Value("${spring.datasource.maxPoolPreparedStatementPerConnectionSize}")
    private int maxPoolPreparedStatementPerConnectionSize;
    @Value("${spring.datasource.filters}")
    private String filters;
    @Value("${spring.datasource.connectionProperties}")
    private String connectionProperties;
    @Value("${spring.datasource.useGlobalDataSourceStat}")
    private Boolean useGlobalDataSourceStat;

    @Bean(name = "dbtDataSource")
    public DataSource dbtDataSource(){
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(dbUrl);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setDriverClassName(driverClassName);

        /**
         * druid config
         */
        dataSource.setInitialSize(initialSize);
        dataSource.setMinIdle(minIdle);
        dataSource.setMaxActive(maxActive);
        dataSource.setValidationQuery(validationQuery);
        dataSource.setUseGlobalDataSourceStat(useGlobalDataSourceStat);
        dataSource.setInitialSize(initialSize);
        dataSource.setMinIdle(minIdle);
        dataSource.setMaxActive(maxActive);
        dataSource.setMaxWait(maxWait);
        dataSource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        dataSource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        dataSource.setValidationQuery(validationQuery);
        dataSource.setTestWhileIdle(testWhileIdle);
        dataSource.setTestOnBorrow(testOnBorrow);
        dataSource.setTestOnReturn(testOnReturn);
        dataSource.setPoolPreparedStatements(poolPreparedStatements);
        dataSource.setMaxPoolPreparedStatementPerConnectionSize(maxPoolPreparedStatementPerConnectionSize);
        dataSource.setUseGlobalDataSourceStat(useGlobalDataSourceStat);

        try {
            dataSource.setFilters(filters);
        } catch (SQLException e) {
            logger.error("init druid setFilters error : {}",e);
        }
        dataSource.setConnectionProperties(connectionProperties);
        return  dataSource;
    }

    @Bean(name = "dbtTransactionManager")
    public DataSourceTransactionManager dataSourceTransactionManager(){
        return new DataSourceTransactionManager(dbtDataSource());
    }

    @Bean(name = "dbtSqlSessionFactory")
    public SqlSessionFactory dbtSqlSessionFactory(@Qualifier("dbtDataSource") DataSource dataSource) throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources(DBTDataSourceConfig.MAPPER_LOCATION));
        SqlSessionFactory factory = sessionFactory.getObject();
        factory.getConfiguration().setMapUnderscoreToCamelCase(true);
        return factory;
    }

    @Bean(name = "loadJdbc")
    public NamedParameterJdbcTemplate loadJdbc(@Qualifier("dbtDataSource") DataSource dataSourc){
        NamedParameterJdbcTemplate loadJdbc = new NamedParameterJdbcTemplate(dataSourc);
        return loadJdbc;
    }

    @Bean(name = "myBatisCursorItemReader")
    public MyBatisCursorItemReader myBatisCursorItemReader (@Qualifier("dbtSqlSessionFactory") SqlSessionFactory sqlSessionFactory){
        MyBatisCursorItemReader<Object> myBatisCursorItemReader = new MyBatisCursorItemReader<>();
        myBatisCursorItemReader.setSqlSessionFactory(sqlSessionFactory);
        myBatisCursorItemReader.setQueryId("com.asiainfo.msooimonitor.mapper.dbt.upload.UploadMapper.selectUpload93002");
        return myBatisCursorItemReader;
    }
}
