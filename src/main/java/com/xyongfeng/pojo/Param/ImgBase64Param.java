package com.xyongfeng.pojo.Param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author xyongfeng
 */
@ApiModel(description = "base64上传图片")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImgBase64Param {

    @ApiModelProperty(value = "base64编码后的图片",required = true)
    @NotBlank(message = "图片不能为空")
    private String imgBase64;

    private String meetingId;

    private Integer userId;
}
