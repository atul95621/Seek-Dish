package com.dish.seekdish.ui.home.dataModel

data class FilterDataModel(
    val `data`: Data,
    val status: Int
)

data class Data(
    val additional_services: List<AdditionalService>,
    val ambiance: List<Ambiance>,
    val ambiance_complementary: List<AmbianceComplementary>,
    val budget: List<Budget>,
    val intolerance_compatibilities: List<IntoleranceCompatibility>,
    val meal_types: List<MealType>,
    val seasons: List<Season>,
    val service_speed: List<ServiceSpeed>,
    val speciality: List<Speciality>
)

data class Speciality(
    val id: Int,
    val name: String
)

data class Budget(
    val id: Int,
    val name: String
)

data class Ambiance(
    val id: Int,
    val name: String
)

data class AmbianceComplementary(
    val id: Int,
    val name: String
)

data class ServiceSpeed(
    val id: Int,
    val name: String
)

data class AdditionalService(
    val id: Int,
    val name: String
)

data class Season(
    val id: Int,
    val name: String
)

data class MealType(
    val id: Int,
    val name: String
)

data class IntoleranceCompatibility(
    val id: Int,
    val name: String
)