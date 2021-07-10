package com.example.foody.ui.fragment.main_activity_fragment

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foody.viewmodels.MainViewModel
import com.example.foody.R
import com.example.foody.adapters.RecipesAdapter
import com.example.foody.databinding.FragmentRecipeBinding
import com.example.foody.util.NetworkResult
import com.example.foody.util.NetworkListener
import com.example.foody.util.observeOnce
import com.example.foody.viewmodels.RecipesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RecipeFragment : Fragment(), SearchView.OnQueryTextListener {

    private val args by navArgs<RecipeFragmentArgs>()
    private lateinit var networkListener: NetworkListener
    private var _binding: FragmentRecipeBinding? = null
    private val binding get() =_binding!!
    private lateinit var mainViewModel: MainViewModel
    private lateinit var recipeViewModel: RecipesViewModel
    private val mAdapter by lazy { RecipesAdapter() }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel=ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        recipeViewModel=ViewModelProvider(requireActivity()).get(RecipesViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentRecipeBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.mainViewModel = mainViewModel

        setHasOptionsMenu(true)

        setUpRecycleView()


        recipeViewModel.readBackOnline.observe(viewLifecycleOwner,{
            recipeViewModel.backOnline = it
        })



       lifecycleScope.launchWhenStarted {
           networkListener = NetworkListener()
           networkListener.checkNetworkAvailability(requireContext()).collect { status ->
               recipeViewModel.netwokStatus = status
               recipeViewModel.showNetworkStatus()
               readDatabase()
           }
       }

         //floating button to open the bottom sheet only when internet is available
        binding.recipesFav.setOnClickListener {
            if (recipeViewModel.netwokStatus) {
                findNavController().navigate(R.id.action_recipeFragment_to_recipeBottomSheet)
            } else{
              recipeViewModel.showNetworkStatus()
            }
        }
        return binding.root
    }
    private fun setUpRecycleView(){
        binding.shimmerRecyclerView.adapter=mAdapter
        binding.shimmerRecyclerView.layoutManager=LinearLayoutManager(requireContext())
        showShimmerEffect()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.recipes_menu, menu)
        val search =menu.findItem(R.id.menu_search)
        val searchView =search.actionView as? SearchView
        searchView?.isSubmitButtonEnabled = true
        searchView?.setOnQueryTextListener(this)
    }
    override fun onQueryTextSubmit(query: String?): Boolean {
     if (query != null){
         searchApiData(query)
     }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
     return true
    }


    private fun readDatabase() {
        Log.d("recipeFragment","readDatabase called!")
       lifecycleScope.launch {
           mainViewModel.readRecipe.observeOnce(viewLifecycleOwner,{database ->
               if (database.isNotEmpty() && !args.backFromBottomSheet){
                   mAdapter.setData(database[0].foodRecipe)
                   hideShimmerEffect()
               }else{
                   requestApiData()
               }
           })
       }
    }


    private fun requestApiData(){
        Log.d("recipeFragment","requestApiData called!")
        mainViewModel.getRecipe(recipeViewModel.applyQuery())

        mainViewModel.recipeResponse.observe(viewLifecycleOwner,{response ->
            when(response){
                is NetworkResult.Success -> {hideShimmerEffect()
                    response.data?.let { mAdapter.setData(it) }
                    recipeViewModel.saveMealAndDietType()
                }
                is NetworkResult.Error -> {hideShimmerEffect()
                    loadDatabasefromCache()
                Toast.makeText(requireContext(),response.message.toString(),Toast.LENGTH_LONG).show()}
                is NetworkResult.Loading ->{
                    showShimmerEffect()
            }
        }
        })
    }

    private fun searchApiData(searchQuery :String){
        showShimmerEffect()
        mainViewModel.searchRecipe(recipeViewModel.applySearchQuery(searchQuery))
        mainViewModel.searchResponse.observe(viewLifecycleOwner, {response ->
            when(response){
                is NetworkResult.Success -> {
                    hideShimmerEffect()
                    val foodRecipe =response.data
                    foodRecipe?.let { mAdapter.setData(it) }
                }
                is NetworkResult.Error ->{
                    hideShimmerEffect()
                    loadDatabasefromCache()
                    Toast.makeText(requireContext(),response.message.toString(),Toast.LENGTH_SHORT).show()
                }
                is NetworkResult.Loading ->{
                showShimmerEffect()
            }
            }
        })
    }

     //to retrieve data from database
    private fun loadDatabasefromCache(){
      lifecycleScope.launch {
          mainViewModel.readRecipe.observe(viewLifecycleOwner,{database ->
              if (database.isNotEmpty()){
                  mAdapter.setData(database[0].foodRecipe)
                  hideShimmerEffect()
              }
      })
    }
    }

    private fun showShimmerEffect() {
        binding.shimmerFrameLayout.startShimmer()
        binding.shimmerRecyclerView.visibility = View.GONE
    }

    private fun hideShimmerEffect() {
        binding.shimmerFrameLayout.hideShimmer()
        binding.shimmerRecyclerView.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
