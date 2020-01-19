package com.dish.seekdish.retrofit


import com.dish.seekdish.ui.home.dataModel.*
import com.dish.seekdish.ui.login.LoginDataClass
import com.dish.seekdish.ui.navDrawer.activities.model.ContactModel
import com.dish.seekdish.ui.navDrawer.activities.model.ProfileDataClass
import com.dish.seekdish.ui.navDrawer.checkin.data.CheckinModel
import com.dish.seekdish.ui.navDrawer.dishDescription.model.AddTodoModel
import com.dish.seekdish.ui.navDrawer.dishDescription.model.DishDescpModel
import com.dish.seekdish.ui.navDrawer.myFriends.contactFetch.ContactsDetailsModel
import com.dish.seekdish.ui.navDrawer.myFriends.dataModel.FriendDataModel
import com.dish.seekdish.ui.navDrawer.restaurantDiscription.RestroDescpModel
import com.dish.seekdish.ui.navDrawer.restaurants.dataClass.ProximityDataClass
import com.dish.seekdish.ui.navDrawer.restaurants.dataClass.RestroMapModel
import com.dish.seekdish.ui.navDrawer.restaurants.dataClass.TimeRestroDataClass
import com.dish.seekdish.ui.navDrawer.settings.dataModel.*
import com.dish.seekdish.ui.navDrawer.settings.myAlerts.InvitationModel
import com.dish.seekdish.ui.navDrawer.settings.myAlerts.MyAlertDataClass
import com.dish.seekdish.ui.navDrawer.toDo.list.DeleteTodoList
import com.dish.seekdish.ui.navDrawer.toDo.list.ListTodoDataClass
import com.dish.seekdish.ui.signup.SignUpModel
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.Response
import retrofit2.Call
import retrofit2.http.*
import retrofit2.http.GET


/**
 * Created by anupamchugh on 09/01/17.
 */

internal interface APIInterface {

    // with image
    @Multipart
    @POST("register")
    fun doSignUp(
        @Part("email") email: RequestBody,
        @Part("password") password: RequestBody,
        @Part("c_password") c_password: RequestBody,
        @Part("first_name") first_name: RequestBody,
        @Part("last_name") last_name: RequestBody,
        @Part("phone") phone: RequestBody,
        @Part("username") username: RequestBody,
        @Part("gender") gender: RequestBody,
        @Part("bio") bio: RequestBody,
        @Part("country") country_code: RequestBody,
        @Part("fcm_token") fcm_token: RequestBody,
        @Part file: MultipartBody.Part
    ): Call<SignUpModel>


    // without image
    @Multipart
    @POST("register")
    fun doSignUpWithoutImage(
        @Part("email") email: RequestBody,
        @Part("password") password: RequestBody,
        @Part("c_password") c_password: RequestBody,
        @Part("first_name") first_name: RequestBody,
        @Part("last_name") last_name: RequestBody,
        @Part("phone") phone: RequestBody,
        @Part("username") username: RequestBody,
        @Part("gender") gender: RequestBody,
        @Part("bio") bio: RequestBody,
        @Part("country") country_code: RequestBody,
        @Part("fcm_token") fcm_token: RequestBody
    ): Call<SignUpModel>


    @FormUrlEncoded
    @POST("facebook_login")
    fun doFacebookSignup(
        @Field("email") email: String,
        @Field("first_name") first_name: String,
        @Field("last_name") last_name: String,
        @Field("photo") photoUrl: String,
        @Field("fcm_token") fcm_token: String,
        @Field("provider_id") facebook_id: String

    ): Call<SignUpModel>


    @FormUrlEncoded
    @POST("twitter_login")
    fun doTwitterSignup(
        @Field("email") email: String,
        @Field("first_name") first_name: String,
        @Field("photo") photoUrl: String,
        @Field("fcm_token") fcm_token: String,
        @Field("provider_id") twitter_id: String

    ): Call<SignUpModel>

    @FormUrlEncoded
    @POST("login")
    fun doLogIn(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginDataClass>


    @POST("create_token")
    @FormUrlEncoded
    fun forgotPassword(
        @Field("email") email: String
    ): Call<JsonObject>


    @POST("reset_password")
    @FormUrlEncoded
    fun resetPassword(
        @Field("email") email: String,
        @Field("token") tokenPassword: String,
        @Field("password") password: String,
        @Field("c_password") confirmPassword: String
    ): Call<JsonObject>

    @FormUrlEncoded
    @POST("location_cords")
    fun getLocation(
        @Field("user_id") userId: String,
        @Field("longitude") longitude: String,
        @Field("latitude") latitude: String

    ): Call<Location>


    @FormUrlEncoded
    @POST("get_user_settings")
    fun getGeneralSetting(
        @Field("user_id") userId: String

    ): Call<SettingDataClass>

    @FormUrlEncoded
    @POST("set_user_settings")
    fun setGeneralSetting(
        @Field("user_id") userId: String,
        @Field("geolocation") geolocation: String,
        @Field("push_notification") push_notification: String,
        @Field("private") private: String,
        @Field("radius") radius: String

    ): Call<SendUserGeneralSetting>

    @FormUrlEncoded
    @POST("get_all_langs")
    fun getLanguagesData(
        @Field("user_id") userId: String

    ): Call<LanguageData>


    @FormUrlEncoded
    @POST("save_language")
    fun saveLanguagesData(
        @Field("user_id") userId: String,
        @Field("language_id") languageId: String
    ): Call<SaveLanguageModel>

    @FormUrlEncoded
    @POST("get_all_countries")
    fun getCountriesData(
        @Field("user_id") userId: String

    ): Call<LanguageData>

    @FormUrlEncoded
    @POST("get_cities")
    fun getCitiesData(
        @Field("user_id") userId: String,
        @Field("country_id") countryId: String
    ): Call<LanguageData>


    @FormUrlEncoded
    @POST("get_profile")
    fun getProfileData(
        @Field("user_id") userId: String
    ): Call<ProfileDataClass>


    @Multipart
    @POST("update_profile")
    fun doUpdateProfileDetails(

        @Part("first_name") first_name: RequestBody,
        @Part("last_name") last_name: RequestBody,
        @Part("username") username: RequestBody,
        @Part("gender") gender: RequestBody,
        @Part("address_line1") addressLineOne: RequestBody,
        @Part("address_line2") addressLineTwo: RequestBody,
        @Part("bio") bio: RequestBody,
        @Part("city") city: RequestBody,
        @Part("country") country: RequestBody,
        @Part("zip_code") zip_code: RequestBody,
        @Part("body_fat") body_fat: RequestBody,
        @Part("weight") weight: RequestBody,
        @Part("height") height: RequestBody,
        @Part("user_id") user_id: RequestBody,
        @Part file: MultipartBody.Part
    ): Call<ProfileDataClass>


    @FormUrlEncoded
    @POST("get_liked_ingredients")
    fun getLikedIngredients(
        @Field("user_id") userId: String,
        @Field("page_no") pageNo: String,
        @Field("per_page") perPage: String

    ): Call<LikeDataClass>


    @FormUrlEncoded
    @POST("search_liked_ingredients")
    fun getSearchLikedIngredients(
        @Field("user_id") userId: String,
        @Field("page_no") pageNo: String,
        @Field("per_page") perPage: String,
        @Field("search_term") searchTerm: String


    ): Call<LikeDataClass>

    @FormUrlEncoded
    @POST("save_liked_ingredients")
    fun postSavedLikeIngre(
        @Field("user_id") userId: String,
        @Field("ingredients") ingredients: String

    ): Call<LikedIngredientsSaved>

    @FormUrlEncoded
    @POST("get_disliked_ingredients")
    fun getDisLikedIngredients(
        @Field("user_id") userId: String,
        @Field("page_no") pageNo: String,
        @Field("per_page") perPage: String

    ): Call<DisLikeDataClass>


    @FormUrlEncoded
    @POST("save_disliked_ingredients")
    fun postSavedDisLikeIngre(
        @Field("user_id") userId: String,
        @Field("ingredients") ingredients: String

    ): Call<LikedIngredientsSaved>


    @FormUrlEncoded
    @POST("search_disliked_ingredients")
    fun getSearchDisLikedIngredients(
        @Field("user_id") userId: String,
        @Field("page_no") pageNo: String,
        @Field("per_page") perPage: String,
        @Field("search_term") searchTerm: String

    ): Call<DisLikeDataClass>


    @FormUrlEncoded
    @POST("get_filters")
    fun getFilterData(
        @Field("user_id") userId: String,
        @Field("language_id") languageId: String

    ): Call<FilterDataModel>


    @FormUrlEncoded
    @POST("home_meals")
    fun getTasteMealData(
        @Field("user_id") userId: String,
        @Field("latitude") latitude: String,
        @Field("longitude") longitude: String,
        @Field("type") type: String,
        @Field("radius") radius: String,
        @Field("device_token") device_token: String,
        @Field("device_type") device_type: String,
        @Field("page_no") page_no: String,
        @Field("per_page") per_page: String

    ): Call<TasteFragDataClass>


    @FormUrlEncoded
    @POST("home_meals_time")
    fun getTimeMeal(
        @Field("user_id") userId: String,
        @Field("latitude") latitude: String,
        @Field("longitude") longitude: String,
        @Field("type") type: String,
        @Field("radius") radius: String,
        @Field("device_token") device_token: String,
        @Field("device_type") device_type: String,
        @Field("page_no") page_no: String,
        @Field("per_page") per_page: String

    ): Call<TimeFragDataClass>


    @FormUrlEncoded
    @POST("home_meals_map")
    fun getHomeMapData(
        @Field("user_id") userId: String,
        @Field("latitude") latitude: String,
        @Field("longitude") longitude: String,
        @Field("type") type: String,
        @Field("radius") radius: String,
        @Field("device_token") device_token: String,
        @Field("device_type") device_type: String,
        @Field("page_no") page_no: String,
        @Field("per_page") per_page: String

    ): Call<MapHomeModel>


    @FormUrlEncoded
    @POST("home_restaurants")
    fun getProxiRest(
        @Field("user_id") userId: String,
        @Field("latitude") latitude: String,
        @Field("longitude") longitude: String,
        @Field("type") type: String,
        @Field("radius") radius: String,
        @Field("device_token") device_token: String,
        @Field("device_type") device_type: String,
        @Field("page_no") page_no: String,
        @Field("per_page") per_page: String

    ): Call<ProximityDataClass>


    @FormUrlEncoded
    @POST("home_restaurants_time")
    fun getTimeRest(
        @Field("user_id") userId: String,
        @Field("latitude") latitude: String,
        @Field("longitude") longitude: String,
        @Field("type") type: String,
        @Field("radius") radius: String,
        @Field("device_token") device_token: String,
        @Field("device_type") device_type: String,
        @Field("page_no") page_no: String,
        @Field("per_page") per_page: String

    ): Call<TimeRestroDataClass>


    @FormUrlEncoded
    @POST("home_restaurants_map")
    fun getRestroMapData(
        @Field("user_id") userId: String,
        @Field("latitude") latitude: String,
        @Field("longitude") longitude: String,
        @Field("type") type: String,
        @Field("radius") radius: String,
        @Field("device_token") device_token: String,
        @Field("device_type") device_type: String,
        @Field("page_no") page_no: String,
        @Field("per_page") per_page: String

    ): Call<RestroMapModel>


    @FormUrlEncoded
    @POST("meal_detail")
    fun getMealDetails(
        @Field("user_id") userId: String,
        @Field("meal_id") meal_id: String,
        @Field("restaurant_id") restaurant_id: String,
        @Field("longitude") longitude: String,
        @Field("latitude") latitude: String
    ): Call<DishDescpModel>


    @FormUrlEncoded
    @POST("add_todo_list")
    fun addtoTodo(
        @Field("user_id") userId: String,
        @Field("meal_id") meal_id: String,
        @Field("restaurant_id") restaurant_id: String

    ): Call<AddTodoModel>


    @FormUrlEncoded
    @POST("add_favorite_list")
    fun addFavorite(
        @Field("user_id") userId: String,
        @Field("meal_id") meal_id: String,
        @Field("restaurant_id") restaurant_id: String

    ): Call<AddTodoModel>


    @FormUrlEncoded
    @POST("todo_maps_list")
    fun getTodoList(
        @Field("user_id") userId: String
    ): Call<ListTodoDataClass>

    @FormUrlEncoded
    @POST("delete_todo_list")
    fun deleteTodo(
        @Field("user_id") userId: String,
        @Field("meal_id") meal_id: String,
        @Field("restaurant_id") restaurant_id: String

    ): Call<DeleteTodoList>


    @FormUrlEncoded
    @POST("favorite_maps_list")
    fun getFavoriteList(
        @Field("user_id") userId: String
    ): Call<ListTodoDataClass>

    @FormUrlEncoded
    @POST("delete_favorite_list")
    fun deleteFavorite(
        @Field("user_id") userId: String,
        @Field("meal_id") meal_id: String,
        @Field("restaurant_id") restaurant_id: String

    ): Call<DeleteTodoList>

    @Multipart
    @POST("post_comment")
    fun doPostComment(
        @Part("user_id") user_id: RequestBody,
        @Part("meal_id") meal_id: RequestBody,
        @Part("taste_rating") taste_rating: RequestBody,
        @Part("presentation_rating") presentation_rating: RequestBody,
        @Part("texture_rating") texture_rating: RequestBody,
        @Part("odor_rating") odor_rating: RequestBody,
        @Part("comment") comment: RequestBody,
        @Part("anonymous") anonymous: RequestBody,
        @Part("restaurant_id") restaurant_id: RequestBody,
        @Part("service_rating") service_rating: RequestBody,
        @Part("decore_rating") decore_rating: RequestBody,
        @Part("cleanness_rating") cleanness_rating: RequestBody,
        @Part("ambiance_rating") ambiance_rating: RequestBody,
        @Part image1: MultipartBody.Part,
        @Part image2: MultipartBody.Part
    ): Call<AddTodoModel>


    @FormUrlEncoded
    @POST("logout")
    fun logout(
        @Field("user_id") userId: String
    ): Call<AddTodoModel>

    @FormUrlEncoded
    @POST("send_friend_request")
    fun sendFriendRequest(
        @Field("sender_id") senderId: String,
        @Field("receiver_id") receiverId: String
    ): Call<AddTodoModel>

    @FormUrlEncoded
    @POST("following_friend_request")
    fun sendFollowingRequest(
        @Field("sender_id") senderId: String,
        @Field("receiver_id") receiverId: String
    ): Call<AddTodoModel>

    @FormUrlEncoded
    @POST("restaurant_detail")
    fun getRestroDetails(
        @Field("user_id") userId: String,
        @Field("restaurant_id") restaurant_id: String

    ): Call<RestroDescpModel>

    @FormUrlEncoded
    @POST("all_received_friend_request")
    fun recievedReq(
        @Field("user_id") userId: String
    ): Call<ReceivedRequestDataClass>


    @FormUrlEncoded
    @POST("cancel_friend_request_sent")
    fun cancelReqSent(
        @Field("sender_id") senderId: String,
        @Field("receiver_id") receiverId: String
    ): Call<CancelReModel>


    @FormUrlEncoded
    @POST("save_filters")
    fun saveFilterData(
        @Field("user_id") userId: String,
        @Field("budget") latitude: String,
        @Field("service_speed") serviceSpeed: String,
        @Field("meal_type") mealType: String,
        @Field("compatibility_intolerance") comIntol: String,
        @Field("specialities") special: String,
        @Field("ambiance") ambiance: String,
        @Field("ambiance_complementary") amb_comp: String,
        @Field("additional_services") addServi: String,
        @Field("seasonality") season: String,
        @Field("consider_my_profile") consider_my_profile: String
    ): Call<SaveFilterModel>

    @FormUrlEncoded
    @POST("my_friends")
    fun getFriendList(
        @Field("user_id") userId: String
    ): Call<FriendDataModel>

    @FormUrlEncoded
    @POST("delete_friend")
    fun doDeleteFriend(
        @Field("user_id") userId: String,
        @Field("friend_id") friendId: String
    ): Call<CancelReModel>

    @FormUrlEncoded
    @POST("cancel_following_request")
    fun doDeleteFollower(
        @Field("sender_id") userId: String,
        @Field("receiver_id") friendId: String
    ): Call<CancelReModel>

    @FormUrlEncoded
    @POST("accept_friend_request")
    fun doAcceptReq(
        @Field("sender_id") userId: String,
        @Field("receiver_id") friendId: String
    ): Call<CancelReModel>

    @FormUrlEncoded
    @POST("get_checkin_list")
    fun getCheckinData(
        @Field("user_id") userId: String
    ): Call<CheckinModel>

    @FormUrlEncoded
    @POST("add_alert")
    fun doAddAlert(
        @Field("user_id") userId: String,
        @Field("restaurant_id") restaurant_id: String
    ): Call<CancelReModel>

    @FormUrlEncoded
    @POST("my_alert_list")
    fun getAlertListData(
        @Field("user_id") userId: String
    ): Call<MyAlertDataClass>

    @FormUrlEncoded
    @POST("remove_alert")
    fun getAlertDlete(
        @Field("user_id") userId: String,
        @Field("restaurant_id") restaurant_id: String

    ): Call<CancelReModel>

    @FormUrlEncoded
    @POST("get_contact_details")
    fun getContactDeatails(
        @Field("user_id") userId: String
    ): Call<ContactModel>

    @FormUrlEncoded
    @POST("post_contact_details")
    fun postContactDeatails(
        @Field("user_id") userId: String,
        @Field("email") email: String,
        @Field("country_code") country: String,
        @Field("phone") phone: String,
        @Field("skype_id") skype: String,
        @Field("facebook_id") facbook: String,
        @Field("twitter_id") twitter: String
    ): Call<CancelReModel>

    @FormUrlEncoded
    @POST("get_all_invitations")
    fun getInvitation(
        @Field("user_id") userId: String,
        @Field("restro_id") restaurant_id: String

    ): Call<InvitationModel>


    @FormUrlEncoded
    @POST("get_all_users_details")
    fun getAllUsersDetails(
        @Field("user_id") userId: String
    ): Call<ContactsDetailsModel>

    @FormUrlEncoded
    @POST("save_invitation_setting")
    fun getInvitationSetting(
        @Field("user_id") userId: String,
        @Field("allow_invitation") restaurant: String,
        @Field("validity_of_invitation") validity: String

    ): Call<CancelReModel>

    @FormUrlEncoded
    @POST("post_invitation")
    fun postInvitationApi(
        @Field("sender_id") sender_id: String,
        @Field("restro_id") restro_id: String,
        @Field("receiver_id") receiver_id: String,
        @Field("invitation_schedule_time") time: String,
        @Field("invitation_schedule_date") date: String

    ): Call<CancelReModel>


}