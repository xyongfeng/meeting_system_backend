package com.xyongfeng.pojo.Param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * @author xyongfeng
 */
@Data
@ApiModel(description = "ID实体类")
public class IDParam {

    @ApiModelProperty(value = "id",required = true)
    @NotNull(message = "id不能为空")
    private Integer id;


}
