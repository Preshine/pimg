package com.preshine.img.controller;


import com.preshine.img.entity.User;
import com.preshine.img.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Preshine
 * @since 2018-11-13
 */
@Controller
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private IUserService userService;

    @RequestMapping(value = "/list")
    @ResponseBody
    public List<User> list(@RequestBody Map<String, Object> requestBody,
                           HttpServletRequest request, HttpServletResponse response) {
        return userService.selectList(null);
    }

}

