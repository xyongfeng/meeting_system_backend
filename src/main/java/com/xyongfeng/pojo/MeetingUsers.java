package com.xyongfeng.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
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
 * @since 2022-12-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_meeting_users_xq")
@ApiModel(value = "MeetingUsers对象", description = "")
public class MeetingUsers implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id_xq", type = IdType.AUTO)
    private Integer id;

    @TableField("meeting_id_xq")
    private String meetingId;

    @TableField("users_id_xq")
    private Integer usersId;

    @TableField("had_sign_in_xq")
    @ApiModelProperty(value = "是否完成签到")
    private Boolean hadSignIn;

    @TableField("had_sign_in_time_xq")
    @ApiModelProperty(value = "签到时间")
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime hadSignInTime;

    @TableField("had_ban_xq")
    @ApiModelProperty(value = "是否被加入黑名单")
    private Boolean hadBan;

    @TableField("had_muted_xq")
    @ApiModelProperty(value = "是否被静言")
    private Boolean hadMuted;

    @TableField("had_banup_xq")
    @ApiModelProperty(value = "是否被禁止投屏")
    private Boolean hadBanup;

    @TableField("uping_xq")
    @ApiModelProperty(value = "投屏中")
    private Boolean uping;

    @TableField("speeching_xq")
    @ApiModelProperty(value = "语音中")
    private Boolean speeching;

    @TableField("exist_minute_xq")
    @ApiModelProperty(value = "参会累计时长(分钟)")
    private Integer existMinute;

    @TableField("to_user_hidden_xq")
    @ApiModelProperty(value = "删除了该会议的记录就对他隐藏")
    private Boolean toUserHidden;

    @TableField("is_founder_xq")
    @ApiModelProperty(value = "是否为会议主持人")
    private Boolean isFounder;

    @TableField(exist = false)
    private Users users;
}
