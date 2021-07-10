package com.example.foody.ui.fragment.main_activity_fragment

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foody.R
import com.example.foody.adapters.FavoriteRecipeAdapter
import com.example.foody.databinding.FavouriteRecipeFragmentBinding
import com.example.foody.viewmodels.MainViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavouriteRecipeFragment : Fragment() {
    private val mainViewModel : MainViewModel by  viewModels()
    private val mAdapter :FavoriteRecipeAdapter by lazy { FavoriteRecipeAdapter(requireActivity(), mainViewModel) }


    private var _binding : FavouriteRecipeFragmentBinding? =null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true)
        _binding = FavouriteRecipeFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.mainViewModel = mainViewModel
        binding.mAdapter = mAdapter

        setUpRecyclerView(binding.favoriteRecipeRecyclerView)

        return binding.root
    }

    fun setUpRecyclerView(recyclerView: RecyclerView) {
        recyclerView.adapter = mAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        mAdapter.clearContextualActionMode()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.fav_recipe_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.deleteAll) {
            mainViewModel.deleteAllFavoriteRecipe()
           Snackbar.make(binding.root,"All recipes removed", Snackbar.LENGTH_SHORT).setAction("Okay") {}.show()
        }
        return super.onOptionsItemSelected(item)
    }
}
