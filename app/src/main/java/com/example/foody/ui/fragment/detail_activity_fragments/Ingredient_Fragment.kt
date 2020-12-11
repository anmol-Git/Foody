package com.example.foody.ui.fragment.detail_activity_fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foody.R
import com.example.foody.adapters.Ingredients_adapter
import com.example.foody.model.Result
import kotlinx.android.synthetic.main.fragment_ingridient_.view.*


class Ingredient_Fragment : Fragment() {

  private  val mAdapter : Ingredients_adapter by lazy {  Ingredients_adapter() }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_ingridient_, container, false)
        val args = arguments
        val myBundle : Result? =args?.getParcelable("recipeBundle")
        setUpRecycleView(view)
        myBundle?.extendedIngredients?.let {
            mAdapter.setData(it)
        }
        return view
    }
     private fun setUpRecycleView(view :View) {
         view.ingridient_recycleView.adapter=mAdapter
         view.ingridient_recycleView.layoutManager=LinearLayoutManager(requireContext())
     }
    }
