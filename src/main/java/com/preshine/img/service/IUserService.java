package com.preshine.img.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.preshine.img.entity.User;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Preshine
 * @since 2018-11-13
 */
public interface IUserService extends IService<User> {

    Page<Map<String, Object>> getUserPage(Page page, String userName, String mobile, String email, String realName, Integer sex, String sorterField, String sorterOrder);

    Page<Map<String, Object>> getUsersByRole(Page page, String userName, String mobile, List<Integer> userAccounts);
}
