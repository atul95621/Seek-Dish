package com.dish.seekdish.ui.home.view

import com.dish.seekdish.ui.home.dataModel.Location
import com.dish.seekdish.ui.home.dataModel.TasteFragDataClass
import retrofit2.Response

interface ITasteView {


    fun onGetLocation(result: Boolean, response: Response<Location>)

}