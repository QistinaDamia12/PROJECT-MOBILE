package com.example.facility_bookuitm.remote;

import com.example.facility_bookuitm.model.Reservation;
import com.example.facility_bookuitm.model.DeleteResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ReservationService {

    @GET("reservation")
    Call<List<Reservation>> getAllReservations(@Header("api-key") String token);

    @GET("reservation/{reservationID}")
    Call<Reservation> getReservation(
            @Header("api-key") String token,
            @Path("reservationID") int reservationID
    );

    @FormUrlEncoded
    @POST("reservation")
    Call<Reservation> addReservation(
            @Header("api-key") String token,
            @Field("reserveDate") String reserveDate,
            @Field("reserveTime") String reserveTime,
            @Field("reservePurpose") String reservePurpose,
            @Field("facilityID") int facilityID,
            @Field("userID") int userID
    );

    @DELETE("reservation/{reservationID}")
    Call<DeleteResponse> deleteReservation(
            @Header("api-key") String token,
            @Path("reservationID") int reservationID
    );

    @PUT("reservation/{reservationID}")
    Call<Void> updateReservation(
            @Header("api-key") String token,
            @Path("reservationID") int reservationID,
            @Field("reserveDate") String reserveDate,
            @Field("reserveTime") String reserveTime,
            @Field("reservePurpose") String reservePurpose,
            @Field("facilityID") int facilityID,
            @Field("userID") int userID
    );
}




