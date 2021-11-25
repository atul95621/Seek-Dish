package com.dish.seekdish.walkthrough.view

import com.dish.seekdish.ui.login.CheckUpdateModel
import retrofit2.Response

interface IWalkThrough {
    fun checkUpdate(result: Boolean, response: Response<CheckUpdateModel>)
}