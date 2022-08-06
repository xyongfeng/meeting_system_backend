package com.xyongfeng.pojo.Param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author xyongfeng
 */
@Data
@ApiModel(description = "LongID实体类")
@AllArgsConstructor
public class LongIDParam {

    @ApiModelProperty(value = "id",required = true)
    @NotNull(message = "id不能为空")
    private String id;


}
