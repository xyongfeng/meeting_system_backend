package com.xyongfeng.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * 
 * </p>
 *
 * @author xyongfeng
 * @since 2022-09-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_meeting_notice")
@ApiModel(value="MeetingNotice对象", description="")
public class MeetingNotice implements Serializable {

    private static final long serialVersionUID=1L;
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "会议id")
    private String meetingId;

    @ApiModelProperty(value = "发送人id")
    private Integer senderId;

    @NotBlank(message = "标题不能为空")
    @Length(max = 10, message = "标题长度不能大于10")
    @ApiModelProperty(value = "通知标题")
    private String title;

    @NotBlank(message = "通知内容不能为空")
    @ApiModelProperty(value = "通知内容")
    private String content;

    @ApiModelProperty(value = "发送时间")
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime sendTime;

    @ApiModelProperty(value = "公告类型 1 普通公告 2 推送公告")
    @NotNull(message = "公告类型不能为空")
    private Integer type;

    @ApiModelProperty(value = "编辑时间")
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime updateTime;

    @TableField(exist = false)
    @ApiModelProperty(value = "通知id")
    private Integer informId;

    @TableField(exist = false)
    @ApiModelProperty(value = "发送通知的用户")
    private Users sender;

    @TableField(exist = false)
    @ApiModelProperty(value = "通知来源的会议")
    private Meeting meeting;
}
