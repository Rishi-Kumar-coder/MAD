package com.predator.mad.fragment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.predator.mad.R
import com.predator.mad.Utils
import com.predator.mad.activity.MainActivity
import com.predator.mad.databinding.FragmentSplashBinding
import com.predator.mad.viewmodel.AuthViewModal


class SplashFragment : Fragment() {

    lateinit var binding: FragmentSplashBinding
    private val viewModel : AuthViewModal by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSplashBinding.inflate(layoutInflater)
        val user = FirebaseAuth.getInstance().currentUser
        Log.d("rishi","firebase ${user}")
        Log.d("rishi","Local ${Utils.getStudent(requireContext()).uid}")
        Handler().postDelayed({
            if (user != null){
                Log.d("rishi","here  home")
                startActivity(Intent(requireContext(),MainActivity::class.java))
                requireActivity().finish()
            }
            else {
                Log.d("rishi","here  login")

                findNavController().navigate(R.id.action_splashFragment2_to_loginFragment2)

            }

//            startActivity(Intent(requireContext(),MainActivity::class.java))
//            requireActivity().finish()

        },2000)

        return binding.root
    }


}