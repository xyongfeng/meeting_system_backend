package com.xyongfeng.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

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
@TableName("t_user_advice_img_xq")
@ApiModel(value="UserAdviceImg对象", description="")
public class UserAdviceImg implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id_xq", type = IdType.AUTO)
    private Integer id;

    @TableField("advice_id_xq")
    @ApiModelProperty(value = "意见id")
    private Integer adviceId;

    @TableField("img_path_xq")
    @ApiModelProperty(value = "图片路径")
    private String imgPath;


}
