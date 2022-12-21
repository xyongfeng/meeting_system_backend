package com.xyongfeng.pojo;

import com.baomidou.mybatisplus.annotation.*;

import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
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
 * @since 2022-07-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_meeting")
@ApiModel(value="Meeting对象", description="")
public class Meeting implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty(value = "会议名称")
    private String name;

    @ApiModelProperty(value = "创建会议的用户id")
    private Integer userId;

    @ApiModelProperty(value = "创建会议的时间")
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime createDate;

    @ApiModelProperty(value = "会议开始时间")
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime startDate;

    @ApiModelProperty(value = "会议结束时间")
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime endDate;

    @ApiModelProperty(value = "进入会议是否需要创建者认可")
    @TableField("haveLicence")
    private Boolean haveLicence;

    @ApiModelProperty(value = "会议是否已经结束")
    @TableField("end")
    @TableLogic(value = "0",delval = "1")
    private Boolean end;

    @ApiModelProperty(value = "主持人删除了该会议的记录就对他隐藏")
    @TableField("to_owner_hidden")
    private Boolean toOwnerHidden;

    @ApiModelProperty(value = "创建用户")
    @TableField(exist = false)
    private Users users;


    @ApiModelProperty(value = "参加用户列表")
    @TableField(exist = false)
    private List<Users> participants;


}
