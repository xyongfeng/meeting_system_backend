package com.xyongfeng.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xyongfeng.pojo.Admins;
import com.xyongfeng.pojo.MyPage;

import java.util.List;


/**
 * @author xyongfeng
 */

public interface AdminsService extends IService<Admins> {
    /**
     * 登录管理页面
     * @param username 账号
     * @param password 密码
     * @return 登录是否成功
     */
    Admins adminLogin(String username,String password);
    /**
     * 增加新的管理员
     * @param admins 增加的对象
     * @return 返回执行状态
     *
     */
    int adminAdd(Admins admins) throws Exception;
    /**
     * 分页获取管理员列表
     * @param myPage current(当前页码),size(页码大小)
     *
     * @return 管理员列表
     */
    List<Admins> listPage(MyPage myPage);
    /**
     * 根据id修改管理员
     * @param admins 修改的对象
     *
     * @return 修改结果
     */
    int adminUpdateById(Admins admins);
    /**
     * 根据id删除管理员
     * @param id 删除的管理员id
     *
     * @return 删除结果
     */
    Admins adminDelById(Integer id);

    /**
     * 通过username获取admin
     * @param username
     * @return
     */
    Admins getAdminByUserName(String username);
}
