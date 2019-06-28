package com.asiainfo.msooimonitor;

import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class,
        MybatisAutoConfiguration.class})
@ServletComponentScan("com.asiainfo.msooimonitor.filter")
@MapperScan("com.asiainfo.msooimonitor.mapper.dbt")
public class MsOoiMonitorApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsOoiMonitorApplication.class, args);
        System.out.println("=============================");
        System.out.println("|        start success       |");
        System.out.println("=============================");
    }
}

