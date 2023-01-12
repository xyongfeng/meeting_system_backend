package com.xyongfeng.pojo;


import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author xyongfeng
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "好友信息及好友聊天记录", description = "")
public class FriendsAndChat {
    private Integer uid;
    private String name;
    private String headImage;
    private List<UsersFriendInform> chatMessage;
}
