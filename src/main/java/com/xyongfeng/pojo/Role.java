package com.xyongfeng.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
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
@TableName("t_role_xq")
@ApiModel(value = "Role对象", description = "权限实体对象")
public class Role implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id_xq", type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private Integer id;

    @TableField("perms_xq")
    @ApiModelProperty(value = "权限识别")
    private String perms;

    @TableField("name_xq")
    @ApiModelProperty(value = "权限名")
    private String name;

    @TableField("remark_xq")
    @ApiModelProperty(value = "备注")
    private String remark;

//    @ApiModelProperty(value = "是否隐藏")
//    private Boolean hidden;
}
