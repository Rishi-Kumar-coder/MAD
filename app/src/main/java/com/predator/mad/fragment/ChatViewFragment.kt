package com.predator.mad.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.predator.mad.Constants
import com.predator.mad.R
import com.predator.mad.Utils
import com.predator.mad.adapter.ChatAdapter
import com.predator.mad.databinding.FragmentChatViewBinding
import com.predator.mad.models.ChatUser
import com.predator.mad.models.HomeWork
import com.predator.mad.models.Messege
import com.predator.mad.viewmodel.MainViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ChatViewFragment : Fragment() {

    lateinit var binding: FragmentChatViewBinding
    lateinit var recipient_uid:String
    private val viewModel:MainViewModel by viewModels()
    var adminName = ""
    var homework: HomeWork? = null
    var sender_uid = ""
    var MessegesData = ArrayList<Messege>()

    lateinit var adapter : ChatAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatViewBinding.inflate(layoutInflater)
        sender_uid = Utils.getStudent(requireContext()).uid
        recipient_uid = arguments?.getString("auther").toString()

        binding.viewAbout.visibility = View.GONE

       homework = arguments?.getParcelable<HomeWork>("hw")

        viewModel.fetchUser(recipient_uid)

        viewModel.apply {
            lifecycleScope.launch {
                _AdminName.collect{
                    adminName = it
                    binding.tvRecipentName.text = adminName
                }
            }
        }

        if (homework==null){
            binding.viewAbout.visibility = View.GONE
        }else{
            binding.tvHwAbout.text = homework!!.title
        }

        binding.btnSend.setOnClickListener{
            var messege = Messege(
                date = Utils.getCurrentDate(),
                time = Utils.getCurrentTime(),
                msg = binding.tvMsg.text.toString(),
                sender_uid = Utils.getStudent(requireContext()).uid,
                reciver_uid = recipient_uid,
            )
            if (homework!=null){
                messege.refference = homework!!.uid
            }
            sendMessege(requireContext(),messege)

            binding.tvMsg.setText("")
            binding.rvChat.scrollToPosition(MessegesData.size-1)




        }
        adapter = ChatAdapter(Utils.getStudent(requireContext()).uid)

        val layoutManager = LinearLayoutManager(context)


        binding.rvChat.layoutManager = layoutManager

        binding.rvChat.adapter = adapter

        FirebaseFirestore.getInstance().collection(Constants.CollectionMessege).document(sender_uid+recipient_uid).collection(Constants.CollectionMessege).get().addOnSuccessListener {

            for (doc in it){
                val messege = doc.toObject<Messege>()
                MessegesData.add(messege)
            }
            adapter.differ.submitList(MessegesData)
        }





        return binding.root
    }

    fun sendMessege(context: Context, messege: Messege){


            val ChatUser = ChatUser(uid = recipient_uid, lastMsg = "")
            viewModel.getFireStoreInstance().collection(Constants.CollectionStudents)
                .document(sender_uid).collection(Constants.CollectionMessege)
                .document(recipient_uid).set(ChatUser)
            ChatUser.uid = sender_uid

            viewModel.getFireStoreInstance().collection(Constants.CollectionAdminUser)
                .document(recipient_uid).collection(Constants.CollectionMessege)
                .document(sender_uid).set(ChatUser)


            viewModel.getFireStoreInstance().collection(Constants.CollectionMessege).document(sender_uid+recipient_uid).collection(Constants.CollectionMessege).document().set(messege).addOnSuccessListener{
                Utils.showToast(context,"Messege Sent")
                MessegesData.add(messege)
                adapter.notifyItemInserted(MessegesData.size)
            }


    }


}