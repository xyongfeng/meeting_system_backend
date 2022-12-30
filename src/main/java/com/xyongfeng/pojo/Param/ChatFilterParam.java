package com.xyongfeng.pojo.Param;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatFilterParam {

    private Integer id;

    private String filterContent;

    private Integer filterRule;

    private String replaceContent;
}
