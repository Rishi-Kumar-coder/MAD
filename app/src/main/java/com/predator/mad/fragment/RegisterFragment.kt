package com.predator.mad.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.predator.mad.Utils
import com.predator.mad.Constants

import com.predator.mad.viewmodel.AuthViewModal
import com.predator.mad.R
import com.predator.mad.activity.MainActivity
import com.predator.mad.databinding.FragmentRegisterBinding
import kotlinx.coroutines.launch

class RegisterFragment : Fragment() {

    lateinit var binding: FragmentRegisterBinding

    private val viewModel : AuthViewModal by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(layoutInflater)

        binding.btnRegisterRegister.setOnClickListener{

            if (Utils.checkEmpty(binding.tvNameRegister)) binding.tvNameRegister.setError("Enter Name")
            else if (Utils.checkEmpty(binding.tvPhoneRegister)) binding.tvPhoneRegister.setError("Enter Phone Number")
            else if (Utils.checkEmpty(binding.tvEmailRegister)) binding.tvEmailRegister.setError("Enter Email")
            else if (Utils.checkEmpty(binding.tvPasswordRegister)) binding.tvPasswordRegister.setError("Enter Password")
            else if (Utils.checkEmpty(binding.tvCnfPasswordRegister)) binding.tvCnfPasswordRegister.setError("Enter Confirm Password")
            else if (binding.tvPasswordRegister.text.toString() != binding.tvCnfPasswordRegister.text.toString()) binding.tvCnfPasswordRegister.setError("Confirm Password Not Matching")
            else {


                viewModel.registerUser(
                    requireContext(),
                    binding.tvEmailRegister.text.toString(),
                    binding.tvPasswordRegister.text.toString(),
                    binding.tvNameRegister.text.toString(),
                    binding.tvPhoneRegister.text.toString(),
                    binding.tvEmailStandard.text.toString(),
                    binding.tvRegisterSection.text.toString()


                )
            }
        }

        Utils.setListAdapter(requireContext(), Constants.standards,binding.tvEmailStandard)
        Utils.setListAdapter(requireContext(), Constants.sections,binding.tvRegisterSection)



        viewModel.apply {
            lifecycleScope.launch {
                isLoggedIn.collect{
                    if (it == true){
                        startActivity(Intent(requireContext(), MainActivity::class.java))
                        requireActivity().finish()

                    }
                }
            }
        }

        binding.tvLoginRegister.setOnClickListener{
            findNavController().navigate(R.id.action_registerFragment2_to_loginFragment2)
        }


        return binding.root
    }


}