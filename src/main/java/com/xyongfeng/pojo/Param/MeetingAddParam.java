package com.xyongfeng.pojo.Param;


import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @author xyongfeng
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "Meeting添加实体类")
public class MeetingAddParam {

    @ApiModelProperty(value = "会议名称",required = true)
    @NotBlank(message = "会议名不能为空")
    private String name;

    @ApiModelProperty(value = "会议开始时间",required = true)
    @NotNull(message = "开始时间不能为空")
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime startDate;

    @ApiModelProperty(value = "进入会议是否需要创建者认可")
    @TableField("haveLicence")
    private Boolean haveLicence;

}
