package com.dish.seekdish.ui.language


import com.dish.seekdish.ui.navDrawer.settings.dataModel.LanguageData
import com.dish.seekdish.ui.navDrawer.settings.dataModel.SaveLanguageModel
import retrofit2.Response

interface ILanguageView {


    fun onGetLanguageInfo(result: Boolean, response: Response<LanguageData>)

//    fun onSaveLanguageInfo(result: Boolean, response: Response<SaveLanguageModel>)

}