package com.xyongfeng.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xyongfeng.util.ValidGroups;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Collection;

@ApiModel(description = "管理员实体")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Accessors(chain = true)
@TableName("t_admins")
public class Admins implements Serializable, UserDetails {
    @ApiModelProperty("管理员id")
    @NotNull(message = "id不能为空", groups = ValidGroups.Id.class)
    @TableId(type = IdType.AUTO)
    private Integer id;


    @ApiModelProperty("用户名")
    @NotBlank(message = "用户名不能为空", groups = ValidGroups.Default.class)
    @Length(max = 20, message = "用户名长度不能大于20", groups = ValidGroups.Default.class)
    @TableField("username")
    private String username;


    @ApiModelProperty("密码")
    @NotBlank(message = "密码不能为空", groups = ValidGroups.Default.class)
    @Length(max = 20, message = "密码长度不能大于20", groups = ValidGroups.Default.class)
    @TableField("password")
    private String password;



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
