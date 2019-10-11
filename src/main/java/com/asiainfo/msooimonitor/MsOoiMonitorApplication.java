package com.asiainfo.msooimonitor;

import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import java.time.LocalDate;


@SpringCloudApplication
@EnableScheduling
public class MsOoiMonitorApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsOoiMonitorApplication.class, args);
        System.out.println("=============================");
        System.out.println("|        start success       |");
        System.out.println("=============================");
    }
    @Configuration
    public static class SecuritySecureConfig extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.authorizeRequests()
                    .antMatchers("/api/**").permitAll()
                    ///actuator/health不打开，网关路由的时候会找不到服务！
                    .antMatchers("/actuator/health").permitAll()
                    .antMatchers("/actuator/hystrix.stream").permitAll()
                    .antMatchers("/actuator/**").authenticated().and()
                    .httpBasic().and().headers().frameOptions().disable().and()
                    .csrf()
                    .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                    .ignoringAntMatchers(
                            "/instances",
                            "/actuator/**"
                    ).and().csrf().disable();
        }
    }
}

