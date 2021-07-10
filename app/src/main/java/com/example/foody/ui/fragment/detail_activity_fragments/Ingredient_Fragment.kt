package com.example.foody.ui.fragment.detail_activity_fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foody.R
import com.example.foody.adapters.Ingredients_adapter
import com.example.foody.databinding.FragmentIngridientBinding
import com.example.foody.model.Result


class Ingredient_Fragment : Fragment() {

    private val mAdapter: Ingredients_adapter by lazy { Ingredients_adapter() }
    private var _binding: FragmentIngridientBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentIngridientBinding.inflate(inflater, container, false)
        val args = arguments
        val myBundle: Result? = args?.getParcelable("recipeBundle")
        setUpRecycleView()
        myBundle?.extendedIngredients?.let {
            mAdapter.setData(it)
        }
        return binding.root
    }

    private fun setUpRecycleView() {
        binding.ingridientRecycleView.adapter = mAdapter
        binding.ingridientRecycleView.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
