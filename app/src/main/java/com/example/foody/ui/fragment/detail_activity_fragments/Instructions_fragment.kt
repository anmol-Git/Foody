package com.example.foody.ui.fragment.detail_activity_fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import com.example.foody.databinding.FragmentInstructionsFragmentBinding
import com.example.foody.model.Result


class Instructions_fragment : Fragment() {

    private var _binding: FragmentInstructionsFragmentBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentInstructionsFragmentBinding.inflate(inflater, container, false)
        val args = arguments
        val myBundle: Result? = args?.getParcelable("recipeBundle")
        binding.instructionWebView.webViewClient = object : WebViewClient() {}
        val websiteUrl: String = myBundle!!.sourceUrl
        binding.instructionWebView.loadUrl(websiteUrl)
        return binding.root
    }
}