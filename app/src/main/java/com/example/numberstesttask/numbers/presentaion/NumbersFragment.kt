package com.example.numberstesttask.numbers.presentaion

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.numberstesttask.R
import com.example.numberstesttask.details.presentation.DetailsFragment
import com.example.numberstesttask.main.presentaion.ShowFragment

class NumbersFragment : Fragment() {

    private var showFragment: ShowFragment = ShowFragment.Empty()

    override fun onAttach(context: Context) {
        showFragment = context as ShowFragment
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_numbers, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<ProgressBar>(R.id.progressBar).visibility = View.GONE
        //todo refactor amd remove hardcode
        view.findViewById<Button>(R.id.getFactButton).setOnClickListener {
            showFragment?.show(DetailsFragment.newInstance("some information about the random number hardcoded"))
        }

        view.findViewById<Button>(R.id.getRandomFactButton).setOnClickListener {
            Toast.makeText(context, "Get random fact", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDetach() {
        showFragment = ShowFragment.Empty()
        super.onDetach()
    }
}