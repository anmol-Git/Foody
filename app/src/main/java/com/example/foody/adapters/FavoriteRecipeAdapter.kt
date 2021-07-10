package com.example.foody.adapters

import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.foody.R
import com.example.foody.data.database.entities.FavouriteEntity
import com.example.foody.databinding.FavoriteRecipeRowLayoutBinding
import com.example.foody.ui.fragment.main_activity_fragment.FavouriteRecipeFragmentDirections
import com.example.foody.util.RecipeDiffUtil
import com.example.foody.viewmodels.MainViewModel
import com.google.android.material.snackbar.Snackbar


class FavoriteRecipeAdapter(private val requireActivity : FragmentActivity, private val mainViewModel: MainViewModel) : RecyclerView.Adapter<FavoriteRecipeAdapter.MyViewHolder>(), ActionMode.Callback {

    private var favoriteRecipes = emptyList<FavouriteEntity>()
    private var multiSelection = false
    private var myViewHolders = arrayListOf<MyViewHolder>()
    private lateinit var mActionMode: ActionMode
    private lateinit var rootView :View

    private val selectedRecipes = arrayListOf<FavouriteEntity>()
    class MyViewHolder(val binding: FavoriteRecipeRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(favouriteEntity: FavouriteEntity) {
            binding.favoritesEntity = favouriteEntity
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = FavoriteRecipeRowLayoutBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
       return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        myViewHolders.add(holder)
        rootView = holder.itemView.rootView
        val currentRecipe = favoriteRecipes[position]
        holder.bind(currentRecipe)

        saveItemStateOnScroll(currentRecipe, holder)

        /**Single click listener*/
        holder.binding.favoriterecipeRowLayout.setOnClickListener {
            if (multiSelection) {
                applySelection(holder, currentRecipe)
            } else {
                val action =
                    FavouriteRecipeFragmentDirections.actionFavouriteRecipeFragementToDetailsActivity(
                        currentRecipe.result
                    )
                holder.itemView.findNavController().navigate(action)
            }
        }
        /**Long click listener*/
        holder.binding.favoriterecipeRowLayout.setOnLongClickListener {
            if (!multiSelection) {
                multiSelection = true
                requireActivity.startActionMode(this)
                applySelection(holder, currentRecipe)
                true
            } else {
                applySelection(holder, currentRecipe)
                true
            }

        }
    }

    private fun saveItemStateOnScroll(currentRecipe: FavouriteEntity, holder: MyViewHolder) {
        if (selectedRecipes.contains(currentRecipe)) {
            changeRecipeStyle(holder, R.color.cardBackgroundLightColor, R.color.colorPrimary)
        } else {
            changeRecipeStyle(holder, R.color.cardBackGroundColor, R.color.strokeColor)
        }
    }

    fun applySelection(holder: MyViewHolder, currentRecipe: FavouriteEntity) {
        if (selectedRecipes.contains(currentRecipe)) {
            selectedRecipes.remove(currentRecipe)
            changeRecipeStyle(holder, R.color.cardBackGroundColor, R.color.strokeColor)
            applyActionModeTitle()
        } else {
            selectedRecipes.add(currentRecipe)
            changeRecipeStyle(holder, R.color.cardBackgroundLightColor, R.color.colorPrimary)
            applyActionModeTitle()
        }
    }

    fun changeRecipeStyle(holder : MyViewHolder,backgroundColor :Int, strokeColor : Int) {
        holder.binding.favoriterecipeRowLayout.setBackgroundColor(
            ContextCompat.getColor(
                requireActivity,
                backgroundColor
            )
        )
        holder.binding.favoriteRowCardView.strokeColor =
            ContextCompat.getColor(requireActivity, strokeColor)
    }

    private fun applyActionModeTitle(){
        when(selectedRecipes.size){
            0 -> {
                mActionMode.finish()
                multiSelection = false
            }
            1 -> {
                mActionMode.title ="${selectedRecipes.size} item selected"
            }
            else -> {
                mActionMode.title ="${selectedRecipes.size} items selected"
            }
        }
    }

    override fun getItemCount(): Int {
     return  favoriteRecipes.size
    }



    override fun onCreateActionMode(actionMode: ActionMode?, menu: Menu?): Boolean {
       actionMode?.menuInflater?.inflate(R.menu.favorite_contexual_menu,menu)
        mActionMode = actionMode!!
        applyStatusBarColor(R.color.ContextualStatusBarColor)
        return true
    }

    override fun onPrepareActionMode(actionMode: ActionMode?, menu: Menu?): Boolean {
      return true
    }

    override fun onActionItemClicked(actionMode: ActionMode?, menu: MenuItem?): Boolean {
        if (menu?.itemId==R.id.delete_fav_recipe_menu){
            selectedRecipes.forEach{
                mainViewModel.deleteFavoriteRecipe(it)
            }
            showSnackBar("${selectedRecipes.size} Recipes removed")

            multiSelection =false
            selectedRecipes.clear()
            actionMode?.finish()
        }
        return true
    }

    override fun onDestroyActionMode(actionMode: ActionMode?) {
        myViewHolders.forEach{holder ->
        changeRecipeStyle(holder, R.color.cardBackGroundColor,R.color.strokeColor)
    }
        multiSelection =false
        selectedRecipes.clear()
            applyStatusBarColor(R.color.statusBarColor)
    }

    private fun applyStatusBarColor(color :Int){
        requireActivity.window.statusBarColor=ContextCompat.getColor(requireActivity,color)
    }

    fun setData(newFavRecipe : List<FavouriteEntity>){
        val favrecipeDiffUtil = RecipeDiffUtil(favoriteRecipes,newFavRecipe)
        val diffUtilResult = DiffUtil.calculateDiff(favrecipeDiffUtil)
        favoriteRecipes =newFavRecipe
        diffUtilResult.dispatchUpdatesTo(this)
    }

    private fun showSnackBar(message : String){
        Snackbar.make(rootView,message,Snackbar.LENGTH_SHORT).setAction("Okay") {}.show()
    }
    fun clearContextualActionMode(){
        if (this::mActionMode.isInitialized){
            mActionMode.finish()
        }
    }
}