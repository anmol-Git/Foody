package com.example.foody.bindingadapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.foody.adapters.FavoriteRecipeAdapter
import com.example.foody.data.database.entities.FavouriteEntity

class FavoriteBindingAdapter {

    companion object {

        @BindingAdapter("viewVisibility", "setData",requireAll = false)
        @JvmStatic
        fun setDataAndViewVisibility(view : View, favoriteEntity : List<FavouriteEntity>?, mAdapter :FavoriteRecipeAdapter?) {
            when (view) {
                is RecyclerView -> {
                    val dataCheck = favoriteEntity.isNullOrEmpty()
                    view.isInvisible = dataCheck
                    if (!dataCheck) {
                        favoriteEntity?.let { mAdapter?.setData(it) }
                    }
                }
                else -> view.isVisible = favoriteEntity.isNullOrEmpty()
            }
        }
    }
}