package com.dish.seekdish.ui.navDrawer.myFriends.contactFetch

class ContactModel {

    var name: String? = null
    var number: String? = null

    fun setNames(name: String) {
        this.name = name
    }

    fun getNumbers(): String {
        return number.toString()
    }

    fun setNumbers(number: String) {
        this.number = number
    }

    fun getNames(): String {
        return name.toString()
    }

}

data class ContactsDetailsModel(
    val `data`: ArrayList<Data>,
    val status: Int
)

data class Data(
    val country: Int,
    val email: String,
    val id: Int,
    val phone: String,
    val user_image: String,
    val username: String
)
