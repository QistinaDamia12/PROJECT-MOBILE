package com.example.facility_bookuitm.remote;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiUtils {
    // REST API server URL
    public static final String BASE_URL =  "http://178.128.220.20/2024771469/api/";

    private ApiUtils() {}

    public static ReservationService getReservationService() {
        return getRetrofit().create(ReservationService.class);
    }

    private static Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static UserService getUserService() {
        return RetrofitClient.getClient(BASE_URL).create(UserService.class);
    }

    public static FacilityService getFacilityService() {
        return RetrofitClient.getClient(BASE_URL).create(FacilityService.class);
    }

}
