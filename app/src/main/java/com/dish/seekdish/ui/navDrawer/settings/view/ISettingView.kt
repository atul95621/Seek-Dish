package com.dish.seekdish.ui.navDrawer.settings.view

import com.dish.seekdish.ui.navDrawer.settings.dataModel.LanguageData
import com.dish.seekdish.ui.navDrawer.settings.dataModel.SaveLanguageModel
import com.dish.seekdish.ui.navDrawer.settings.dataModel.SendUserGeneralSetting
import com.dish.seekdish.ui.navDrawer.settings.dataModel.SettingDataClass
import retrofit2.Response


interface ISettingView {

    fun onGetSettingInfo(result: Boolean, response: Response<SettingDataClass>)

    fun onSetSettingInfo(result: Boolean, response: Response<SendUserGeneralSetting>)

    fun onGetLanguageInfo(result: Boolean, response: Response<LanguageData>)

    fun onSaveLanguageInfo(result: Boolean, response: Response<SaveLanguageModel>)

}