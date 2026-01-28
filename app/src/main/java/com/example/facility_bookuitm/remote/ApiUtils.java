package com.example.facility_bookuitm.remote;

public class ApiUtils
{
    // REST API server URL
    public static final String BASE_URL =  "http://aptitude.my/2025138587/api/";

    // return UserService instance
    public static UserService getUserService() {
        return RetrofitClient.getClient(BASE_URL).create(UserService.class);
    }

    // return FacilityService instance
    public static FacilityService getFacilityService() {
        return RetrofitClient.getClient(BASE_URL).create(FacilityService.class);
    }

    // return FacilityService instance
    public static ReservationService getReservationService() {
        return RetrofitClient.getClient(BASE_URL).create(ReservationService.class);
    }
}
