package com.example.tumbaiorganizer.ui.gallery

import androidx.lifecycle.ViewModel
import com.example.tumbaiorganizer.Model.Categoria

class GalleryViewModel : ViewModel() {

    val listaCategorias = arrayListOf<Categoria>()

    fun clearList() {
        listaCategorias.clear()
    }

    fun addCategoria(categoria: Categoria) {
        listaCategorias.add(categoria)
    }
}