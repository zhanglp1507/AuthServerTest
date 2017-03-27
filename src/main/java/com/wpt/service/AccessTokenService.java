package com.wpt.service;

import com.wpt.model.AccessToken;

/**
 * Created by zhanglp on 2017/3/21.
 */
public interface AccessTokenService {
    /**
     * 根据客户端用户信息生成token
     * @param accessToken
     * @return
     */
    String  getAccessToken(AccessToken accessToken ) throws ClassNotFoundException;

    /**
     * 根据客户端token信息，验证客户端是否有效
     * @param accessToken
     * @return
     */
    String verifyToken(String accessToken);




}
