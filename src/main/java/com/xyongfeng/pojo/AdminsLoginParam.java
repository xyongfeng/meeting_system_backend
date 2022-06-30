package com.xyongfeng.pojo;

import com.xyongfeng.util.ValidGroups;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;


/**
 * @author xyongfeng
 */
@Data
@EqualsAndHashCode
@Accessors(chain = true)
@ApiModel(description = "管理员实体")
public class AdminsLoginParam implements Serializable {
    @ApiModelProperty(value = "用户名", required = true)
    @NotBlank(message = "用户名不能为空")
    @Length(max = 20, message = "用户名长度不能大于20")
    private String username;


    @ApiModelProperty(value = "密码", required = true)
    @NotBlank(message = "密码不能为空")
    @Length(max = 20, message = "密码长度不能大于20")
    private String password;


    @ApiModelProperty(value = "验证码", required = true)
    @NotBlank(message = "验证码不能为空", groups = ValidGroups.Default.class)
    private String code;


}
