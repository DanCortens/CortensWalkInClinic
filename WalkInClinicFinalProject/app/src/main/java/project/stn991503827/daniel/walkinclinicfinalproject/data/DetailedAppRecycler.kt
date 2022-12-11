package project.stn991503827.daniel.walkinclinicfinalproject.data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import project.stn991503827.daniel.walkinclinicfinalproject.databinding.DetailedAppointmentItemBinding
import project.stn991503827.daniel.walkinclinicfinalproject.databinding.FragmentAppointmentsBinding

class DetailedAppRecycler(
    private val detAppList : List<DetailedAppItem>, private val listener : OnItemClickListener
) : RecyclerView.Adapter<DetailedAppRecycler.DetailedAppItemViewHolder>()
{
    private lateinit var binding : DetailedAppointmentItemBinding

    inner class DetailedAppItemViewHolder(binding: DetailedAppointmentItemBinding) : RecyclerView.ViewHolder (binding.root),
    View.OnClickListener {
        var id = binding.detAppId
        val pNameText = binding.textApptListPatientName
        val pEmailText = binding.textAppListPatientEmail
        val dNameText = binding.textAppListDocName
        val dEmailText = binding.textAppListDocEmail
        val dateText = binding.textAppListDate
        val detail1Text = binding.textAppListDetails1
        val detail2Text = binding.textAppListDetail2
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailedAppItemViewHolder {
        binding = DetailedAppointmentItemBinding.inflate(LayoutInflater.from(parent.context))
        return DetailedAppItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DetailedAppItemViewHolder, position: Int){
        val appointment = detAppList[position]
        holder.id.text = appointment.id
        holder.pNameText.text = appointment.pName
        holder.pEmailText.text = appointment.pEmail
        holder.dNameText.text = appointment.dName
        holder.dEmailText.text = appointment.dEmail
        holder.dateText.text = appointment.date
        holder.detail1Text.text = appointment.detail1
        holder.detail2Text.text = appointment.detail2
    }

    override fun getItemCount() = detAppList.count()
}