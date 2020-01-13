package com.uniqlo.uniqloandroidapp.ui.discover

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uniqlo.uniqloandroidapp.data.Ad
import com.uniqlo.uniqloandroidapp.respository.AdsRepository
import kotlinx.coroutines.launch

class DiscoverViewModel(

) : ViewModel() {

    private val _adList = MutableLiveData<List<Ad>>().apply { value = emptyList() }
    val adList: LiveData<List<Ad>>
        get() = _adList

    val adsRepository: AdsRepository = AdsRepository()


    fun updateAds() {

        // coroutine on suspend function for network calls
        viewModelScope.launch {

            _adList.value = adsRepository.getAds()
        }
    }

}