package com.example.foody.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.foody.R
import com.example.foody.databinding.IngredientRowLayoutBinding
import com.example.foody.model.ExtendedIngredient
import com.example.foody.util.Constant.Companion.BASE_IMAGE_URL
import com.example.foody.util.RecipeDiffUtil
import java.util.*

class Ingredients_adapter : RecyclerView.Adapter<Ingredients_adapter.MyViewHolder>() {

    private var ingredientList = emptyList<ExtendedIngredient>()

    class MyViewHolder(val binding: IngredientRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            IngredientRowLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.ingredientImageView.load(BASE_IMAGE_URL + ingredientList[position].image) {
            crossfade(600)
            error(R.drawable.ic_error_image)
        }
        holder.binding.ingredientName.text = ingredientList[position].name.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(
                Locale.ROOT
            ) else it.toString()
        }
        holder.binding.ingredientAmount.text = ingredientList[position].amount.toString()
        holder.binding.ingredientUnit.text = ingredientList[position].unit
        holder.binding.ingredientConsistency.text = ingredientList[position].consistency
        holder.binding.original.text = ingredientList[position].original
    }

    override fun getItemCount(): Int {
      return ingredientList.size
    }

    fun setData(newIngredients : List<ExtendedIngredient>) {
        var ingredientDiffUtil = RecipeDiffUtil(ingredientList,newIngredients)
        val diffUtilResult = DiffUtil.calculateDiff(ingredientDiffUtil)
        ingredientList =newIngredients
        diffUtilResult.dispatchUpdatesTo(this)
    }
}