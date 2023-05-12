package sdumchykov.androidApp.domain.api

import retrofit2.Response
import retrofit2.http.*
import sdumchykov.androidApp.domain.model.Data
import sdumchykov.androidApp.domain.model.ServerResponse
import sdumchykov.androidApp.domain.model.authorizeUser.AuthorizationData
import sdumchykov.androidApp.domain.model.contacts.Contacts
import sdumchykov.androidApp.domain.model.register.RegisterData
import sdumchykov.androidApp.domain.model.requestModels.AuthorizeModel
import sdumchykov.androidApp.domain.model.requestModels.ContactIdModel
import sdumchykov.androidApp.domain.model.requestModels.EditProfileResponseData
import sdumchykov.androidApp.domain.model.requestModels.EditProfileUser

interface ServerApi {
    @FormUrlEncoded
    @POST("users")
    suspend fun registerUser(
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<ServerResponse<RegisterData>>

    @PUT("users/{userID}")
    @Headers("Content-type: application/json")
    suspend fun editUser(
        @Path("userID") userId: Int,
        @Header("Authorization") token: String,
        @Body body: EditProfileUser
    ): Response<ServerResponse<EditProfileResponseData>>

    @POST("login")
    @Headers("Content-type: application/json")
    suspend fun authorizeUser(
        @Body body: AuthorizeModel
    ): Response<ServerResponse<AuthorizationData>>

    @PUT("users/{userID}/contacts")
    @Headers("Content-type: application/json")
    suspend fun addContact(
        @Path("userID") userId: Int,
        @Header("Authorization") token: String,
        @Body() contactId: ContactIdModel
    ): Response<ServerResponse<Data>>

    @GET("users/{userID}/contacts")
    suspend fun getAccountUsers(
        @Path("userID") userId: Int,
        @Header("Authorization") token: String
    ): Response<ServerResponse<Contacts>>

    @DELETE("users/{userId}/contacts/{contactId}")
    suspend fun deleteContact(
        @Path("userId") userId: Int,
        @Path("contactId") contactId: Int,
        @Header("Authorization") token: String
    ): Response<ServerResponse<Contacts>>

    @GET("users")
    suspend fun getUsers(@Header("Authorization") token: String): Response<ServerResponse<Data>>

}