package com.catfoxtechnology.tumbaiorganizer.ui.addtarea

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.catfoxtechnology.tumbaiorganizer.R

class AddTareaFragment : Fragment() {

    companion object {
        fun newInstance() = AddTareaFragment()
    }

    private lateinit var viewModel: AddTareaViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.add_tarea_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AddTareaViewModel::class.java)
        // TODO: Use the ViewModel
    }

}