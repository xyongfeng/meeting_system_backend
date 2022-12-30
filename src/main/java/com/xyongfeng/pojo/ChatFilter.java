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

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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
@TableName("t_chat_filter")
@ApiModel(value = "ChatFilter对象", description = "")
public class ChatFilter implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "过滤内容", required = true)
    @NotBlank(message = "过滤内容不能为空")
    private String filterContent;

    @ApiModelProperty(value = "过滤规则 1 直接替换，2 正则表达式替换", required = true)
    @NotNull(message = "过滤规则不能为空")
    private Integer filterRule;

    @ApiModelProperty(value = "替换内容")
    private String replaceContent;

    @ApiModelProperty(value = "添加人的id")
    private Integer appenderId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "添加时间")
    private LocalDateTime appendTime;

    @ApiModelProperty(value = "是否启用")
    private Boolean enable;

    @ApiModelProperty(value = "添加用户")
    @TableField(exist = false)
    private Users appender;
}
