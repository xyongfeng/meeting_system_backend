package com.xyongfeng.pojo.Param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * User设置管理员
 * @author xyongfeng
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "User设置管理员")
public class UsersSetAdminParam {
    @ApiModelProperty(value = "id",required = true)
    @NotNull(message = "id不能为空")
    private Integer id;

    @ApiModelProperty(value = "是否为管理员",required = true)
    @NotNull(message = "管理员状态不能为空")
    private Boolean isAdmin;
}
