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
 * @since 2022-08-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_users_friend_xq")
@ApiModel(value="UsersFriend对象", description="")
public class UsersFriend implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id_xq", type = IdType.AUTO)
    private Integer id;

    @TableField("user_id1_xq")
    private Integer userId1;

    @TableField("user_id2_xq")
    private Integer userId2;


}
