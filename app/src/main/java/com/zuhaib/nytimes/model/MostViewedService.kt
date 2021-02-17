package com.zuhaib.nytimes.model

import com.zuhaib.nytimes.di.component.DaggerApiComponent
import io.reactivex.Single
import javax.inject.Inject

class MostViewedService {

    @Inject
    lateinit var api:MostViewedApi

    init {
        DaggerApiComponent.create().inject(this)
    }

    fun getMostView():Single<MostViewed>{
        return api.getMostViews()
    }
}