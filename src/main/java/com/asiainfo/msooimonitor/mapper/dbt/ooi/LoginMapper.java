package com.asiainfo.msooimonitor.mapper.dbt.ooi;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginMapper {

    int login(@Param("userName") String userName,@Param("password") String password);

}
