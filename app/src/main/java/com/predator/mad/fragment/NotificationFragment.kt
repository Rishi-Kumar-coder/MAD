package com.predator.mad.fragment

import NotificationAdapter
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.predator.mad.Utils
import com.predator.mad.databinding.FragmentNotificationBinding
import com.predator.mad.models.Notification
import com.predator.mad.viewmodel.MainViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class NotificationFragment : Fragment() {

    lateinit var binding : FragmentNotificationBinding
    private val viewModel : MainViewModel by viewModels()
    lateinit var  adapter : NotificationAdapter
    private val _NotificationData = MutableStateFlow<ArrayList<Notification>>(ArrayList<Notification>())
    val NotificationData = _NotificationData.value
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentNotificationBinding.inflate(layoutInflater)
        binding.rvNotification.layoutManager = LinearLayoutManager(requireContext())
        adapter = NotificationAdapter()

        setNotification(Utils.getCurrentDate())
        binding.rvNotification.adapter = adapter
        return binding.root

    }

    fun setNotification(date:String){

        val standard = Utils.getStudent(requireContext()).standard
        val section = Utils.getStudent(requireContext()).section


        lifecycleScope.launch {
            viewModel.fetchNotificatioon(standard!!,section!!,date).collect{
                _NotificationData.value = it
                Log.d("rishi",it.toString())
                if (it.isEmpty()){
                    binding.tvNotingNoti.visibility = View.VISIBLE

                }
                else{
                    binding.tvNotingNoti.visibility = View.GONE


                    adapter.differ.submitList(it)
                    adapter.notifyDataSetChanged()
                }

            }
        }



    }


}