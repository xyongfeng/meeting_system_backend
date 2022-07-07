package com.xyongfeng.pojo.Param;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author xyongfeng
 */
@ApiModel(description = "分页类实体")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageParam {
    @ApiModelProperty(value = "当前页码",required = true)
    @NotNull(message = "页码不能为空")
    private Integer current;

    @ApiModelProperty(value = "页面大小",required = true)
    @NotNull(message = "页面大小不能为空")
    private Integer size;
}
