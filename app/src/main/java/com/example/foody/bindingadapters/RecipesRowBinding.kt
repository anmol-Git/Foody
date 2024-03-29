package com.example.foody.bindingadapters

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import coil.load
import com.example.foody.R
import com.example.foody.model.Result
import com.example.foody.ui.fragment.main_activity_fragment.RecipeFragmentDirections
import org.jsoup.Jsoup
import java.lang.Exception

class RecipesRowBinding {

    companion object {


        @BindingAdapter("onRecipeClickedListener")
        @JvmStatic
        fun onRecipeClickedListener(recipeRowLayout :ConstraintLayout, result: Result){
            recipeRowLayout.setOnClickListener {
                try {
                 val  action = RecipeFragmentDirections.actionRecipeFragmentToDetailsActivity(result)
                    recipeRowLayout.findNavController().navigate(action)
                }catch (e :Exception){
                    Log.d("onRecipeClickListener", e.toString())
                }
            }
        }

        @BindingAdapter("loadImageFromUrl")
        @JvmStatic
        fun loadImageFromUrl(imageView: ImageView,imageUrl :String){
            imageView.load(imageUrl){
                crossfade(600)
                error(R.drawable.ic_error_image)
            }
        }
        @BindingAdapter("applyVeganColor")
        @JvmStatic
        fun applyVeganColor(view : View,vegan: Boolean){
            if (vegan){
                when(view){
                    is TextView ->{
                        view.setTextColor(ContextCompat.getColor(view.context, R.color.green))
                    }
                    is ImageView -> {
                        view.setColorFilter(ContextCompat.getColor(view.context,R.color.green))
                    }
                }
            }
        }

        @BindingAdapter("parseHtml")
        @JvmStatic
        fun parseHtml(textView: TextView,description :String?){
            if(description!=null){
                val desc = Jsoup.parse(description).text()
                textView.text=desc
            }
        }
    }
}