package com.dish.seekdish.ui.login

import retrofit2.Response

interface ILoginView {

    fun onSetLoggedin(result: Boolean, response: Response<LoginDataClass>)
    fun checkUpdate(result: Boolean, response: Response<CheckUpdateModel>)

}