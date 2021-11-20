package com.catfoxtechnology.tumbaiorganizer.ui.slideshow

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SlideshowViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Esta característica aún no está lista. ¡Vuelve pronto!"
    }
    val text: LiveData<String> = _text
}