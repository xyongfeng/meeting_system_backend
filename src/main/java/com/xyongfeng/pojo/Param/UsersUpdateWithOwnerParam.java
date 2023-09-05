package com.xyongfeng.pojo.Param;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;


/**
 * @author xyongfeng
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "User本人更新实体类")
public class UsersUpdateWithOwnerParam {

    @ApiModelProperty(value = "id", required = true)
    @NotNull(message = "id不能为空")
    private Integer id;


    @ApiModelProperty(value = "名称")
    @NotBlank(message = "名称不能为空")
    @Length(max = 10, message = "名称长度不能大于10")
    private String name;


    @ApiModelProperty(value = "手机号码")
    @Pattern(regexp = "^$|^[1][3,4,5,6,7,8,9][0-9]{9}$", message = "手机号格式有误")
    private String telephone;

    @ApiModelProperty(value = "邮箱地址")
    @Email(message = "邮箱地址格式有误")
    private String email;
}
