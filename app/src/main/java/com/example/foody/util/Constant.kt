package com.example.foody.util

class Constant {
    companion object{
        const val API_KEY = "b7da859f43be43d69ac8556ca8825df1"
        const val BASE_URL = "https://api.spoonacular.com"
        const val BASE_IMAGE_URL = "https://spoonacular.com/cdn/ingredients_100x100/"

        //API Query key
        const val QUERY_SEARCH="query"
        const val QUERY_NUMBER="number"
        const val QUERY_API_KEY="apiKey"
        const val QUERY_TYPE="type"
        const val QUERY_DIET="diet"
        const val QUERY_ADD_RECIPE_INFORMATION="addRecipeInformation"
        const val QUERY_FILL_INGREDIENT="fillIngredients"

        //Room database
        const val DATABASE_NAME="recipes_database"
        const val RECIPES_TABLE="recipes_table"
        const val FAVOURITE_RECIPES_TABLE="favourite_recipes_table"
        const val FOOD_JOKE_TABLE = "food_joke_table"

        //Bottom Sheet and Preferences
        const val DEFAULT_RECIPE_NUMBER = "90"
        const val DEFAULT_MEAL_TYPE = "main course"
        const val DEFAULT_DIET_TYPE="gluten free"
        const val  MEAL_TYPE="mealType"
        const val  DIET_TYPE="dietType"
        const val  MEAL_TYPE_ID="mealTypeId"
        const val  DIET_TYPE_ID="DietTypeId"
        const val PREFRENCES_NAME="foody prefrences"
        const val PREFRENCES_BACK_ONLINE="backOnline"


    }
}