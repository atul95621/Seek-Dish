package com.dish.seekdish.ui.navDrawer.settings.view

import com.dish.seekdish.ui.navDrawer.settings.dataModel.SendUserGeneralSetting
import retrofit2.Response


interface IRadiusView {
    fun onSetSettingInfo(result: Boolean, response: Response<SendUserGeneralSetting>)
}