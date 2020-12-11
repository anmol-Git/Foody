package com.example.foody.ui.fragment.detail_activity_fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import coil.load
import com.example.foody.R
import com.example.foody.model.Result
import kotlinx.android.synthetic.main.fragment_overview.view.*
import org.jsoup.Jsoup

class OverviewFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_overview, container, false)
    val args = arguments
        val myBundle : Result? =args?.getParcelable("recipeBundle")
        view.mainInageView.load(myBundle?.image)
        view.title_text_view.text =myBundle?.title
        view.like_text_view.text =myBundle?.aggregateLikes.toString()
        view.timeTextView.text =myBundle?.readyInMinutes.toString()
         myBundle?.summary.let {
             val summary =Jsoup.parse(it).text()
             view.summaryTextView.text=summary
         }

        if (myBundle?.vegetarian==true){
            view.vegetarian_image_view.setColorFilter(ContextCompat.getColor(requireContext(),R.color.green))
            view.vegetarian_text_view.setTextColor(ContextCompat.getColor(requireContext(),R.color.green))
        }
        if (myBundle?.vegan==true){
            view.vegan_imageView.setColorFilter(ContextCompat.getColor(requireContext(),R.color.green))
            view.vegan_textView.setTextColor(ContextCompat.getColor(requireContext(),R.color.green))
        }
        if (myBundle?.glutenFree==true){
            view.gluten_imageView.setColorFilter(ContextCompat.getColor(requireContext(),R.color.green))
            view.gluen_textView.setTextColor(ContextCompat.getColor(requireContext(),R.color.green))
        }
        if (myBundle?.dairyFree==true){
            view.diary_image_view.setColorFilter(ContextCompat.getColor(requireContext(),R.color.green))
            view.diary_text_view.setTextColor(ContextCompat.getColor(requireContext(),R.color.green))
        }
        if (myBundle?.veryHealthy==true){
            view.healthy_image_view.setColorFilter(ContextCompat.getColor(requireContext(),R.color.green))
            view.healthy_text_view.setTextColor(ContextCompat.getColor(requireContext(),R.color.green))
        }
        if (myBundle?.cheap==true){
            view.cheap_image_view.setColorFilter(ContextCompat.getColor(requireContext(),R.color.green))
            view.cheap_text_view.setTextColor(ContextCompat.getColor(requireContext(),R.color.green))
        }
         return view
    }


    }
