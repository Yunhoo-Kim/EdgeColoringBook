package com.hooitis.hoo.edgecoloringbook.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.hooitis.hoo.edgecoloringbook.R
import com.hooitis.hoo.edgecoloringbook.base.BaseFragment
import com.hooitis.hoo.edgecoloringbook.databinding.FragmentPaletteBinding
import com.hooitis.hoo.edgecoloringbook.databinding.FragmentReviseGuideBinding
import com.hooitis.hoo.edgecoloringbook.di.ViewModelFactory
import com.hooitis.hoo.edgecoloringbook.utils.UiUtils
import com.hooitis.hoo.edgecoloringbook.vm.MainVM
import javax.inject.Inject

class ReviseGuideFragment: BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var viewModel: MainVM
    private lateinit var binding: FragmentReviseGuideBinding

    private var isFirst = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_revise_guide, container, false)
        viewModel = ViewModelProviders.of(activity!!, viewModelFactory).get(MainVM::class.java)
        binding.viewModel = viewModel
        binding.setLifecycleOwner(this)

        binding.saveDrawing.setOnClickListener {
            UiUtils.replaceNewFragment(activity!!, ColoringGuideFragment.newInstance(Bundle()), R.id.container_main)
        }

        return binding.root
    }

    companion object {
        fun newInstance(args: Bundle?): ReviseGuideFragment{
            return ReviseGuideFragment().apply {
                this.arguments = args
            }
        }
    }

}
