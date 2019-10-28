package com.dish.seekdish.ui.navDrawer.activities.view

import android.provider.ContactsContract
import com.dish.seekdish.ui.navDrawer.activities.model.ProfileDataClass
import com.dish.seekdish.ui.navDrawer.settings.dataModel.LanguageData
import retrofit2.Response

interface IMyInformationView {

    fun onSetDataChanged(result: Boolean, response: Response<ProfileDataClass>)

    fun onGetCountryInfo(result: Boolean, response: Response<LanguageData>)

    fun onGetCitiesInfo(result: Boolean, response: Response<LanguageData>)

    fun onGetProfileDetailsData(result: Boolean, response: Response<ProfileDataClass>)


}