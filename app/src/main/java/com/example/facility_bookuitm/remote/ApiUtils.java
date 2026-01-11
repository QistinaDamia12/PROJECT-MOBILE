package com.example.facility_bookuitm.remote;

public class ApiUtils
{
    // REST API server URL
    public static final String BASE_URL = "http://178.128.220.20/2024771469/api/";

    // return UserService instance
    public static UserService getUserService() {
        return RetrofitClient.getClient(BASE_URL).create(UserService.class);
    }

    // return BookService instance
    public static FacilityService getFacilityService() {
        return RetrofitClient.getClient(BASE_URL).create(FacilityService.class);
    }
}
