package com.xyongfeng.pojo;

import com.xyongfeng.util.ValidGroups;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @NotEmpty(message = "id不能为空",groups = ValidGroups.Update.class)
    private int id;
    @NotBlank(message = "姓名不能为空",groups = ValidGroups.Update.class)
    private String name;
    @NotBlank(message = "用户名不能为空",groups = ValidGroups.Default.class)
    @Length(max = 20,message = "用户名长度不能大于20",groups = ValidGroups.Default.class)
    private String username;
    @NotBlank(message = "密码不能为空",groups = ValidGroups.Default.class)
    @Length(max = 20,message = "密码长度不能大于20",groups = ValidGroups.Default.class)
    private String password;
    @NotBlank(message = "姓名不能为空",groups = ValidGroups.Update.class)
    private String faceImgPath;
}
