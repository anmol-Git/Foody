package com.example.foody.data

import com.example.foody.data.network.FoodRecipeApi
import com.example.foody.model.FoodJoke
import com.example.foody.model.FoodRecipe
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val foodRecipeApi: FoodRecipeApi) {

   suspend fun getRecipe(queries :Map<String, String>) : Response<FoodRecipe> {
       return foodRecipeApi.getRecipe(queries)
    }

    suspend fun searchRecipe(queries: Map<String, String>) :Response<FoodRecipe>{
        return foodRecipeApi.searchRecipe(queries)
    }

    suspend fun getFoodJoke(apiKey : String): Response<FoodJoke> {
        return foodRecipeApi.getFoodJoke(apiKey)
    }
}