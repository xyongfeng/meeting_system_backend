package com.xyongfeng.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import java.io.Serializable;
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
 * @since 2022-08-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_meeting_inform")
@ApiModel(value="MeetingInform对象", description="")
public class MeetingInform implements Serializable {

    private static final long serialVersionUID=1L;

    private Integer id;

    @ApiModelProperty(value = "0 会议申请 1 会议通知")
    private Integer type;

    @ApiModelProperty(value = "发送的userid")
    private Integer fromUid;

    @ApiModelProperty(value = "发送通知的meetid（会议通知）")
    private String fromMid;

    @ApiModelProperty(value = "接收通知的userid（会议通知）")
    private Integer toUid;

    @ApiModelProperty(value = "接收申请的meetid（会议申请）")
    private String toMid;

    @ApiModelProperty(value = "通知标题（会议通知）")
    private String title;

    @ApiModelProperty(value = "内容")
    private String content;

    @ApiModelProperty(value = "0 未读 1 已读")
    private Integer state;

    @ApiModelProperty(value = "发送时间")
    private LocalDateTime sendTime;


}
