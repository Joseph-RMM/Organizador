package com.example.tumbaiorganizer.ui.detailstarea

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tumbaiorganizer.R

class DetailsTareaFragment : Fragment() {

    companion object {
        fun newInstance() = DetailsTareaFragment()
    }

    private lateinit var viewModel: DetailsTareaViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.details_tarea_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(DetailsTareaViewModel::class.java)
        // TODO: Use the ViewModel
    }

}