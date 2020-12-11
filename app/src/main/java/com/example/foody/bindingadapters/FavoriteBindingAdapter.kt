package com.example.foody.bindingadapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.foody.adapters.FavoriteRecipeAdapter
import com.example.foody.data.database.entities.FavouriteEntity

class FavoriteBindingAdapter {

    companion object {

        @BindingAdapter("viewVisibility", "setData",requireAll = false)
        @JvmStatic
        fun setDataAndViewVisibility(view : View, favoriteEntity : List<FavouriteEntity>?, mAdapter :FavoriteRecipeAdapter?) {
            if (favoriteEntity.isNullOrEmpty()){
                when(view){
                    is ImageView -> {
                        view.visibility=View.VISIBLE
                    }
                    is TextView -> {
                        view.visibility=View.VISIBLE
                    }
                    is RecyclerView -> {
                        view.visibility=View.INVISIBLE
                    }
                }
            } else {
                when(view){
                    is ImageView -> {
                        view.visibility=View.INVISIBLE
                    }
                    is TextView -> {
                        view.visibility=View.INVISIBLE
                    }
                    is RecyclerView -> {
                        view.visibility=View.VISIBLE
                        mAdapter?.setData(favoriteEntity)
                    }
                }

            }
        }
    }
}