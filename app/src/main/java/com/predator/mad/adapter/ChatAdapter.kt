package com.predator.mad.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.predator.mad.R
import com.predator.mad.Utils
import com.predator.mad.models.HomeWork
import com.predator.mad.models.Messege
import com.predator.mad.viewmodel.MainViewModel

class ChatAdapter(uid: String) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val currentUserUid: String = uid

    val diff = object : DiffUtil.ItemCallback<Messege>(){
        override fun areItemsTheSame(oldItem: Messege, newItem: Messege): Boolean {
            return oldItem.sender_uid == newItem.sender_uid && oldItem.msg == newItem.msg
        }

        override fun areContentsTheSame(oldItem: Messege, newItem: Messege): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this,diff)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            SENT_MESSAGE_VIEW_TYPE -> {
                val view = layoutInflater.inflate(R.layout.item_messege_sent, parent, false)
                SentMessageViewHolder(view)
            }
            RECEIVED_MESSAGE_VIEW_TYPE -> {
                val view = layoutInflater.inflate(R.layout.item_messege_recieve, parent, false)
                ReceivedMessageViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = differ.currentList[position]

        when (holder.itemViewType) {
            SENT_MESSAGE_VIEW_TYPE -> {
                (holder as SentMessageViewHolder).bind(message)
            }
            RECEIVED_MESSAGE_VIEW_TYPE -> {
                (holder as ReceivedMessageViewHolder).bind(message)
            }
        }
    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun getItemViewType(position: Int): Int {
        return if (differ.currentList[position].sender_uid == currentUserUid) {
            SENT_MESSAGE_VIEW_TYPE
        } else {
            RECEIVED_MESSAGE_VIEW_TYPE
        }
    }

    inner class SentMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val messageTextView: TextView = itemView.findViewById(R.id.tv_sent)
//        private val timestampTextView: TextView = itemView.findViewById(R.id.text_timestamp_sent)

        fun bind(message: Messege) {
            messageTextView.text = message.msg

        }
    }

    inner class ReceivedMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val messageTextView: TextView = itemView.findViewById(R.id.tv_recieved)
//        private val timestampTextView: TextView = itemView.findViewById(R.id.text_timestamp_received)
//        private val senderNameTextView: TextView = itemView.findViewById(R.id.text_sender_name) // Optional: Display sender name if available

        fun bind(message: Messege) {
            messageTextView.text = message.msg

        }
    }

    companion object {
        private const val SENT_MESSAGE_VIEW_TYPE = 1
        private const val RECEIVED_MESSAGE_VIEW_TYPE = 2
    }
}
