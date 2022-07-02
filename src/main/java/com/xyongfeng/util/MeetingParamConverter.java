package com.xyongfeng.util;

import com.xyongfeng.pojo.Meeting;
import com.xyongfeng.pojo.MeetingAddParam;
import com.xyongfeng.pojo.MeetingUpdateParam;


/**
 * Meeting字段转换器
 * @author xyongfeng
 */

public class MeetingParamConverter {

    public static Meeting getMeeting(MeetingAddParam meet){
        return new Meeting()
                .setStartDate(meet.getStartDate())
                .setHaveLicence(meet.getHaveLicence())
                .setName(meet.getName());

    }


    public static Meeting getMeeting(MeetingUpdateParam meet){
        return new Meeting()
                .setId(meet.getId())
                .setStartDate(meet.getStartDate())
                .setHaveLicence(meet.getHaveLicence())
                .setName(meet.getName());

    }


}
