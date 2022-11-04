package com.example.numberstesttask.numbers.data.cache

import com.example.numberstesttask.numbers.data.NumberData

class NumberDataToCache : NumberData.Mapper<NumberCache> {
    override fun map(id: String, fact: String) = NumberCache(id, fact, System.currentTimeMillis())
}