package com.asiainfo.msooimonitor.model.ooimodel.label;

import lombok.Data;

/**
 * @Author H
 * @Date 2019/10/16 15:01
 * @Desc
 **/
@Data
public class CocLabelInfo {

    private String labelId;

    private String labelName;

    private String tableName;

    private String updateCycle;

    private String rules;

    private String columnName;

    private String dependIndex;
}
