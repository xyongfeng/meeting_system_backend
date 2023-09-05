package com.xyongfeng.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.time.LocalDateTime;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
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
 * @since 2022-12-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_admin_log_xq")
@ApiModel(value = "AdminLog对象", description = "")
public class AdminLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id_xq", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "操作模块")
    @TableField("action_module_xq")
    private String actionModule;

    @ApiModelProperty(value = "操作人id")
    @TableField("action_user_id_xq")
    private Integer actionUserId;

    @TableField("action_url_xq")
    @ApiModelProperty(value = "url")
    private String actionUrl;

    @TableField("action_content_xq")
    @ApiModelProperty(value = "请求参数")
    private String actionContent;

    @TableField("action_type_xq")
    @ApiModelProperty(value = "操作类型 查看 编辑 删除 新增")
    private String actionType;

    @TableField("action_ip_xq")
    @ApiModelProperty(value = "操作ip")
    private String actionIp;

    @TableField("action_time_xq")
    @ApiModelProperty(value = "操作时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime actionTime;


    @TableField("action_success_xq")
    @ApiModelProperty(value = "操作是否成功")
    private Boolean actionSuccess;

    @ApiModelProperty(value = "操作人")
    @TableField(exist = false)
    private Users actionUser;
}
