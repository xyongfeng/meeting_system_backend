package com.xyongfeng.pojo.Param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "进入会议是否需要许可")
public class MeetSetLicenceParam {
    @ApiModelProperty(value = "id",required = true)
    @NotNull(message = "id不能为空")
    private String id;

    @ApiModelProperty(value = "是否需要许可",required = true)
    @NotNull(message = "许可不能为空")
    private Boolean haveLicence;

}
