package com.wpt.model;

import lombok.Data;

import java.util.Date;

/**
 * Created by zhanglp on 2017/3/21.
 */
@Data
public class RefreshToken {
    private String clientID;
    private String clientSecret;
    private String refreshToken;
    private String clientMessage;
    private Date CDate;
    private Date EDate;
}
