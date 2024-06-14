package com.predator.mad.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.predator.mad.R
import com.predator.mad.adapter.ViewPagerOnlineAdapter
import com.predator.mad.databinding.FragmentHomeWorkViewBinding
import com.predator.mad.models.HomeWork
import com.predator.mad.viewmodel.MainViewModel


class HomeWorkViewFragment : Fragment() {
    lateinit var binding: FragmentHomeWorkViewBinding
    private val mainViewModal: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeWorkViewBinding.inflate(inflater)


        val homeWork = arguments?.getParcelable<HomeWork>("homeWork")

        if (homeWork != null) {
            binding.tvHwSubject.text = homeWork.subject
            binding.tvHwTitle.text = homeWork.title
            binding.tvHwDesc.text = homeWork.desc
            binding.tvHwDate.text = homeWork.date
            binding.tvHwAuther.text = "by:${homeWork.auther}"
            if (homeWork.urls!=null){
            binding.vpHomeworkViewpager.adapter =
                ViewPagerOnlineAdapter(requireContext(), homeWork.urls!!)
                }else{
                binding.vpHomeworkViewpager.adapter =
                    ViewPagerOnlineAdapter(requireContext(), listOf("https://"))
            }
        }

        binding.btnHwBack.setOnClickListener {
            loadFragment(HomeFragment())
//            removeFragment(this)
//            backToPreviousFragment()
        }




        return binding.root
    }
    fun backToPreviousFragment() {
        if ((fragmentManager?.backStackEntryCount ?: 0) > 0) {
            requireFragmentManager().popBackStack()
        } else {
            // Handle no previous fragment (e.g., show a toast)
        }
    }
    fun removeFragment(fragment: Fragment) {
        val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
        fragmentTransaction.remove(fragment)

        fragmentTransaction.commit()
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.commit()
    }


}