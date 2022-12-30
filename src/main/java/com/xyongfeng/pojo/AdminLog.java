package com.xyongfeng.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sun.org.apache.xpath.internal.operations.Bool;
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
@TableName("t_admin_log")
@ApiModel(value="AdminLog对象", description="")
public class AdminLog implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "操作模块")
    private String actionModule;

    @ApiModelProperty(value = "操作人id")
    private Integer actionUserId;

    @ApiModelProperty(value = "url")
    private String actionUrl;

    @ApiModelProperty(value = "请求参数")
    private String actionContent;

    @ApiModelProperty(value = "操作类型 查看 编辑 删除 新增")
    private String actionType;

    @ApiModelProperty(value = "操作ip")
    private String actionIp;

    @ApiModelProperty(value = "操作时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime actionTime;

    @ApiModelProperty(value = "操作是否成功")
    private Boolean actionSuccess;

    @ApiModelProperty(value = "操作人")
    @TableField(exist = false)
    private Users actionUser;
}
