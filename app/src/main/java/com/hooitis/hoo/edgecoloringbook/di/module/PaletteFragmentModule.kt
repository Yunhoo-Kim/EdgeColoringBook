package com.hooitis.hoo.edgecoloringbook.di.module

import com.hooitis.hoo.edgecoloringbook.di.FragmentScope
import com.hooitis.hoo.edgecoloringbook.ui.PaletteFragment
import dagger.Binds
import dagger.Module

@Module
@Suppress("unused")
abstract class PaletteFragmentModule{

    @FragmentScope
    @Binds
    abstract fun bindPaletteFragment(paletteFragment: PaletteFragment): PaletteFragment
}
