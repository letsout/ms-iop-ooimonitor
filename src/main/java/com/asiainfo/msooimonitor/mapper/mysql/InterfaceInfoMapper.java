package com.asiainfo.msooimonitor.mapper.mysql;

import com.asiainfo.msooimonitor.model.ooimodel.InterfaceInfo;
import feign.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InterfaceInfoMapper {
    /**
     * 获取所有接口信息
     * @param serachFilter
     * @return
     */
    List<InterfaceInfo> getInterfaceInfo(InterfaceInfo serachFilter);

    /**
     * 根据id删除接口信息
     * @param interfaceId
     */
    void deleteInterfaceInfoById(@Param("interfaceId")String interfaceId);

    /**
     * 修改接口信息
     * @param interfaceInfo
     */
    void updateInterfaceInfoById(InterfaceInfo interfaceInfo);

    /**
     * 新增接口信息
     * @param interfaceInfo
     */
    void insertInterfaceInfo(InterfaceInfo interfaceInfo);

    int thisIdIsHave(String interfaceId);

    InterfaceInfo listInterfaceInfoById(String interfaceId);
}
