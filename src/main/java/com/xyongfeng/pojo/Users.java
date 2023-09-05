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

import java.time.LocalDateTime;
import java.time.LocalTime;
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
@TableName("t_users_xq")
public class Users implements Serializable, UserDetails {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty("id")
    @NotNull(message = "id不能为空")
    @TableId(value = "id_xq", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "名称")
    @NotBlank(message = "名称不能为空")
    @Length(max = 10, message = "名称长度不能大于10")
    @TableField("name_xq")
    private String name;


    @ApiModelProperty("用户名")
    @NotBlank(message = "用户名不能为空")
    @Length(max = 20, message = "用户名长度不能大于20")
    @TableField("username_xq")
    private String username;


    @ApiModelProperty("密码")
    @NotBlank(message = "密码不能为空")
    @Length(max = 20, message = "密码长度不能大于20")
    @TableField("password_xq")
    private String password;

    @ApiModelProperty(value = "手机号码")
    @Pattern(regexp = "^$|^[1][3,4,5,6,7,8,9][0-9]{9}$", message = "手机号格式有误")
    @TableField("telephone_xq")
    private String telephone;

    @ApiModelProperty(value = "邮箱地址")
    @Email(message = "邮箱地址格式有误")
    @TableField("email_xq")
    private String email;

    @ApiModelProperty(value = "头像路径")
    @TableField("headImage_xq")
    private String headImage;


    @ApiModelProperty(value = "面部照片路径")
    @TableField("faceImage_xq")
    private String faceImage;

    @ApiModelProperty(value = "是否拥有后台权限")
    @TableField("isAdmin_xq")
    private Boolean isAdmin;

    @ApiModelProperty("权限列表")
    @TableField(exist = false)
    private List<String> perms;

    @ApiModelProperty("签到时间")
    @TableField(exist = false)
    private LocalDateTime hadSignInTime;


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

}
