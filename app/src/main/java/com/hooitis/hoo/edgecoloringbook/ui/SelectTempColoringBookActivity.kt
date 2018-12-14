package com.hooitis.hoo.edgecoloringbook.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.GridLayoutManager
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds
import com.hooitis.hoo.edgecoloringbook.R
import com.hooitis.hoo.edgecoloringbook.base.BaseActivity
import com.hooitis.hoo.edgecoloringbook.di.ViewModelFactory
import com.hooitis.hoo.edgecoloringbook.vm.MainVM
import javax.inject.Inject
import com.hooitis.hoo.edgecoloringbook.databinding.ActivitySelectTempColoringbookBinding
import com.hooitis.hoo.edgecoloringbook.model.edgecoloringbook.PassColoringBook
import com.hooitis.hoo.edgecoloringbook.utils.UiUtils


class SelectTempColoringBookActivity: BaseActivity(){
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var viewModel: MainVM
    private lateinit var binding: ActivitySelectTempColoringbookBinding
    private lateinit var mInterstitialAd: InterstitialAd
    private lateinit var alertDialog: AlertDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MobileAds.initialize(this,  getString(R.string.admob))
        val adRequest = AdRequest.Builder().build()

        binding = DataBindingUtil.setContentView(this, R.layout.activity_select_temp_coloringbook)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainVM::class.java)

        binding.viewModel = viewModel
        binding.setLifecycleOwner(this)
        setContentView(binding.root)

        binding.adView.loadAd(adRequest)

        binding.coloringBooks.apply {
            layoutManager = GridLayoutManager(context, 2)
        }

        viewModel.tempImageId.observe(this, Observer {
            if(it == 0L)
                return@Observer

            val tempColoringBook = viewModel.getTempColoringBook(it!!)
            val resultBitmap = UiUtils.convertStringToBitmap(tempColoringBook.imageData)
            viewModel.savePassColoringBook(PassColoringBook(
                    id = 0,
                    imageData = UiUtils.convertBitmapToString(resultBitmap)))

            val dialog = AlertDialog.Builder(this@SelectTempColoringBookActivity)
            dialog.setView(R.layout.layout_loading_dialog)
            alertDialog = dialog.create()
            alertDialog.window.setBackgroundDrawableResource(android.R.color.transparent)
            alertDialog.setCancelable(false)
            alertDialog.show()

            mInterstitialAd = InterstitialAd(this@SelectTempColoringBookActivity).apply {
                adUnitId = getString(R.string.admob_id)
                loadAd(AdRequest.Builder().build())
                adListener = object : AdListener(){
                    override fun onAdLoaded() {
                        super.onAdLoaded()
                        show()
                    }
                    override fun onAdFailedToLoad(p0: Int) {
                        super.onAdFailedToLoad(p0)
                        alertDialog.dismiss()
                        val intent = Intent(applicationContext, DrawColoringBookActivity::class.java)
                        intent.putExtra("tempId", it)
                        startActivity(intent)
                        finish()
                    }

                    override fun onAdClosed() {
                        super.onAdClosed()
                        alertDialog.dismiss()
                        val intent = Intent(applicationContext, DrawColoringBookActivity::class.java)
                        intent.putExtra("tempId", it)
                        startActivity(intent)
                        finish()
                    }
                }
            }
        })

        viewModel.getTempColoringBookList()
    }


}