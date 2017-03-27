package com.wpt.model;

import lombok.Data;

import java.util.Date;

/**
 * Created by zhanglp on 2017/3/21.
 */
@Data
public class AccessToken {
    private long userID;
    private String userName;
    private Date  CDate;
    private Date  EDate;
}
