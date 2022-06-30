package com.xyongfeng.pojo;

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
 * @since 2022-06-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_meeting")
@ApiModel(value="Meeting对象", description="")
public class Meeting implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "会议名称")
    private String name;

    @ApiModelProperty(value = "创建会议的用户id")
    private Integer userId;

    @ApiModelProperty(value = "创建会议的时间")
    private LocalDateTime createDate;

    @ApiModelProperty(value = "会议开始时间")
    private LocalDateTime startDate;


}
