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
import com.predator.mad.Constants
import com.predator.mad.Utils
import com.predator.mad.R
import com.predator.mad.activity.MainActivity
import com.predator.mad.databinding.FragmentLoginBinding
import com.predator.mad.models.Users
import com.predator.mad.viewmodel.AuthViewModal
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {

    lateinit var binding: FragmentLoginBinding
    private val viewModel:AuthViewModal by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(layoutInflater)
        binding.tvRegisterLogin.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment2_to_registerFragment2)
        }

        binding.btnLoginLogin.setOnClickListener{

            if (Utils.checkEmpty(binding.tvLoginEmail)) Utils.setError(binding.tvLoginEmail,"Enter A Valid Email")
            else if(Utils.checkEmpty(binding.tvPasswordLogin)) Utils.setError(binding.tvPasswordLogin,"Enter Password...")
            else {
                viewModel.loginUser(
                    requireContext(),
                    binding.tvLoginEmail.text.toString(),
                    binding.tvPasswordLogin.text.toString()
                )
            }
        }

        binding.tvForgotLogin.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment2_to_forgetFragment2)
        }

        viewModel.apply {
            lifecycleScope.launch {
                isLoggedIn.collect{
                    if (it==true){
                        val uid = viewModel.getUserId()
                        viewModel.getFireStoreInstance().collection(Constants.CollectionStudents).document(uid).get().addOnSuccessListener{
                            val user = it.toObject(Users::class.java)
                            Utils.putStudent(requireContext(),user!!)
                            startActivity(Intent(requireContext(), MainActivity::class.java))
                            requireActivity().finish()
                        }



                    }
                }
            }
        }
        return binding.root
    }


}