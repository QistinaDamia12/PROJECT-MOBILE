package com.example.facility_bookuitm.remote;

import com.example.facility_bookuitm.model.Facility;
import com.example.facility_bookuitm.model.DeleteResponse;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
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

    @FormUrlEncoded
    @PUT("facilities/{facilityID}")
    Call<Facility> updateFacility(
            @Header("Authorization") String token,
            @Path("facilityID") int facilityID,
            @Field("facilityName") String facilityName,
            @Field("facilityLocation") String facilityLocation,
            @Field("facilityPicture") String facilityPicture,
            @Field("facilityStatus") String facilityStatus,
            @Field("facilityType") String facilityType,
            @Field("facilityCapacity") int facilityCapacity
    );


}
