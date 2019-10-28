package com.dish.seekdish.walkthrough.view

import com.dish.seekdish.ui.signup.SignUpModel
import retrofit2.Response

interface IRegisterFragView {

    fun onFacebookSiginDetails(result: Boolean, response: Response<SignUpModel>)
    fun onTwitterSiginDetails(result: Boolean, response: Response<SignUpModel>)

}