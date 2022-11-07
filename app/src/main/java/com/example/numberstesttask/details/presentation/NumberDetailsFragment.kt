package com.example.numberstesttask.details.presentation

import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.example.numberstesttask.R
import com.example.numberstesttask.main.presentaion.BaseFragment

class NumberDetailsFragment : BaseFragment<NumberDetailsViewModel>() {

    override val viewModelClass: Class<NumberDetailsViewModel> = NumberDetailsViewModel::class.java
    override val layoutId = R.layout.fragment_details

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val value = viewModel.read()
        view.findViewById<TextView>(R.id.detailsTextView).text = value
    }
}