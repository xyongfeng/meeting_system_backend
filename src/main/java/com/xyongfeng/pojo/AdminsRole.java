package com.xyongfeng.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author xyongfeng
 * @since 2022-06-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_admins_role")
@ApiModel(value="AdminsRole对象", description="")
public class AdminsRole implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "id")
    private Integer id;

    @ApiModelProperty(value = "admins_id")
    private Integer adminsId;

    @ApiModelProperty(value = "role_id")
    private Integer roleId;


}
