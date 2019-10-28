package com.dish.seekdish.ui.signup

import retrofit2.Response

interface ISignUpView {

     fun onSetSignedUp(result: Boolean, response: Response<SignUpModel>)
}