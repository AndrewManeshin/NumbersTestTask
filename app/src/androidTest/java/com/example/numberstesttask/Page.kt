package com.example.numberstesttask

interface Page {

    class Numbers : Page {
        val getFactButton = R.id.getFactButton
        val getRandomFactButton = R.id.getRandomFactButton
        val input = R.id.inputEditText
        val recycler = R.id.historyRecyclerView

        val titleItem = R.id.titleTextView
        val subtitleItem = R.id.subtitleTextView
    }

    class Details : Page {
        val details = R.id.detailsTextView
    }
}