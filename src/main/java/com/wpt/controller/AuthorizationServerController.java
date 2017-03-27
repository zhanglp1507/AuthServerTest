package com.wpt.controller;

import com.wpt.common.CommonUtil;
import com.wpt.common.DateUtil;
import com.wpt.common.RedisUtil;
import com.wpt.impl.AccessTokenImpl;
import com.wpt.impl.RefreshTokenImpl;
import com.wpt.model.AccessToken;
import com.wpt.model.RefreshToken;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.servlet.http.HttpServletRequest;
import javax.annotation.Resource;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhanglp on 2017/3/21.
 */
@Controller
public class AuthorizationServerController {

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private AccessTokenImpl accessTokenImpl;
    private RefreshTokenImpl refreshTokenImpl;

    @RequestMapping(value = "/testRSA_Crypt")
    public String testRSA_Crypt(){
        String randString = CommonUtil.getRandomString(10);

        return  "index";
    }

    @RequestMapping(value = "/testRedis")
    public String testRedis(){
       // redisUtil.setString("RefreshToken:123","123");

        redisUtil.hashSet("RefreshToken","1234","123",3600 * 24 * 7);
        return  "index";
    }



    /**
     * 登录获取accessToken和refreshToken
     */
    @RequestMapping(value = "/getAccessToken")
    public String getAccessToken(HttpServletRequest request){

        int userID = 100;
        System.out.print(request.getParameter("grant_type"));
        System.out.print(request.getParameter("username"));
        System.out.print(request.getParameter("password"));
        System.out.print(request.getParameter("Scope"));
        String userName = request.getParameter("username");
        //  获取authorization 数据
        String  authorizationInfo = "";
        Enumeration headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
         //   String value = request.getHeader(key);
            if(key == "authorization"){
                authorizationInfo = request.getHeader(key);
                break;
            }
        }

        if(StringUtils.isEmpty(authorizationInfo)){
            return "";
        }

        // 解析授权参数
        String[]  authInfoArray = authorizationInfo.split(":");

        // 验证用户名密码

        // 生成AccessToken
        Date cDate = new Date();
        Date eDate = DateUtil.addHours(cDate,7);
        AccessToken  accessToken = new AccessToken();
        accessToken.setUserID(userID);
        accessToken.setUserName(userName);
        accessToken.setCDate(cDate);
        accessToken.setEDate(eDate);

        //  生成RefreshToken
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setClientID("");
        refreshToken.setClientSecret("");
        refreshToken.setCDate(cDate);
        refreshToken.setClientMessage(userName);

        try {
            String acceToken = accessTokenImpl.getAccessToken(accessToken);
            String reToken = refreshTokenImpl.createRefreshToken(refreshToken);
            if(StringUtils.isEmpty(acceToken)){
                return "";
            }
            if(StringUtils.isEmpty(reToken)){
                return "";
            }
            return acceToken;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return  "index";
    }


    /**
     * 根据刷新token 刷新生成新的accessToken
     * @param request
     * @return
     */
    @RequestMapping(value = "/refreshToken")
    public String refreshToken(HttpServletRequest request){
        String newAccessToken = "";
        String grantType = request.getParameter("grant_type");
        String refreshToken = request.getParameter("refresh_token");
        try{
            if(grantType != "refresh_token"){
                return newAccessToken;
            }
            newAccessToken = refreshTokenImpl.createAccessToken(refreshToken);
        }
        catch (Exception ex){

        }

        return newAccessToken;
    }


}
