package com.preshine.img.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.preshine.img.entity.Resources;
import com.preshine.img.entity.Role;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;


@Aspect
@Component
public class LoginAspect {

    /** 缓存用户信息  */
    public static final Map<String, Map<String, String>> cache = new HashMap<>();

    /** 校验Token有效性地址 */
//    @Value("${configServiceUrl}")
    private String configServiceUrl = "";

    /** 分配的系统代码 */
    private String systemCode = "333";

    /** JSSO免登录token */
    private String unLoginTokenId = "666";

    /** 日志管理器 */
    private static final Logger LOGGER = LogManager.getLogger(LoginAspect.class);


    /** 定义一个切点，匹配Logined注解 */
    @Pointcut("@annotation(com.preshine.img.config.Logined)")
    public void pointcut() {
    }

    /** 定义前置增强 */
    @Before("pointcut()&&@annotation(logined)")
    public void doBefore(JoinPoint joinPoint, Logined logined) throws Exception{

        String loginedName = logined.name();

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();

        String JSSOSESSIONID = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                String name = cookie.getName();
                if ("JSSOSESSIONID".equals(name)) {
                    JSSOSESSIONID = cookie.getValue();
                    break;
                }
            }
        }

        String JSSOSESSIONID_form = getJSSOSESSIONID(request);

        if (JSSOSESSIONID_form != null && !JSSOSESSIONID_form.equals("")) {
            JSSOSESSIONID = JSSOSESSIONID_form;
        }

        boolean result = false;
        // JSSOSESSIONID判断是否为空
        if (JSSOSESSIONID == null) {
            throw new Exception("登录超时");
        } else {
            result = checkLogin(request, JSSOSESSIONID, systemCode);
            if (!result) {
                throw new Exception("登录超时");
            }

            // 判断是否有权限访问
            boolean isAuthorized = true;
            if(loginedName != null && "manage".equals(loginedName.toLowerCase()) )
            {
                isAuthorized = checkAuthorization(request);
                if (!isAuthorized) {
                    throw new Exception("无权限访问");
                }
            }else {
                //默认只需要登录，无需判断权限
            }
        }
    }


    /**
     * 检查登录是否超时
     *
     * @param  request          HttpServletRequest
     * @param  JSSOSESSIONID    String
     * @param  systemCode       String
     * @return boolean
     */
    boolean checkLogin(HttpServletRequest request, String JSSOSESSIONID, String systemCode) throws Exception {

        SessionUser sessionUser = null;
        Map<String, String> obj  = cache.get(JSSOSESSIONID);
        if (obj == null) {
            obj = new HashMap<>();
        }
        String userInfo = obj.get("userInfo");

        if (userInfo != null) {
            sessionUser = JSON.parseObject(userInfo, SessionUser.class);
        }

        if (sessionUser == null) {

            //sessionUser = getSessionUser(JSSOSESSIONID);
            //if (sessionUser == null) {
                throw new Exception("登录超时");
            //}

//            obj.put("userInfo", JSON.toJSONString(sessionUser));
//            cache.put(JSSOSESSIONID, obj);
//            request.setAttribute("sessionUser", sessionUser);
//
//            return true;

        } else {
            request.setAttribute("sessionUser", sessionUser);
            return true;
        }
    }

    public SessionUser getSessionUser(String JSSOSESSIONID) throws Exception{
        SessionUser sessionUser = new SessionUser();
//        Map<String, String> headers_map = new HashMap<String, String>();
//        headers_map.put("Accept", "application/json, text/javascript, */*; q=0.01");
//        headers_map.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
//
//        Map<String, String> paramMap = new HashMap<String, String>();
//        paramMap.put("token", JSSOSESSIONID);
//        paramMap.put("systemCode", systemCode);
//        paramMap.put("unLoginTokenId", unLoginTokenId);
//        List<String> menuList = new ArrayList<String>();
//        String jsonResultStr = "";
//        try {
//            jsonResultStr = OkHttpUtils.getInstance().post(configServiceUrl + "user/verify", Headers.of(headers_map),
//                    paramMap);
//        } catch (IOException e) {
//            logger.error("token验证服务异常", e);
//            throw new Exception("token验证服务异常！");
//        }
        String jsonResultStr = "";
        JSONObject jsonObject = JSON.parseObject(jsonResultStr);
        String objStr = jsonObject.getString("obj");
        if (objStr == null || objStr.equals("")) {
            throw new Exception("登录超时");
        }

        // 获取用户信息，放入本地缓存
        JSONObject userObj = JSON.parseObject(objStr);

        String userStr = userObj.getString("user");
        if (userStr == null || userStr.equals("")) {
            throw new Exception("登录超时");
        }

        JSONObject user = JSON.parseObject(userStr);

        sessionUser.setLastLogintime(user.getDate("lastLogintime"));
        sessionUser.setFlag(user.getString("flag"));
        sessionUser.setUserName(user.getString("userName"));
        sessionUser.setMobile(user.getString("mobile"));
        sessionUser.setAccountPw(user.getString("accountPw"));
        //用户邮箱
        sessionUser.setEmail(user.getString("mailAcc"));
        //自增长账户id
        sessionUser.setAccountId(user.getInteger("accountId"));
        // 系统代码
        sessionUser.setSystemCode(user.getString("systemCode"));
        // 登录名
        sessionUser.setUserAccount(user.getInteger("jiaoBaoHao"));
        //企业ID
        sessionUser.setUnitID(user.getInteger("defaultUnitID"));
        // 设置账户头像路径
        sessionUser.setAvatar(user.getString("avatar"));
        sessionUser.setToken(JSSOSESSIONID);

        // 获取用户权限资源
        String userPermissionStr = userObj.getString("permissions");
        List<Resources> esources = new ArrayList<>();
        if (userPermissionStr != null && !userPermissionStr.equals("")) {
            JSONArray userPermissions = JSONArray.parseArray(userPermissionStr);
            for (int i = 0; i < userPermissions.size(); i++) {
                Resources resources = userPermissions.getObject(i, Resources.class);
                esources.add(resources);
            }
        }

        if (esources.size() > 0) {
            sessionUser.setUserResources(esources);
        }

        // 获取用户角色信息
        String userRoleStr = userObj.getString("roles");
        List<Role> userRoles = new ArrayList<>();
        if (userRoleStr != null && !userRoleStr.equals("")) {
            JSONArray userRolesArray = JSONArray.parseArray(userRoleStr);
            for (int i = 0; i < userRolesArray.size(); i++) {
                Role role = userRolesArray.getObject(i, Role.class);
                userRoles.add(role);
            }
        }

        if (userRoles.size() > 0) {
            sessionUser.setUserRoles(userRoles);
        }
        return sessionUser;
    }

    /**
     * 检查是否有权限访问，无权限访问直接抛出异常
     * @param   request   HttpServletRequest
     * @return  boolean
     */
    boolean checkAuthorization(HttpServletRequest request) throws Exception {
        SessionUser sessionUser = (SessionUser) request.getAttribute("sessionUser");

        if (null == sessionUser) {
            throw new Exception("登录超时");
        }

        // 获取用户所有资源
        List<Resources> userResourceList = sessionUser.getUserResources();

        if (null == userResourceList || userResourceList.size() == 0) {
            return false;
        }

        String controllerName = "controller";
        String currentController = "";
        String currentMethod = "";
        String mappingURL = request.getServletPath();
        if (mappingURL != null && !mappingURL.equals("")) {
            mappingURL = mappingURL.replaceAll("//", "/");
            String[] paths = mappingURL.split("/");

            if (paths.length == 4) {
                currentController = paths[2];
                currentMethod = paths[3];
            } else {
                return false;
            }

            if (currentController == null || currentController.equals("") || currentMethod == null || currentMethod.equals("")) {
                return false;
            }

            // 在用户所有资源中遍历Controller节点
            Optional<Resources> controllerResource = userResourceList.stream().filter(p -> p.getName() != null && p.getPath() != null).filter(k -> k.getPath().equals(controllerName)).findFirst();

            if (controllerResource.isPresent()) {
                // 获取Controller节点下所有的控制器
                Resources controller = controllerResource.get();
                List<Resources> userControllerList = userResourceList.stream().filter(p -> p.getParentId() != null)
                        .filter(k -> k.getParentId().equals(controller.getId())).collect(Collectors.toList());

                // 用户所有的控制器资源
                int countController = 0;
                if (null != userControllerList && userControllerList.size() > 0) {
                    for (int i = 0; i < userControllerList.size() ; i ++) {
                        Resources tmp = userControllerList.get(i);
                        if (!currentController.equalsIgnoreCase(tmp.getPath())) {
                            // 本次循环控制器不相等
                            countController ++;
                            continue;
                        } else {
                            // 正是当前访问的控制器，继续查找方法是否相等
                            int countMethod = 0;
                            // 获取每个控制器下的方法列表
                            List<Resources> methodList = userResourceList.stream().filter(p -> p.getParentId() != null)
                                    .filter(k -> k.getParentId().equals(tmp.getId())).collect(Collectors.toList());

                            if (null != methodList && methodList.size() > 0) {
                                // 循环获取每个方法名称，并放入列表中方便对比
                                for (Resources userResource : methodList) {
                                    if (!currentMethod.equalsIgnoreCase(userResource.getPath())) {
                                        // 本次循环的方法不相等
                                        countMethod ++;
                                        continue;
                                    } else {
                                        return true;
                                    }
                                }
                            }
                            // 方法循环结束，仍未找到匹配的方法名
                            if (countMethod == methodList.size()) {
                                return false;
                            }
                        }
                    }

                    // 控制器循环结束，仍未找到匹配的
                    if (countController == userControllerList.size()) {
                        return false;
                    }

                } else {
                    return false;
                }
            }
            return false;
        }
        return true;
    }


    /**
     *
     * 获取jssosessionid
     * @param   request  HttpServletRequest
     * @return  String
     */
    private String getJSSOSESSIONID(HttpServletRequest request) {
//        Map<String, String> parmMap = new HashMap<String, String>();
        //方式一：getParameterMap()，获得请求参数map
        Map<String, String[]> map = request.getParameterMap();
        //参数名称
        Set<String> key = map.keySet();
        //参数迭代器
        Iterator<String> iterator = key.iterator();
        while (iterator.hasNext()) {
            String k = iterator.next();
            if (k.equals("JSSOSESSIONID")) {
                return map.get(k)[0];
            }
        }

        //方式二：getParameterNames()：获取所有参数名称
        Enumeration<String> a = request.getParameterNames();
        String parm = null;
        String val = "";
        while (a.hasMoreElements()) {
            //参数名
            parm = a.nextElement();
            //值
            val = request.getParameter(parm);

            if (parm.equals("JSSOSESSIONID")) {
                return val;
            }
        }
        return null;
    }

}
