package com.example.foody.ui.fragment.detail_activity_fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import coil.load
import com.example.foody.R
import com.example.foody.bindingadapters.RecipesRowBinding
import com.example.foody.databinding.FragmentOverviewBinding
import com.example.foody.model.Result
import org.jsoup.Jsoup

class OverviewFragment : Fragment() {

    private var _binding: FragmentOverviewBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentOverviewBinding.inflate(inflater, container, false)
        val args = arguments
        val myBundle: Result = args!!.getParcelable<Result>("recipeBundle") as Result
        binding.mainInageView.load(myBundle.image)
        binding.titleTextView.text = myBundle.title
        binding.likeTextView.text = myBundle.aggregateLikes.toString()
        binding.timeTextView.text = myBundle.readyInMinutes.toString()
//         myBundle?.summary.let {
//             val summary =Jsoup.parse(it).text()
//             binding.summaryTextView.text=summary
//         }
        RecipesRowBinding.parseHtml(binding.summaryTextView, myBundle.summary)

        updateColor(myBundle.vegetarian, binding.vegetarianTextView, binding.vegetarianImageView)
        updateColor(myBundle.vegan, binding.veganTextView, binding.veganImageView)
        updateColor(myBundle.cheap, binding.cheapTextView, binding.cheapImageView)
        updateColor(myBundle.dairyFree, binding.diaryTextView, binding.diaryImageView)
        updateColor(myBundle.glutenFree, binding.gluenTextView, binding.glutenImageView)
        updateColor(myBundle.veryHealthy, binding.healthyTextView, binding.healthyImageView)
        return binding.root
    }

    private fun updateColor(stateIsOn: Boolean, tv: TextView, iv: ImageView) {
        if (stateIsOn) {
            iv.setColorFilter(ContextCompat.getColor(requireContext(), R.color.green))
            tv.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
