package com.xyongfeng.pojo;

import com.xyongfeng.util.ValidGroups;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@ApiModel(description = "管理员实体")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Admins {
    @ApiModelProperty("管理员id")
    @NotEmpty(message = "id不能为空",groups = ValidGroups.Update.class)
    private int id;
    @ApiModelProperty("用户名")
    @NotBlank(message = "用户名不能为空",groups = ValidGroups.Default.class)
    @Length(max = 20,message = "用户名长度不能大于20",groups = ValidGroups.Default.class)
    private String username;
    @ApiModelProperty("密码")
    @NotBlank(message = "密码不能为空",groups = ValidGroups.Default.class)
    @Length(max = 20,message = "密码长度不能大于20",groups = ValidGroups.Default.class)
    private String password;
    @ApiModelProperty("权限")
    @NotEmpty(message = "权限不能为空",groups = ValidGroups.Update.class)
    private int authority;
}
