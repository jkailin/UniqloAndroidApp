package com.uniqlo.uniqloandroidapp

import android.app.Application
import com.dropbox.android.external.store4.Store
import com.uniqlo.uniqloandroidapp.database.CartItemEntity
import com.uniqlo.uniqloandroidapp.model.Ad
import com.uniqlo.uniqloandroidapp.model.Item
import com.uniqlo.uniqloandroidapp.model.Items
import com.uniqlo.uniqloandroidapp.respository.CartRepository
import com.uniqlo.uniqloandroidapp.respository.UniqloRepository
import timber.log.Timber

class UniqloApplication : Application()  {

    // TODO swap to DI
    lateinit var adsStore: Store<String, List<Ad>>
    lateinit var popularItemsStore: Store<String, List<Item>>
    lateinit var itemResultsStore: Store<Pair<String?, String?>, Items>
    lateinit var cartItemsStore: Store<String, List<CartItemEntity>>

    override fun onCreate() {
        super.onCreate()

        // DI?
        adsStore = UniqloRepository.createAdsStore(this)
        popularItemsStore = UniqloRepository.createPopularItemsStore()
        itemResultsStore = UniqloRepository.createItemResultsStore()
        cartItemsStore = CartRepository.createCartItemsStore(this)
        initTimber()
    }

    private fun initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

}