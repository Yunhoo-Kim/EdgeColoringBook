package com.hooitis.hoo.edgecoloringbook.di.module

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.hooitis.hoo.edgecoloringbook.di.ViewModelFactory
import com.hooitis.hoo.edgecoloringbook.di.ViewModelKey
import com.hooitis.hoo.edgecoloringbook.vm.MainVM
import com.hooitis.hoo.edgecoloringbook.vm.VersionVM
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
@Suppress("unused")
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(MainVM::class)
    internal abstract fun bindMainVM(mainVM: MainVM): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(VersionVM::class)
    internal abstract fun bindVersionVM(versionVM: VersionVM): ViewModel

}
