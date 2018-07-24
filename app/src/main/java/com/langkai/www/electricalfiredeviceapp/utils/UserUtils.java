package com.langkai.www.electricalfiredeviceapp.utils;

import java.util.HashMap;

public class UserUtils {

    private HashMap<String, UserInfo> userMap;

    private UserUtils() {
        userMap = new HashMap<>();

        initUsername("test001", new UserInfo("123456"));
    }

    private static class UserUtilsHolder{
        private final static UserUtils instance = new UserUtils();
    }

    public static UserUtils getInstance(){
        return UserUtilsHolder.instance;
    }

    public void initUsername(String username, UserInfo info){
        if(userMap.containsKey(username)) {
            userMap.remove(username);
        }

        userMap.put(username, info);
    }

    public boolean checkUserLogin(String username, String password){
        if(!userMap.containsKey(username))
            return false;

        UserInfo info = userMap.get(username);
        if(password.equals(info.password)){
            return true;
        }else{
            return false;
        }
    }

    public class UserInfo{
        public String password;

        public UserInfo(String password){
            this.password = password;
        }
    }
}
