package com.rctapp.utils;

public class Api {
   // public static final String main_url = "http://142.93.210.105:1122";
    public static final String main_url = "https://rctapp.co.tz:1122";
    public static final String platforms = "/api/v1/platform/offset/0";
    public static final String sellers = "/api/v1/seller/offset/0";
    public static final String get_seller_by_platform_id = "/api/v1/seller/platform/";
    public static String get_seller_by_platform(String platformsId){
        return main_url+get_seller_by_platform_id+platformsId+"/offset/0";
    }
    public static final String country = "/api/v1/country";
    public static final String createOtp = "/api/v1/user/createotp";
    public static final String signin = "/api/v1/user/signin";
    public static final String verify= "api/v1/user/verifyotp";
    public static final String generateToken= "/api/v1/session/generatetoken/";
    public static final String complete_registration= "/api/v1/user/complete";
    public static final String get_user_information_by_token= "/api/v1/user/information";
    public static final String get_seller_tender = "/api/v1/buyer/give-tender";
    public static final String get_seller_offer_tender = "/api/v1/seller/tender/offset/0";
    public static final String get_all_quotes_buyer = "/api/v1/quote/buyer";
    public static final String get_recent_tender = "/api/v1/seller/tender-given";
    public static final String get_send_quote = "/api/v1/quote/tender/";
    public static final String seller_request_tender = "/api/v1/seller/tender";
    public static final String get_all_messages = "/api/v1/messenger";
    public static final String decline_tender = "/api/v1/buyer/tender/decline/";
    public static final String upload_profile = "/api/v1/service/file-manager/upload/tag/dp/reference-id/";
    public static final String accept_seller_tender = "/api/v1/seller/tender/accept/";
    public static final String accept_tender = "/api/v1/buyer/tender/accept/";
    public static final String decline_seller_tender = "/api/v1/seller/tender/decline/";
    public static final String delete_seller_tender = "/api/v1/seller/tender/";




}
