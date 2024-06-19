package com.predator.mad.fragment

import HomeWorkAdapter
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.predator.mad.R
import com.predator.mad.Utils
import com.predator.mad.activity.AuthActivity
import com.predator.mad.databinding.FragmentHomeBinding
import com.predator.mad.models.HomeWork
import com.predator.mad.models.Notification
import com.predator.mad.viewmodel.MainViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar


class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding
    private val mainViewmodel : MainViewModel by viewModels()
    lateinit var adapter:HomeWorkAdapter
    private var _standard=MutableStateFlow<String>("1")
    private var _section=MutableStateFlow<String>("A")
    private var _date=MutableStateFlow<String>(Utils.getCurrentDate())

    private val _HomeWorkData = MutableStateFlow<ArrayList<HomeWork>>(ArrayList<HomeWork>())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)


        binding.btnHomeDate.text = Utils.getCurrentDate()


        binding.rvMainHomeWork.layoutManager = LinearLayoutManager(requireContext())
        adapter= HomeWorkAdapter()
        adapter.differ.submitList(_HomeWorkData.value)

        binding.rvMainHomeWork.adapter = adapter

        adapter.onItemClick ={ item, pos->

            onHomeWorkClick(item)
        }

        setHomeWork(Utils.getCurrentDate())

//        binding.btnSettingHome.setOnClickListener{
//            FirebaseAuth.getInstance().signOut()
//            startActivity(Intent(requireContext(),AuthActivity::class.java))
//        }

        binding.btnHomeDate.setOnClickListener{
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(requireContext(),
                { view, year, monthOfYear, dayOfMonth ->
                    // Handle the selected date here
                    var selectedDate:String
                    if(monthOfYear+1 <10){
                        selectedDate  = "$year-0${monthOfYear + 1}-$dayOfMonth"

                    }
                    if(dayOfMonth <10){
                        selectedDate  = "$year-${monthOfYear + 1}-0$dayOfMonth"

                    }

                    if(dayOfMonth <10 && monthOfYear+1 <10){
                        selectedDate  = "$year-0${monthOfYear + 1}-0$dayOfMonth"

                    }
                    else {
                        selectedDate  = "$year-${monthOfYear + 1}-$dayOfMonth"
                    }

                    _date.value = selectedDate

                    binding.btnHomeDate.setText(selectedDate)

                    _date.update { selectedDate }
                    Log.d("rishi",_date.value)
                    setHomeWork(_date.value)


                },
                year, month, day)

            datePickerDialog.show()

        }
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setHomeWork(date:String){

        val standard = Utils.getStudent(requireContext()).standard
        val section = Utils.getStudent(requireContext()).section


        lifecycleScope.launch {
            mainViewmodel.fetchData(standard!!,section!!,date).collect{
                _HomeWorkData.value = it
                Log.d("rishi",it.toString())
                if (it.isEmpty()){
                    binding.tvMainNoting.visibility = View.VISIBLE
                    binding.rvMainHomeWork.visibility = View.GONE
                }
                else{
                    binding.tvMainNoting.visibility = View.GONE
                    binding.rvMainHomeWork.visibility = View.VISIBLE
                    adapter.differ.submitList(it)
                    adapter.notifyDataSetChanged()
                }

            }
        }



    }

    private  fun loadFragment(fragment: Fragment){
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container,fragment)
        transaction.commit()
    }

    fun onHomeWorkClick(homeWork: HomeWork){

        val hwHW_ViewFragment = HomeWorkViewFragment()
        val bundle = Bundle()

        bundle.putParcelable("homeWork",homeWork)
        hwHW_ViewFragment.arguments = bundle
        Log.d("rishi",homeWork.toString())
        loadFragment(hwHW_ViewFragment)

    }


}