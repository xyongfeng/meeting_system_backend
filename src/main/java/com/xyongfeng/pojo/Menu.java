package com.xyongfeng.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

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
@TableName("t_menu_xq")
@ApiModel(value="Menu对象", description="")
public class Menu implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id_xq", type = IdType.AUTO)
    private Integer id;

    @TableField("url_xq")
    @ApiModelProperty(value = "url")
    private String url;

    @TableField("path_xq")
    @ApiModelProperty(value = "path")
    private String path;

    @TableField("component_xq")
    @ApiModelProperty(value = "组件")
    private String component;

    @TableField("name_xq")
    @ApiModelProperty(value = "菜单名")
    private String name;

    @ApiModelProperty(value = "图标")
    @TableField("iconCls_xq")
    private String iconCls;

    @ApiModelProperty(value = "是否要求权限")
    @TableField("requireAuth_xq")
    private Boolean requireAuth;

    @TableField("parentid_xq")
    @ApiModelProperty(value = "父ID")
    private Integer parentid;

    @TableField("enabled_xq")
    @ApiModelProperty(value = "是否启用")
    private Boolean enabled;

    @ApiModelProperty(value = "子菜单")
    @TableField(exist = false)
    private List<Menu> children;

}
