package com.secbot.pi.di

import com.secbot.pi.Program
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {

    fun inject(instance: Program)


}