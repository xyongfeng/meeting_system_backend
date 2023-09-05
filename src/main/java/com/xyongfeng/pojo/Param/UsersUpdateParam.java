package com.xyongfeng.pojo.Param;


import com.baomidou.mybatisplus.annotation.TableField;
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
@ApiModel(description = "User更新实体类")
public class UsersUpdateParam {

    @ApiModelProperty(value = "id",required = true)
//    @NotNull(message = "id不能为空")
    private Integer id;

    @ApiModelProperty(value = "名称")
    @NotBlank(message = "名称不能为空")
    @Length(max = 10, message = "名称长度不能大于10")
    private String name;

    @ApiModelProperty("用户名")
    @NotBlank(message = "用户名不能为空")
    @Length(max = 20, message = "用户名长度不能大于20")
    private String username;

    @ApiModelProperty("密码")
    @Length(max = 20, message = "密码长度不能大于20")
    private String password;

    @ApiModelProperty(value = "手机号码")
    @Pattern(regexp = "^$|^[1][3,4,5,6,7,8,9][0-9]{9}$", message = "手机号格式有误")
    private String telephone;

    @ApiModelProperty(value = "邮箱地址")
    @Email(message = "邮箱地址格式有误")
    private String email;

    @ApiModelProperty(value = "头像路径")
    private String headImage;

    @ApiModelProperty(value = "是否拥有后台权限")
    private Boolean isAdmin;

}
