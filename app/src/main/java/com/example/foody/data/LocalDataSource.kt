package com.example.foody.data

import com.example.foody.data.database.RecipeDao
import com.example.foody.data.database.entities.FavouriteEntity
import com.example.foody.data.database.entities.FoodJokeEntity
import com.example.foody.data.database.entities.RecipeEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(private val RecipeDao :RecipeDao) {

     fun readRecipes() : Flow<List<RecipeEntity>>{
        return RecipeDao.readRecipe()
    }

     fun readFavouriteRecipe() :Flow<List<FavouriteEntity>>{
        return RecipeDao.readFavouriteRecipes()
    }

    fun readFoodJoke() : Flow<List<FoodJokeEntity>> {
        return RecipeDao.readFoodJoke()
    }

   suspend fun insertRecipe(recipeEntity: RecipeEntity){
        RecipeDao.insertRecipes(recipeEntity)
    }

    suspend fun insertFavouriteRecipe(favouriteEntity: FavouriteEntity){
        RecipeDao.insertFavouriteRecipe(favouriteEntity)
    }

    suspend fun insertFoodJoke (foodJokeEntity: FoodJokeEntity) {
        RecipeDao.insertFoodJoke(foodJokeEntity)
    }

    suspend fun deleteFavouriteRecipe(favouriteEntity: FavouriteEntity) {
        RecipeDao.deleteFavouriteRecipe(favouriteEntity)
    }

    suspend fun deleteAllFavouriteRecipes() {
        RecipeDao.deleteAllFavouriteRecipes()
    }
}