package com.jalen.jo.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by jh on 2015/3/2.
 *
 * 数据验证
 * 1、在构造方法中对配置文件进行读取并赋予对应的M层变量
 * 2、提供对应的public方法供外部调用验证
 */
public class VerifyUtil {
    private static VerifyUtil mIntance;
//    M
    private String regExpPhone;
    private String regExpEmail;
    private String regExpUsername;
    private String regExpNickname;

    private VerifyUtil(){
//        对M进行初始化
        regExpPhone = "^1[3458]\\d{9}$";
        regExpEmail = "";
        regExpNickname = "";
        regExpUsername = "";
    }

    public static VerifyUtil getIntance(){
        if (mIntance != null){
            return mIntance;
        }else{
            mIntance = new VerifyUtil();
            return mIntance;
        }
    }

    public boolean isPhoneNumber(String phoneStr){
        return  verify(phoneStr, regExpPhone);
    }
    public boolean isUsername(String usernameStr){
        return  verify(usernameStr, regExpUsername);
    }
    public boolean isNickname(String nicknameStr){
        return  verify(nicknameStr, regExpNickname);
    }

    private boolean verify(String str, String regExpStr) {
        Pattern p = Pattern.compile(regExpStr);
        Matcher m = p.matcher(str);
        if (!m.matches()) {
            // 不对
            return false;
        }
        return true;
    }

}
