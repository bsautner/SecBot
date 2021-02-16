package com.secbot.di

import com.secbot.Application
import dagger.Component

@Component(modules = [AppModule::class])
interface AppComponent {

    fun inject(instance: Application)


}