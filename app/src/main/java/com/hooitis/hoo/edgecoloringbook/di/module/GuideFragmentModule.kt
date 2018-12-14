package com.hooitis.hoo.edgecoloringbook.di.module

import com.hooitis.hoo.edgecoloringbook.di.FragmentScope
import com.hooitis.hoo.edgecoloringbook.ui.ColoringGuideFragment
import com.hooitis.hoo.edgecoloringbook.ui.PaletteFragment
import com.hooitis.hoo.edgecoloringbook.ui.ReviseGuideFragment
import com.hooitis.hoo.edgecoloringbook.ui.TedooriGuideFragment
import dagger.Binds
import dagger.Module

@Module
@Suppress("unused")
abstract class GuideFragmentModule{

    @FragmentScope
    @Binds
    abstract fun bindReviseGuideFragment(reviseGuideFragment: ReviseGuideFragment): ReviseGuideFragment

    @FragmentScope
    @Binds
    abstract fun bindColoringGuideFragment(coloringGuideFragment: ColoringGuideFragment): ColoringGuideFragment

    @FragmentScope
    @Binds
    abstract fun bindTedooriGuideFragment(tedooriGuideFragment: TedooriGuideFragment): TedooriGuideFragment
}
