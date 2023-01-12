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
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author xyongfeng
 * @since 2022-09-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_meeting_application_xq")
@ApiModel(value="MeetingApplication对象", description="")
public class MeetingApplication implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id_xq", type = IdType.AUTO)
    private Integer id;

    @TableField("applicant_id_xq")
    @ApiModelProperty(value = "申请人id")
    private Integer applicantId;

    @TableField("meeting_id_xq")
    @ApiModelProperty(value = "会议id")
    private String meetingId;

    @TableField("state_xq")
    @ApiModelProperty(value = "0 未读 1 已读")
    private Integer state;

    @TableField("send_time_xq")
    @ApiModelProperty(value = "发送时间")
    private LocalDateTime sendTime;

    @TableField(exist = false)
    private Meeting meeting;

    @TableField(exist = false)
    private Users users;
}
