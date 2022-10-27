package com.xyongfeng.pojo.Param;


import com.xyongfeng.util.ValidGroups;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;


/**
 * @author xyongfeng
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "User添加实体类")
public class UsersRegisterParam {

    @ApiModelProperty(value = "名称",required = true)
    @NotBlank(message = "名称不能为空")
    @Length(max = 10, message = "名称长度不能大于10")
    private String name;

    @ApiModelProperty(value = "用户名",required = true)
    @NotBlank(message = "用户名不能为空")
    @Length(max = 20, message = "用户名长度不能大于20")
    private String username;

    @ApiModelProperty(value = "密码",required = true)
    @NotBlank(message = "密码不能为空")
    @Length(max = 20, message = "密码长度不能大于20")
    private String password;

    @ApiModelProperty(value = "验证码", required = true)
    @NotBlank(message = "验证码不能为空")
    private String code;

    @ApiModelProperty(value = "手机号码")
    @Pattern(regexp = "^[1][3,4,5,6,7,8,9][0-9]{9}$", message = "手机号格式有误")
    private String telephone;

    @ApiModelProperty(value = "邮箱地址")
    @Email(message = "邮箱地址格式有误")
    private String email;

//    @ApiModelProperty(value = "头像路径")
//    private String headImage;

}
