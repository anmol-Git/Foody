package com.example.foody.model


import com.example.foody.model.Result
import com.google.gson.annotations.SerializedName

data class FoodRecipe(
    @SerializedName("results")
    val results: List<Result>,
)