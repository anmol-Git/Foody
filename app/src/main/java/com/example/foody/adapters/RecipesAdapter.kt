package com.example.foody.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.foody.databinding.RecipeRowLayoutBinding
import com.example.foody.model.FoodRecipe
import com.example.foody.model.Result
import com.example.foody.util.RecipeDiffUtil


class RecipesAdapter() :RecyclerView.Adapter<RecipesAdapter.MyViewHolder>() {

    private var recipes = emptyList<Result>()


    class MyViewHolder(private val binding : RecipeRowLayoutBinding) : RecyclerView.ViewHolder(binding.root) {


        fun bind (result: Result){
            binding.result= result
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup) :MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =RecipeRowLayoutBinding.inflate(layoutInflater,parent,false)
                return MyViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currRecipe =recipes[position]
        holder.bind(currRecipe)
    }

    override fun getItemCount(): Int {
       return recipes.size
    }

    fun setData(newData : FoodRecipe){
        val recipeDiffUtil =RecipeDiffUtil(recipes,newData.results)
        val diffUtilResult =DiffUtil.calculateDiff(recipeDiffUtil)
        recipes =newData.results
        /***notifyDataSetChanged()*/
        diffUtilResult.dispatchUpdatesTo(this)
    }
}