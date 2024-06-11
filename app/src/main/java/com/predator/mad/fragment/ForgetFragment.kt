package com.predator.mad.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.predator.mad.R
import com.predator.mad.databinding.FragmentForgetBinding
import com.predator.mad.viewmodel.AuthViewModal
import kotlinx.coroutines.launch


class ForgetFragment : Fragment() {

    lateinit var binding : FragmentForgetBinding
    private val viewModel : AuthViewModal by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentForgetBinding.inflate(layoutInflater)

        binding.btnRequestEmail.setOnClickListener{
            viewModel.sendPasswordResetEmail(requireContext(),binding.tvEmailForgot.text.toString())
        }

        viewModel.apply {
            lifecycleScope.launch {
                isEmailSent.collect{
                    if (it==true){
                        findNavController().navigate(R.id.action_forgetFragment2_to_loginFragment2)
                    }
                }
            }
        }

        return binding.root
    }


}