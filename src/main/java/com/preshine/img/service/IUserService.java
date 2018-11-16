package com.preshine.img.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.preshine.img.entity.User;
import com.baomidou.mybatisplus.service.IService;

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

    Page<Map<String, Object>> getUserPage(Page page);
}
