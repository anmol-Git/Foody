package com.example.foody.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.navArgs
import com.example.foody.R
import com.example.foody.adapters.PagerAdapter
import com.example.foody.data.database.entities.FavouriteEntity
import com.example.foody.databinding.ActivityDetailsBinding
import com.example.foody.ui.fragment.detail_activity_fragments.Ingredient_Fragment
import com.example.foody.ui.fragment.detail_activity_fragments.Instructions_fragment
import com.example.foody.ui.fragment.detail_activity_fragments.OverviewFragment
import com.example.foody.viewmodels.MainViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception

@AndroidEntryPoint
class DetailsActivity : AppCompatActivity() {

    private val args by navArgs<DetailsActivityArgs>()
    private val mainViewModel: MainViewModel by viewModels()
    private var recipeSaved = false
    private var savedRecipeId = 0
    private lateinit var binding: ActivityDetailsBinding
    private lateinit var menuItem: MenuItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        binding.toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val fragments = ArrayList<Fragment>()
        fragments.add(OverviewFragment())
        fragments.add(Ingredient_Fragment())
        fragments.add(Instructions_fragment())

        val titles = ArrayList<String>()
        titles.add("Overview")
        titles.add("Ingredients")
        titles.add("Instructions")

        val resultBundle = Bundle()
        resultBundle.putParcelable("recipeBundle", args.result)

        val pagerAdapter = PagerAdapter(resultBundle, fragments, this)

        binding.viewPager2.isUserInputEnabled = false
        binding.viewPager2.apply {
            adapter = pagerAdapter
        }
        TabLayoutMediator(binding.tabLayout, binding.viewPager2) { tab, position ->
            tab.text = titles[position]
        }.attach()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.details_menu, menu)
        menuItem = menu!!.findItem(R.id.save_to_fav)
        checkSaveRecipes(menuItem)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home){
            finish()
        }else if (item.itemId==R.id.save_to_fav && !recipeSaved){
            saveToFavourite(item)
        }
        else if (item.itemId==R.id.save_to_fav && recipeSaved){
            removeFromFavourites(item)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun checkSaveRecipes(item: MenuItem) {
        mainViewModel.readFavouriteRecipe.observe(this, { favoritesEntity ->
            try {
                for (savedRecipe in favoritesEntity) {
                    if (savedRecipe.result.recipeId == args.result.recipeId) {
                        changeMenuItemColor(item, R.color.yellow)
                        savedRecipeId = savedRecipe.id
                        recipeSaved = true
                    }

                }

            } catch (e: Exception) {
                Log.d("Detail Activity", e.message.toString())
            }
        })
    }
    private fun saveToFavourite(item: MenuItem) {
        val favouriteEntity = FavouriteEntity(0,args.result)
        mainViewModel.insertFavoriteRecipe(favouriteEntity)
        changeMenuItemColor(item, R.color.yellow)
        showSnackBar("Recipe saved.")
        recipeSaved =true
    }

    private fun removeFromFavourites(item: MenuItem){
        val favouriteEntity =FavouriteEntity(savedRecipeId,args.result)
        mainViewModel.deleteFavoriteRecipe(favouriteEntity)
        changeMenuItemColor(item, R.color.white)
        showSnackBar("Removed from favorites.")
        recipeSaved =false
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(binding.detailLayout, message, Snackbar.LENGTH_SHORT).setAction("Okay") {}
            .show()
    }

    private fun changeMenuItemColor(item: MenuItem, color: Int) {
        item.icon.setTint(ContextCompat.getColor(this, color))
    }

    override fun onDestroy() {
        super.onDestroy()
        changeMenuItemColor(menuItem, R.color.white)
    }
}