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
@TableName("t_menu_role_xq")
@ApiModel(value="MenuRole对象", description="")
public class MenuRole implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id_xq", type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private Integer id;

    @TableField("menu_id_xq")
    @ApiModelProperty(value = "menu_id")
    private Integer menuId;

    @TableField("role_id_xq")
    @ApiModelProperty(value = "role_id")
    private Integer roleId;


}
