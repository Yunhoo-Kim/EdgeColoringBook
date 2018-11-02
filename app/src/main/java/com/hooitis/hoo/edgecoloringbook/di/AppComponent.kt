package com.hooitis.hoo.edgecoloringbook.di

import com.hooitis.hoo.edgecoloringbook.EdgeColoringBookApp
import com.hooitis.hoo.edgecoloringbook.di.module.DaoModule
import com.hooitis.hoo.edgecoloringbook.di.module.NetworkModule
import com.hooitis.hoo.edgecoloringbook.di.module.ViewModelModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

//    ActivityBuilder::class, DaoModule::class, ViewModelModule::class])
@Suppress(names = ["unchecked", "unsafe", "unused"])
@Singleton
@Component(modules = [AndroidSupportInjectionModule::class, AppModule::class, NetworkModule::class,
    ActivityBuilder::class, DaoModule::class, ViewModelModule::class])
interface AppComponent {

    @Component.Builder
    interface Builder{
        @BindsInstance
        fun application(app: EdgeColoringBookApp): Builder
        fun appModule(appModule: AppModule): Builder
        fun networkModule(networkModule: NetworkModule): Builder
        fun build(): AppComponent
    }

    fun inject(app: EdgeColoringBookApp)
}

