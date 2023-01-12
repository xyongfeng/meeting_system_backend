package com.xyongfeng.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
 * @since 2022-09-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_meeting_notice_users_xq")
@ApiModel(value="MeetingNoticeUsers对象", description="")
public class MeetingNoticeUsers implements Serializable {

    private static final long serialVersionUID=1L;
    @TableId(value = "id_xq", type = IdType.AUTO)
    private Integer id;

    @TableField("user_id_xq")
    @ApiModelProperty(value = "接收人id")
    private Integer userId;

    @TableField("notice_id_xq")
    @ApiModelProperty(value = "通知id")
    private Integer noticeId;

    @TableField("state_xq")
    @ApiModelProperty(value = "0 未读 1已读")
    private Integer state;


}
