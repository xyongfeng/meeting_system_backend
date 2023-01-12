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
 * @since 2022-12-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_meeting_screenshot_xq")
@ApiModel(value="MeetingScreenshot对象", description="")
public class MeetingScreenshot implements Serializable {

    private static final long serialVersionUID=1L;
    @TableId(value = "id_xq", type = IdType.AUTO)
    private Integer id;

    @TableField("user_id_xq")
    private Integer userId;

    @TableField("meeting_id_xq")
    private String meetingId;

    @TableField("path_xq")
    private String path;

    @TableField(exist = false)
    private Meeting meeting;

}
