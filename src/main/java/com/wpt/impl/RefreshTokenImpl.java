package com.wpt.impl;

import com.alibaba.fastjson.JSON;
import com.wpt.common.CommonUtil;
import com.wpt.common.DateUtil;
import com.wpt.common.RedisUtil;
import com.wpt.model.AccessToken;
import com.wpt.model.RefreshToken;
import com.wpt.service.AccessTokenService;
import com.wpt.service.RefreshTokenService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Created by zhanglp on 2017/3/21.
 */
@Service
public class RefreshTokenImpl implements RefreshTokenService {

    private static final int RandLength = 20;
    private static final int ExpireDays = 7;

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private AccessTokenService  accessTokenService;

    /**
     * 生成refreshtoken，随机数据,
     * @return
     */
    public String createRefreshToken(RefreshToken refreshToken){
        String tokenValue = "";
        try{
            tokenValue = CommonUtil.getRandomString(RandLength);
            refreshToken.setCDate(new Date());
            refreshToken.setEDate(DateUtil.addDays(refreshToken.getCDate(), ExpireDays));
            refreshToken.setRefreshToken(tokenValue);

            redisUtil.hashSet("TokenServer:RefreshToken",tokenValue,refreshToken.toString(),3600 * 24 * ExpireDays );

            return tokenValue;
        }
        catch(Exception ex){

        }

        return tokenValue;
    }

    /**
     * 生成AccessToken
     * @return
     */
    public String createAccessToken(String refreshToken){

        try{
            String reToken = redisUtil.hashGet("TokenServer:RefreshToken", refreshToken);
            if(StringUtils.isEmpty(reToken)){ // 没有对应token，或者token已过期
                return "";
            }

            RefreshToken tokenModel = JSON.parseObject(reToken,RefreshToken.class);
            if(tokenModel == null){
                return "";
            }

            AccessToken accessToken = new AccessToken();
            accessToken.setUserID(Long.parseLong(tokenModel.getClientID()));
            String newAccessToken = accessTokenService.getAccessToken(accessToken);
            return  newAccessToken;

        }catch (Exception ex){

        }

        return "";
    }
}
