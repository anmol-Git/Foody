package com.example.foody.data.database

import androidx.room.TypeConverter
import com.example.foody.model.FoodRecipe
import com.example.foody.model.Result
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class RecipesTypeConvertor {

    var gson=Gson()

    @TypeConverter
    fun FoodRecipeToString(foodRecipe: FoodRecipe) : String{
        return gson.toJson(foodRecipe)
    }


    @TypeConverter
    fun StringToFoodRecipe(data :String) :FoodRecipe{
        val listType= object : TypeToken<FoodRecipe>() {}.type
        return gson.fromJson(data,listType)
    }

    @TypeConverter
    fun resultToString(result: Result) : String {
        return gson.toJson(result)
    }

    @TypeConverter
    fun StringToResult(data: String) : Result {
        val listType= object : TypeToken<Result>() {}.type
         return gson.fromJson(data,listType)
    }
}