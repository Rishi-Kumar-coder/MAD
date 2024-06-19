import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

import com.predator.mad.Constants

import com.predator.mad.databinding.ViewChatBinding

import com.predator.mad.models.ChatUser

import com.predator.mad.models.Users

import com.squareup.picasso.Picasso

class ChatListAdapter() :
    RecyclerView.Adapter<ChatListAdapter.viewHolder>() {


    val diff = object : DiffUtil.ItemCallback<ChatUser>() {
        override fun areItemsTheSame(oldItem: ChatUser, newItem: ChatUser): Boolean {
            return oldItem.uid == newItem.uid
        }

        override fun areContentsTheSame(oldItem: ChatUser, newItem: ChatUser): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diff)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {

        return viewHolder(
            ViewChatBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {

        val person = differ.currentList[position]


        val uid = person.uid

        FirebaseFirestore.getInstance().collection(Constants.CollectionAdminUser).document(uid)
            .get().addOnSuccessListener {
            val user = it.toObject(Users::class.java)

            holder.binding.apply {
                tvName.setText(user!!.name!!)
                tvLastMsg.text = person.lastMsg
            }
            holder.binding.root.setOnClickListener{
                onItemClick
                    ?.invoke(person,position)
            }
        }


    }

    var onItemClick: ((ChatUser, Int) -> Unit)? = null

    class viewHolder(val binding: ViewChatBinding) : RecyclerView.ViewHolder(binding.root)


}
