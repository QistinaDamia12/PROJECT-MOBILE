package com.example.facility_bookuitm.remote;

import com.example.facility_bookuitm.model.Facility;
import com.example.facility_bookuitm.model.DeleteResponse;
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

    @GET("facilities")
    Call<List<Facility>> getAllFacility(@Header("Authorization") String token);

    @FormUrlEncoded
    @POST("facilities")
    Call<Facility> addFacility(
            @Header("Authorization") String token,
            @Field("facilityName") String facilityName,
            @Field("facilityLocation") String facilityLocation,
            @Field("facilityPicture") String facilityPicture,
            @Field("facilityStatus") String facilityStatus,
            @Field("facilityType") String facilityType,
            @Field("facilityCapacity") int facilityCapacity
    );

    @DELETE("facilities/{facilityID}")
    Call<DeleteResponse> deleteFacility(
            @Header("Authorization") String token,
            @Path("facilityID") int facilityID
    );
}
