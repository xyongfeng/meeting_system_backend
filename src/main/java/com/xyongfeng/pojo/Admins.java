package com.xyongfeng.pojo;

import com.xyongfeng.util.ValidGroups;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Admins {
    @NotEmpty(message = "id不能为空",groups = ValidGroups.Update.class)
    private int id;
    @NotBlank(message = "用户名不能为空",groups = ValidGroups.Default.class)
    @Length(max = 20,message = "用户名长度不能大于20",groups = ValidGroups.Default.class)
    private String username;
    @NotBlank(message = "密码不能为空",groups = ValidGroups.Default.class)
    @Length(max = 20,message = "密码长度不能大于20",groups = ValidGroups.Default.class)
    private String password;
    @NotEmpty(message = "权限不能为空",groups = ValidGroups.Update.class)
    private int authority;
}
