package com.zuhaib.nytimes.di.component

import com.zuhaib.nytimes.di.modules.ApiModule
import com.zuhaib.nytimes.model.MostViewedService
import com.zuhaib.nytimes.viewmodel.ListViewModel
import dagger.Component

@Component(modules = [ApiModule::class])
interface ApiComponent {

    fun inject(service: MostViewedService)

    fun inject(viewModel: ListViewModel)

}