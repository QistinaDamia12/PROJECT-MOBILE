package com.example.facility_bookuitm.remote;

import com.example.facility_bookuitm.model.DeleteResponse;
import com.example.facility_bookuitm.model.Facility;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface FacilityService {

    @GET("facility")
    Call<List<Facility>> getAllFacility(
            @Header("api-key") String apiKey
    );

    @GET("facility/{id}")
    Call<Facility> getFacility(
            @Header("api-key") String apiKey,
            @Path("id") int facilityID
    );

    @FormUrlEncoded
    @POST("facility")
    Call<Facility> addFacility(
            @Header("api-key") String apiKey,
            @Field("facilityName") String facilityName,
            @Field("facilityLocation") String facilityLocation,
            @Field("facilityPicture") String facilityPicture,
            @Field("facilityStatus") String facilityStatus,
            @Field("facilityType") String facilityType,
            @Field("facilityCapacity") int facilityCapacity
    );

    @DELETE("facility/{id}")
    Call<DeleteResponse> deleteFacility(
            @Header("api-key") String apiKey,
            @Path("id") int facilityID
    );
}

