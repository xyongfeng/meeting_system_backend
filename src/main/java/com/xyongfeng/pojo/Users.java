package com.xyongfeng.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *
 * </p>
 *
 * @author xyongfeng
 * @since 2022-06-30
 */
@ApiModel(description = "用户实体")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Accessors(chain = true)
@TableName("t_users")
public class Users implements Serializable, UserDetails {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty("id")
    @NotNull(message = "id不能为空")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "姓名")
    @NotBlank(message = "姓名不能为空")
    @Length(max = 10, message = "姓名长度不能大于10")
    @TableField("name")
    private String name;


    @ApiModelProperty("用户名")
    @NotBlank(message = "用户名不能为空")
    @Length(max = 20, message = "用户名长度不能大于20")
    @TableField("username")
    private String username;


    @ApiModelProperty("密码")
    @NotBlank(message = "密码不能为空")
    @Length(max = 20, message = "密码长度不能大于20")
    @TableField("password")
    private String password;

    @ApiModelProperty(value = "手机号码")
    @Pattern(regexp = "^[1][3,4,5,6,7,8,9][0-9]{9}$", message = "手机号格式有误")
    @TableField("telephone")
    private String telephone;

    @ApiModelProperty(value = "邮箱地址")
    @Email(message = "邮箱地址格式有误")
    @TableField("email")
    private String email;

    @ApiModelProperty(value = "头像路径")
    @TableField("headImage")
    private String headImage;

    @ApiModelProperty(value = "是否拥有后台权限")
    @TableField("isAdmin")
    private Boolean isAdmin;


    @ApiModelProperty("权限列表")
    @TableField(exist = false)
    private List<String> perms;

    @JsonIgnore
    @TableField(exist = false)
    private List<GrantedAuthority> authorities;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (authorities == null && perms != null) {
            authorities = perms.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        }
        return authorities;
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
        return true;
    }

//    public Users setHeadImage(String headImage) {
//
//        this.headImage = headImage;
//        return this;
//    }
//
//    public String getHeadImage() {
//        String headpath = "img/head";
//        Path path = Paths.get(FileUtil.getClassPathUrl())
//                .resolve(headpath).resolve(headImage);
//        return path.toString();
//    }
}
