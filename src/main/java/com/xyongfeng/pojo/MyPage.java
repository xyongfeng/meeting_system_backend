package com.xyongfeng.pojo;



import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@ApiModel(description = "分页类实体")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyPage {
    @ApiModelProperty("当前页码")
    @NotNull(message = "页码不能为空")
    private Integer current;

    @ApiModelProperty("页面大小")
    @NotNull(message = "页面大小不能为空")
    private Integer size;
}
