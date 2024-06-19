package com.predator.mad.fragment

import AttendenceAdapter
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.predator.mad.Utils
import com.predator.mad.activity.AuthActivity
import com.predator.mad.databinding.FragmentProfileBinding
import com.predator.mad.modal.Attendence
import com.predator.mad.viewmodel.MainViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch


class ProfileFragment : Fragment() {
    lateinit var binding: FragmentProfileBinding
    private val viewModel: MainViewModel by viewModels()

    lateinit var  adapter : AttendenceAdapter
    private val _AttendenceData = MutableStateFlow<ArrayList<Attendence>>(ArrayList<Attendence>())
    val AttendenceData = _AttendenceData.value
    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(layoutInflater)

        adapter = AttendenceAdapter()

        binding.rvAtt.layoutManager = GridLayoutManager(requireContext(),6)

        binding.rvAtt.adapter = adapter
        setAttendence(Utils.getCurrentDate())

        val student = Utils.getStudent(requireContext())
        binding.tvNamePro.text = student.name

        binding.btnLogout.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            Utils.clearStudent(requireContext())
            startActivity(Intent(requireContext(),AuthActivity::class.java))
            requireActivity().finish()

        }

        return binding.root
    }



    fun setAttendence(date:String){

        val standard = Utils.getStudent(requireContext()).standard
        val section = Utils.getStudent(requireContext()).section


        lifecycleScope.launch {
            viewModel.fetchAttendence(Utils.getStudent(requireContext()).uid,Utils.getCurrentMonth(),Utils.getCurrentYear()).collect{
                _AttendenceData.value = it
                Log.d("rishi",it.toString() + Utils.getStudent(requireContext()).uid)
                if (it.isEmpty()){
                    binding.rvAtt.visibility = View.GONE

                }
                else{
                    binding.rvAtt.visibility = View.VISIBLE


                    adapter.differ.submitList(it)
                    adapter.notifyDataSetChanged()
                }

            }
        }



    }

}