package com.rctapp.database;

import android.content.Context;
import android.content.SharedPreferences;

public class UserPreference {
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Context mContext;

    private static final int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "surveyor";
    private static final String NAME= "name";
    private static final String TOKEN = "token";
    private static final String REFRESH_TOKEN = "access_token";
    private static final String TOKEN_TYPE = "token_type";
    private static final String USER_ID = "user_id";
    private static final String IS_LOGGED_IN = "loggedIn";
    private static final String ROLE = "role";
    private static final String BUYER = "buyer";
    private static final String SELLER = "seller";
    private static final String ACTIVE = "active";
    private static final String PHONE = "phone";
    private static final String DIAL_CODE = "code";
    private static final String ACTIVE_LOCALE = "locale";
    private static final String IS_PASSED = "passed";
    private static final String IMAGE = "image";


    public UserPreference(Context context) {
        this.mContext = context;
        preferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = preferences.edit();
    }

    public void createLogin(String token, String refreshToken) {
        editor.putString(TOKEN, token);
        editor.putString(REFRESH_TOKEN, refreshToken);
        editor.commit();
    }

    public void setIsLoggedIn(boolean loggedIn){
        editor.putBoolean(IS_LOGGED_IN, loggedIn);
        editor.commit();
    }

    public boolean getLoggedIn(){
        return preferences.getBoolean(IS_LOGGED_IN,false);
    }

    public void setIsBuyer(boolean buyer){
        editor.putBoolean(BUYER, buyer);
        editor.commit();
    }

    public boolean getBuyer(){
        return  preferences.getBoolean(BUYER, false);
    }

    public void setSeller(boolean seller){
        editor.putBoolean(SELLER, seller);
        editor.commit();
    }

    public boolean getSeller(){
        return preferences.getBoolean(SELLER, false);
    }

    public String getName() {
        return preferences.getString(NAME, null);
    }

        public void setName(String name){
        editor.putString(NAME, name);
        editor.commit();
    }

    public String getToken() {
        return preferences.getString(TOKEN, null);
    }

    public void setToken(String token){
        editor.putString(TOKEN, token);
        editor.commit();
    }
    public String getRefreshToken() {
        return preferences.getString(TOKEN, null);
    }

    public void setRefreshToken(String refreshToken){
        editor.putString(TOKEN, refreshToken);
        editor.commit();
    }
    public String getTokenType() {
        return preferences.getString(TOKEN_TYPE, null);
    }

    public void setTokenType(String tokenType){
        editor.putString(TOKEN_TYPE, tokenType);
        editor.commit();
    }

    public String getRole() {
        return preferences.getString(ROLE, null);
    }

    public void setRole(String role){
        editor.putString(ROLE, role);
        editor.commit();
    }

    public String getPhone(){
        return preferences.getString(PHONE, null);
    }

    public void setPhone(String phone){
        editor.putString(PHONE, phone);
        editor.commit();
    }

    public String getUserId() {
        return preferences.getString(USER_ID, null);
    }

    public void setUserId(String userId){
        editor.putString(USER_ID, userId);
        editor.commit();
    }

    public String getActive(){
        return preferences.getString(ACTIVE, null);
    }

    public void setActive(String active){
        editor.putString(ACTIVE, active);
        editor.commit();
    }

    public String getActiveLocale(){
        return preferences.getString(ACTIVE_LOCALE, "eng");
    }

    public void setActiveLocale(String locale){
        editor.putString(ACTIVE_LOCALE, locale);
        editor.commit();
    }

    public String getDialCode(){
        return preferences.getString(DIAL_CODE, null);
    }

    public void setDialCode(String code){
        editor.putString(DIAL_CODE, code);
        editor.commit();
    }

    public String getImage(){
        return preferences.getString(IMAGE, "");
    }

    public void setImage(String image){
        editor.putString(IMAGE, image);
        editor.commit();
    }

    public void clearSession() {
        editor.clear();
        editor.commit();
    }
}
