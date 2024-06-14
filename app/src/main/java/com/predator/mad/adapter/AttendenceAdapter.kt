import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.predator.mad.R
import com.predator.mad.databinding.ViewAttendenceBinding
import com.predator.mad.modal.Attendence


class AttendenceAdapter() :
    RecyclerView.Adapter<AttendenceAdapter.viewHolder>() {




    val diff = object : DiffUtil.ItemCallback<Attendence>(){
        override fun areItemsTheSame(oldItem: Attendence, newItem: Attendence): Boolean {
            return oldItem.uid == newItem.uid
        }

        override fun areContentsTheSame(oldItem: Attendence, newItem: Attendence): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this,diff)



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {

        return viewHolder(ViewAttendenceBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: viewHolder, position: Int) {

        val attendence = differ.currentList[position]
        holder.binding.apply {
            tvDateAtt.text = attendence.day
            tvMonthAtt.text = attendence.month

            if (attendence.status == "p"){
                cvAtt.setCardBackgroundColor(R.color.green)
            }else if (attendence.status == "a"){
                cvAtt.setCardBackgroundColor(R.color.orange_link)
            }
            else{
                cvAtt.setCardBackgroundColor(R.color.grey_hint)
            }

        }

    }

    class viewHolder(val binding: ViewAttendenceBinding): RecyclerView.ViewHolder(binding.root)


}
