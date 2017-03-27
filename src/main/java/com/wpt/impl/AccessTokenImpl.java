package com.wpt.impl;

import com.wpt.common.DateUtil;
import com.wpt.common.EncryptionUtil;
import com.wpt.model.AccessToken;
import com.wpt.service.AccessTokenService;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by zhanglp on 2017/3/21.
 */
@Service
public class AccessTokenImpl implements AccessTokenService {


     static final String PRIVATE_KEY_FILE = "D:/rsa_keystore/private.key";

    /**
     * String to hold name of the public key file.
     */
     static final String PUBLIC_KEY_FILE = "D:/rsa_keystore/public.key";

     static final int ExpireHours = 2;

    /**
     * 根据客户端用户信息生成token
     * @param accessToken
     * @return
     */
    public String  getAccessToken(AccessToken accessToken ) throws ClassNotFoundException {
        String encString = "";
        String userID = Long.toString(accessToken.getUserID());
        String userName = accessToken.getUserName();
        String cDate = accessToken.getEDate().toString();

        encString = userID + "||" + userName + "||" + cDate;

        ObjectInputStream inputStream = null;

        // Encrypt the string using the public key
        try {
            inputStream = new ObjectInputStream(new FileInputStream(
                    PUBLIC_KEY_FILE));
            final PublicKey publicKey = (PublicKey) inputStream.readObject();
            final byte[] encryptedString = EncryptionUtil.encrypt(encString,publicKey);

            // use String to hold cipher binary data
            Base64 base64 = new Base64();
            String cipherTextBase64 = base64.encodeToString(encryptedString);
            return cipherTextBase64;
        } catch (Exception e) {
            e.printStackTrace();
        }


        return "";
    }

    /**
     * 根据客户端token信息，验证客户端是否有效
     * @param accessToken
     * @return
     */
    public String verifyToken(String accessToken){
        if(StringUtils.isEmpty(accessToken)){
            return "";
        }

        ObjectInputStream inputStream = null;

        // Encrypt the string using the public key
        try {
            inputStream = new ObjectInputStream(new FileInputStream(
                    PRIVATE_KEY_FILE));
            final PrivateKey privateKey = (PrivateKey) inputStream.readObject();

            // use String to hold cipher binary data
            Base64 base64 = new Base64();
            // get cipher binary data back from String
            byte[] cipherTextArray = base64.decode(accessToken);

            String decryptedString = EncryptionUtil.decrypt(cipherTextArray, privateKey);

            String[]  decryptArray = decryptedString.split("||");

            // 验证token是否已过期
            Date nDate = Calendar.getInstance().getTime();
            Date cDate = DateUtil.parse(decryptArray[2]);

            Date eDate = DateUtil.addHours(cDate,ExpireHours);
            if(nDate.getTime() >= eDate.getTime()){
                return "";
            }

            return decryptArray[0];
        } catch (Exception e) {
            e.printStackTrace();
        }




        return "";
    }
}
