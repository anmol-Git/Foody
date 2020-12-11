package com.example.foody.ui.fragment.detail_activity_fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import com.example.foody.R
import com.example.foody.model.Result
import kotlinx.android.synthetic.main.fragment_instructions_fragment.view.*


class Instructions_fragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_instructions_fragment, container, false)
        val args = arguments
        val myBundle : Result? =args?.getParcelable("recipeBundle")
        view.instruction_web_view.webViewClient =object : WebViewClient() {}
        val websiteUrl : String =myBundle!!.sourceUrl
        view.instruction_web_view.loadUrl(websiteUrl)
        return view
    }
}