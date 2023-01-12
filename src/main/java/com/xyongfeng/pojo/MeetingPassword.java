package com.xyongfeng.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * @since 2023-01-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_meeting_password_xq")
@ApiModel(value="MeetingPassword对象", description="")
public class MeetingPassword implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id_xq", type = IdType.AUTO)
    private Integer id;

    @TableField("meeting_id_xq")
    private String meetingId;

    @TableField("password_xq")
    @ApiModelProperty(value = "会议密码")
    private String password;

    @TableField("enabled_xq")
    @ApiModelProperty(value = "是否启用")
    private Boolean enabled;


}
