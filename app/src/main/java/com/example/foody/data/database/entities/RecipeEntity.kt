package com.example.foody.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.foody.model.FoodRecipe
import com.example.foody.util.Constant.Companion.RECIPES_TABLE

@Entity(tableName = RECIPES_TABLE)
class RecipeEntity(var foodRecipe: FoodRecipe) {
        @PrimaryKey(autoGenerate = false)
        var id:Int=0
}