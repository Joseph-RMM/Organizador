package com.example.tumbaiorganizer.Model

data class Categoria (var Id_categoria : Int, var Nombre : String) {
    override fun toString(): String {
        return Nombre
    }
}