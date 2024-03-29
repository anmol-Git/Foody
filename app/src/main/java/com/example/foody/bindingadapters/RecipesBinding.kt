package com.example.foody.bindingadapters

import android.media.Image
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.example.foody.data.database.entities.RecipeEntity
import com.example.foody.model.FoodRecipe
import com.example.foody.util.NetworkResult

class RecipesBinding {

    companion object{

        @BindingAdapter("readApiResponse", "readDatabase", requireAll = true)
        @JvmStatic
        fun handleReadDataError(
            view: View,
            apiResponse: NetworkResult<FoodRecipe>?,
            database: List<RecipeEntity>?
        ) {
            when (view) {
                is ImageView -> {
                    view.isVisible = apiResponse is NetworkResult.Error && database.isNullOrEmpty()
                }
                is TextView -> {
                    view.isVisible = apiResponse is NetworkResult.Error && database.isNullOrEmpty()
                    view.text = apiResponse?.message.toString()
                }
            }
        }
    }
}