package project.stn991503827.daniel.walkinclinicfinalproject.data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import project.stn991503827.daniel.walkinclinicfinalproject.databinding.AppointmentItemBinding

class AppItemRecycler (
    private val appList: List<ApptItem>, private val listener: OnItemClickListener
) :
    Adapter<AppItemRecycler.ApptItemViewHolder>() {

    private lateinit var binding : AppointmentItemBinding

    inner class ApptItemViewHolder(binding : AppointmentItemBinding) : RecyclerView.ViewHolder (binding.root),
    View.OnClickListener {
        var id : String? = binding.id
        val nameText : TextView = binding.textCardApptName
        val emailText : TextView = binding.textCardApptEmail
        val dateText : TextView = binding.textCardApptDate
        val detailsText : TextView = binding.textCardApptDetails

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            listener.onItemClick(bindingAdapterPosition)
        }
    }
    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ApptItemViewHolder {
        binding = AppointmentItemBinding.inflate(LayoutInflater.from(parent.context))
        return ApptItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ApptItemViewHolder, position: Int) {
        val currAppt = appList[position]
        holder.id = currAppt.id
        holder.nameText.text = currAppt.name
        holder.dateText.text = currAppt.date
        holder.emailText.text = currAppt.email
        holder.detailsText.text = currAppt.details
    }

    override fun getItemCount() = appList.count()



}