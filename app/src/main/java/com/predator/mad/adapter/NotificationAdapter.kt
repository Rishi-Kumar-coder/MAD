import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

import com.predator.mad.databinding.ViewNotificationBinding
import com.predator.mad.models.Notification



class NotificationAdapter() :
    RecyclerView.Adapter<NotificationAdapter.viewHolder>() {




    val diff = object : DiffUtil.ItemCallback<Notification>(){
        override fun areItemsTheSame(oldItem: Notification, newItem: Notification): Boolean {
            return oldItem.uid == newItem.uid
        }

        override fun areContentsTheSame(oldItem: Notification, newItem: Notification): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this,diff)



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {

        return viewHolder(ViewNotificationBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {

        val notification = differ.currentList[position]
        holder.binding.apply {
            notiTvTitle.text = notification.title
            notiTvDate.text = notification.date
            notiTvDesc.text = notification.body

        }

    }

    class viewHolder(val binding: ViewNotificationBinding): RecyclerView.ViewHolder(binding.root)


}
