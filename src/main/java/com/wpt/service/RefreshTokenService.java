package com.wpt.service;

import com.wpt.model.RefreshToken;

/**
 * Created by zhanglp on 2017/3/21.
 */
public interface RefreshTokenService {
    /**
     * 生成refreshtoken，随机数据
     * @return
     */
     String createRefreshToken(RefreshToken refreshToken);

    /**
     * 生成AccessToken
     * @return
     */
     String createAccessToken(String refreshToken);
}
