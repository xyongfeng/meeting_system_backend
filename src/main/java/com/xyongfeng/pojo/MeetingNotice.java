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
@TableName("t_meeting_notice_xq")
@ApiModel(value="MeetingNotice对象", description="")
public class MeetingNotice implements Serializable {

    private static final long serialVersionUID=1L;
    @TableId(value = "id_xq", type = IdType.AUTO)
    private Integer id;

    @TableField("meeting_id_xq")
    @ApiModelProperty(value = "会议id")
    private String meetingId;

    @TableField("sender_id_xq")
    @ApiModelProperty(value = "发送人id")
    private Integer senderId;

    @TableField("title_xq")
    @NotBlank(message = "标题不能为空")
    @Length(max = 10, message = "标题长度不能大于10")
    @ApiModelProperty(value = "通知标题")
    private String title;

    @TableField("content_xq")
    @NotBlank(message = "通知内容不能为空")
    @ApiModelProperty(value = "通知内容")
    private String content;

    @TableField("send_time_xq")
    @ApiModelProperty(value = "发送时间")
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime sendTime;

    @TableField("type_xq")
    @ApiModelProperty(value = "公告类型 1 普通公告 2 推送公告")
    @NotNull(message = "公告类型不能为空")
    private Integer type;

    @TableField("update_time_xq")
    @ApiModelProperty(value = "编辑时间")
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime updateTime;

    @TableField("hidden_xq")
    @ApiModelProperty(value = "隐藏，不会出现在公告列表里面，通常用来发送私有通知")
    private Boolean hidden;


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
