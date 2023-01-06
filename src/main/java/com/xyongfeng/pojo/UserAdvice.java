package com.xyongfeng.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

/**
 * <p>
 *
 * </p>
 *
 * @author xyongfeng
 * @since 2022-12-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_user_advice")
@ApiModel(value = "UserAdvice对象", description = "")
public class UserAdvice implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "用户id")
    private Integer userId;

    @ApiModelProperty(value = "意见种类",required = true)
    @NotBlank(message = "意见种类不能为空")
    private String type;

    @ApiModelProperty(value = "描述标题",required = true)
    @NotBlank(message = "描述标题不能为空")
    private String title;

    @ApiModelProperty(value = "描述内容",required = true)
    @NotBlank(message = "描述内容不能为空")
    private String content;


    @ApiModelProperty(value = "发起时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime time;

    @ApiModelProperty(value = "用户")
    @TableField(exist = false)
    private Users users;

    @ApiModelProperty(value = "上传的图片")
    @TableField(exist = false)
    private List<String> imgs;
}
