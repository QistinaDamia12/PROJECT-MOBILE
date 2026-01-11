package com.example.facility_bookuitm.remote;

public class ApiUtils {
    public static final String BASE_URL= "https://aptitude.my/2025966025/api/";

    public static UserService getUserService() {
        return RetrofitClient.getClient(BASE_URL).create(UserService.class);
    }
}
