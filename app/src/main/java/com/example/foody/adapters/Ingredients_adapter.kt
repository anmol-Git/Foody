package com.example.foody.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.foody.R
import com.example.foody.model.ExtendedIngredient
import com.example.foody.util.Constant.Companion.BASE_IMAGE_URL
import com.example.foody.util.RecipeDiffUtil
import kotlinx.android.synthetic.main.ingredient_row_layout.view.*

class Ingredients_adapter : RecyclerView.Adapter<Ingredients_adapter.MyViewHolder>() {

    private var ingredientList = emptyList<ExtendedIngredient>()

    class MyViewHolder(itemView :View) : RecyclerView.ViewHolder(itemView){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
      return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.ingredient_row_layout,parent,false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
      holder.itemView.ingredient_image_view.load(BASE_IMAGE_URL+ingredientList[position].image) {
          crossfade(600)
          error(R.drawable.ic_error_image)
      }
        holder.itemView.ingredient_name.text=ingredientList[position].name.capitalize()
        holder.itemView.ingredient_amount.text=ingredientList[position].amount.toString()
        holder.itemView.ingredient_unit.text=ingredientList[position].unit
        holder.itemView.ingredient_consistency.text=ingredientList[position].consistency
        holder.itemView.original.text=ingredientList[position].original
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