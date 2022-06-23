package com.xyongfeng.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
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
import java.io.Serializable;
import java.util.Collection;


@Data
@EqualsAndHashCode
@Accessors(chain = true)
@ApiModel(description = "管理员实体")
public class AdminsLoginParam implements Serializable {
    @ApiModelProperty("用户名")
    @NotBlank(message = "用户名不能为空",groups = ValidGroups.Default.class)
    @Length(max = 20,message = "用户名长度不能大于20",groups = ValidGroups.Default.class)
    private String username;


    @ApiModelProperty("密码")
    @NotBlank(message = "密码不能为空",groups = ValidGroups.Default.class)
    @Length(max = 20,message = "密码长度不能大于20",groups = ValidGroups.Default.class)
    private String password;



}
