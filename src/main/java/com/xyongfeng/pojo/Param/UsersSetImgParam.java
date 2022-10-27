package com.xyongfeng.pojo.Param;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "User设置头像")
public class UsersSetImgParam {
    @ApiModelProperty(value = "id")
    private Integer id;

    @ApiModelProperty(value = "图片",required = true)
    @NotNull(message = "图片不能为空")
    private MultipartFile file;


}
