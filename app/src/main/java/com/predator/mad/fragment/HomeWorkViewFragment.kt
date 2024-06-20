package com.predator.mad.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.predator.mad.R
import com.predator.mad.adapter.ViewPagerOnlineAdapter
import com.predator.mad.databinding.FragmentHomeWorkViewBinding
import com.predator.mad.models.HomeWork
import com.predator.mad.viewmodel.MainViewModel
import kotlinx.coroutines.launch


class HomeWorkViewFragment : Fragment() {
    lateinit var binding: FragmentHomeWorkViewBinding
    private val mainViewModal: MainViewModel by viewModels()
    lateinit var homeWork:HomeWork
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeWorkViewBinding.inflate(inflater)


        homeWork = arguments?.getParcelable<HomeWork>("homeWork")!!

        mainViewModal.apply {
            lifecycleScope.launch {
                fetchUser(homeWork.auther!!).collect{
                    binding.tvHwAuther.setText("${it.name} ${it.standard}${it.section}")
                }
            }
        }


        if (homeWork != null) {
            binding.tvHwSubject.text = homeWork.subject
            binding.tvHwTitle.text = homeWork.title
            binding.tvHwDesc.text = homeWork.desc
            binding.tvHwDate.text = homeWork.date

            if (homeWork.urls!=null){
            binding.vpHomeworkViewpager.adapter =
                ViewPagerOnlineAdapter(requireContext(), homeWork.urls!!)
                }else{
                binding.vpHomeworkViewpager.adapter =
                    ViewPagerOnlineAdapter(requireContext(), listOf("https://"))
            }
        }



        binding.btnDoubt.setOnClickListener{
            loadChatFragmentWithBundle()
        }




        return binding.root
    }


    private fun loadFragment(fragment: Fragment) {
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment).addToBackStack("")
        transaction.commit()
    }

    private fun loadChatFragmentWithBundle(){
        val transaction = requireActivity().supportFragmentManager.beginTransaction()

        val ChatsFragment = ChatViewFragment()
        val args = Bundle()
        args.putString("auther", homeWork.auther.toString())
        args.putParcelable("hw", homeWork)
        ChatsFragment.arguments = args
        transaction.replace(R.id.container, ChatsFragment).addToBackStack("")
        transaction.commit()
    }


}