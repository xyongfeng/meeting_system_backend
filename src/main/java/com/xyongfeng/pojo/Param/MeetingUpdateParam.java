package com.xyongfeng.pojo.Param;


import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @author xyongfeng
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "Meeting修改实体类")
@Accessors(chain = true)
public class MeetingUpdateParam {
    @ApiModelProperty(value = "会议id",required = true)
//    @NotNull(message = "会议id不能为空")
    private String id;

    @ApiModelProperty(value = "会议名称")
    @NotBlank(message = "会议名不能为空")
    private String name;

    @ApiModelProperty(value = "会议开始时间")
    @NotNull(message = "开始时间不能为空")
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime startDate;

    @ApiModelProperty(value = "进入会议是否需要创建者认可")
    @TableField("haveLicence")
    private Boolean haveLicence;

    @ApiModelProperty(value = "参会最大人数")
    @Max(value = 100, message = "人数最多不能超过100")
    @Min(value = 1, message = "人数不能少于1")
    private Integer maxNumber;

    @ApiModelProperty(value = "进入房间是否需要人脸验证")
    private Boolean needFace;

    @ApiModelProperty(value = "入会密码")
    @Length(max = 20, message = "密码长度不能大于20")
    private String password;

    @ApiModelProperty(value = "密码验证开启")
    private Boolean passEnabled;
}
