package com.xyongfeng.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
@TableName("t_users_friend_inform")
@ApiModel(value="UsersFriendInform对象", description="")
public class UsersFriendInform implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "通知消息类型 0 好友申请 1 好友消息")
    private Integer type;

    @ApiModelProperty(value = "发送消息的userid")
    private Integer fromId;

    @ApiModelProperty(value = "接收消息的userid")
    private Integer toId;

    @ApiModelProperty(value = "消息内容")
    private String content;

    @ApiModelProperty(value = "0 未读 1 已读")
    private Integer state;

    @ApiModelProperty(value = "发送时间")
    private LocalDateTime sendTime;

    @ApiModelProperty(value = "发送消息的用户")
    @TableField(exist = false)
    private Users fromer;
}
