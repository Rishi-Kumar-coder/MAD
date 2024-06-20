package com.predator.mad.fragment

import ResultsAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.predator.mad.R
import com.predator.mad.databinding.FragmentResultsBinding
import com.predator.mad.viewmodel.MainViewModel
import kotlinx.coroutines.launch


class ResultsFragment : Fragment() {
   lateinit var binding : FragmentResultsBinding
   private val viewModel : MainViewModel by viewModels()

    lateinit var adapter : ResultsAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentResultsBinding.inflate(layoutInflater)

        binding.rvResults.layoutManager = LinearLayoutManager(requireContext())

        adapter = ResultsAdapter()
        binding.rvResults.adapter = adapter

        viewModel.apply {
            lifecycleScope.launch {
                fetchStudentResult(FirebaseAuth.getInstance().uid.toString()).collect{
                    adapter.differ.submitList(it)
                    adapter.notifyDataSetChanged()
                }
            }
        }

        return binding.root
    }


}