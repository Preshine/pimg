package com.preshine.img.config;

import com.preshine.img.entity.Resources;
import com.preshine.img.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *     定义已登录用户信息
 * </p>
 * @author DanRui
 * @version 1.0.0
 * @since 2017-11-06
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SessionUser implements Serializable {

    private static final long serialVersionUID = 1764365572138947234L;

    /** 登录用户访问Token */
    private String token;

    /** 最后登录时间 */
    private Date lastLogintime;

    /** 密码加密key **/
    private String flag;

    /** 用户姓名 */
    private String userName;

    /** 用户手机号 */
    private String mobile;

    /** 用户密码 */
    private String accountPw;

    /** 用户邮箱 */
    private String email;

    /** 自增长账户id */
    private Integer accountId;

    /** 系统代码 */
    private String systemCode;

    /** 登录名 */
    private Integer userAccount;

    /** 企业ID */
    private Integer unitID;

    /** 账户头像路径 */
    private String avatar;

    /** 用户拥有的资源 */
    private List<Resources> userResources;

    /** 用户拥有的角色 */
    private List<Role> userRoles;

}

