package com.example.foody.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.lifecycle.*
import com.example.foody.data.Repository
import com.example.foody.data.database.entities.FavouriteEntity
import com.example.foody.data.database.entities.FoodJokeEntity
import com.example.foody.data.database.entities.RecipeEntity
import com.example.foody.model.FoodJoke
import com.example.foody.model.FoodRecipe
import com.example.foody.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository,
    application: Application
) : AndroidViewModel(application) {

    /**ROOM DATABASE*/
    val readRecipe: LiveData<List<RecipeEntity>> = repository.local.readRecipes().asLiveData()
    val readFavouriteRecipe: LiveData<List<FavouriteEntity>> =
        repository.local.readFavouriteRecipe().asLiveData()
    val readFoodJoke: LiveData<List<FoodJokeEntity>> = repository.local.readFoodJoke().asLiveData()

    //fun to insert recipe in database
    private fun insertRecipe(recipeEntity: RecipeEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertRecipe(recipeEntity)
        }

     fun insertFavoriteRecipe(favouriteEntity: FavouriteEntity){
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertFavouriteRecipe(favouriteEntity)
        }
    }

    fun insertFoodJoke(foodJokeEntity: FoodJokeEntity){
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertFoodJoke(foodJokeEntity)
        }
    }

     fun deleteFavoriteRecipe(favouriteEntity: FavouriteEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.deleteFavouriteRecipe(favouriteEntity)
        }
    }

     fun deleteAllFavoriteRecipe() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.deleteAllFavouriteRecipes()
        }
    }

    /**RETROFIT*/
    var recipeResponse :MutableLiveData<NetworkResult<FoodRecipe>> = MutableLiveData()
    var searchResponse :MutableLiveData<NetworkResult<FoodRecipe>> = MutableLiveData()
    var foodJokeResponse : MutableLiveData<NetworkResult<FoodJoke>> = MutableLiveData()

    //driver fun to get recipe from API
    fun getRecipe(queries : Map<String , String>) =viewModelScope.launch {
        getRecipeSafeCall(queries)
    }

    fun searchRecipe(searchQueries: Map<String, String>)=viewModelScope.launch {
        searchQuerySafeCall(searchQueries)
    }

    fun getFoodJoke(apiKey : String) =viewModelScope.launch {
        getFoodJokeSafeCall(apiKey)
    }




    private suspend fun getRecipeSafeCall(queries: Map<String, String>) {
        recipeResponse.value=NetworkResult.Loading()
        if (hasInternetConnection()){
            try {
                val response = repository.remote.getRecipe(queries)
                recipeResponse.value = handleFoodRecipeResponse(response)

                val foodRecipe =recipeResponse.value!!.data
                if (foodRecipe!=null){
                    offlineCacheRecipes(foodRecipe)
                }
            }catch ( e: Exception){
                recipeResponse.value =NetworkResult.Error("Recipe not found.")
                e.printStackTrace()
            }
        }else{
            recipeResponse.value=NetworkResult.Error("No Internet Connection")
        }
    }
    private suspend fun searchQuerySafeCall(searchQueries: Map<String, String>) {
        searchResponse.value=NetworkResult.Loading()
        if (hasInternetConnection()){
            try {
                val response = repository.remote.searchRecipe(searchQueries)
                searchResponse.value = handleFoodRecipeResponse(response)
            }catch ( e: Exception){
                searchResponse.value = NetworkResult.Error("Recipe not found error.")
            }
        }else{
            searchResponse.value=NetworkResult.Error("No Internet Connection")
        }
    }

    private suspend fun getFoodJokeSafeCall(apiKey: String) {
        foodJokeResponse.value=NetworkResult.Loading()
        if (hasInternetConnection()){
            try {
                val response = repository.remote.getFoodJoke(apiKey)
                foodJokeResponse.value = handleFoodJokeeResponse(response)

                val foodJoke =foodJokeResponse.value!!.data
                if (foodJoke!= null){
                    offlineCacheFoodJoke(foodJoke)
                }
            }catch ( e: Exception){
                Log.d("in food joke now", "error")
                foodJokeResponse.value =NetworkResult.Error("Joke not found.")
                e.printStackTrace()
            }
        }else{
            foodJokeResponse.value=NetworkResult.Error("No Internet Connection")
        }
    }


    private fun offlineCacheRecipes(foodRecipe: FoodRecipe) {
                      val recipeEntity = RecipeEntity(foodRecipe)
                      insertRecipe(recipeEntity)
    }

    private fun offlineCacheFoodJoke(foodJoke: FoodJoke) {
        val foodJokeEntity = FoodJokeEntity(foodJoke)
      insertFoodJoke(foodJokeEntity)
    }


    private fun handleFoodRecipeResponse(response: Response<FoodRecipe>): NetworkResult<FoodRecipe>? {
       when {
           response.message().toString().contains("timeout") -> {
               return NetworkResult.Error("timeout")
           }
           response.code()==402 ->{
               return NetworkResult.Error("API key Limited.")
           }
           response.body()!!.results.isNullOrEmpty() -> {
               return  NetworkResult.Error("Recipe not found.")
           }
           response.isSuccessful -> {
               val foodRecipe =response.body()
               return NetworkResult.Success(foodRecipe!!)
           }
           else -> {
               return NetworkResult.Error(response.message())
           }
       }
    }
    private fun handleFoodJokeeResponse(response: Response<FoodJoke>): NetworkResult<FoodJoke>? {
        when {
            response.message().toString().contains("timeout") -> {
                return NetworkResult.Error("timeout")
            }
            response.code()==402 ->{
                return NetworkResult.Error("API key Limited.")
            }

            response.isSuccessful -> {
                val FoofJoke =response.body()
                return NetworkResult.Success(FoofJoke!!)
            }
            else -> {
                return NetworkResult.Error(response.message())
            }
        }
    }



    private fun hasInternetConnection() :Boolean {
              val connectivityManager=getApplication<Application>().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
              val activeNetwork=connectivityManager.activeNetwork ?: return false
              val capabilities=connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false

              return when {
                  capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                  capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                  capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                  else -> false
              }
          }
}