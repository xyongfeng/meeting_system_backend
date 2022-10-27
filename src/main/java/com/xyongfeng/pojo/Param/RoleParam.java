package com.xyongfeng.pojo.Param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author xyongfeng
 */
@Data
@ApiModel(description = "权限提交表单")
public class RoleParam {

    @ApiModelProperty(value = "用户管理权限",required = true)
    @NotNull(message = "用户管理权限不能为空")
    private Boolean user;

    @ApiModelProperty(value = "会议管理权限",required = true)
    @NotNull(message = "会议管理权限不能为空")
    private Boolean meeting;


}
