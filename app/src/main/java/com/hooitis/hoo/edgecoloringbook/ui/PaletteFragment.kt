package com.hooitis.hoo.edgecoloringbook.ui

import android.app.Fragment
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.hooitis.hoo.edgecoloringbook.R
import com.hooitis.hoo.edgecoloringbook.base.BaseFragment
import com.hooitis.hoo.edgecoloringbook.databinding.FragmentPaletteBinding
import com.hooitis.hoo.edgecoloringbook.di.ViewModelFactory
import com.hooitis.hoo.edgecoloringbook.utils.UiUtils
import com.hooitis.hoo.edgecoloringbook.vm.MainVM
import javax.inject.Inject

class PaletteFragment: BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var viewModel: MainVM
    private lateinit var binding: FragmentPaletteBinding

    private var isFirst = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        MobileAds.initialize(this.activity, getString(R.string.admob))
        val adRequest = AdRequest.Builder().build()

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_palette, container, false)
        viewModel = ViewModelProviders.of(activity!!, viewModelFactory).get(MainVM::class.java)
        binding.viewModel = viewModel


        binding.colorSelect.apply {
            layoutManager = GridLayoutManager(context, 5)
        }
        binding.setLifecycleOwner(this)
        viewModel.brushColor.observe(this, Observer {
            if(isFirst){
                isFirst = false
                return@Observer
            }

            fragmentManager!!.popBackStackImmediate()
        })


        binding.adView.loadAd(adRequest)
        return binding.root
    }

    companion object {
        fun newInstance(args: Bundle?): PaletteFragment{
            return PaletteFragment().apply {
                this.arguments = args
            }
        }
    }

}
