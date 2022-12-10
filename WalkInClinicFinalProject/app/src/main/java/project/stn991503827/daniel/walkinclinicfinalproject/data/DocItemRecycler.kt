package project.stn991503827.daniel.walkinclinicfinalproject.data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import project.stn991503827.daniel.walkinclinicfinalproject.databinding.DoctorItemBinding

class DocItemRecycler(
    private val docList: List<DocItem>, private val listener: OnItemClickListener
) : Adapter<DocItemRecycler.DocItemViewHolder>() {
    private lateinit var binding : DoctorItemBinding
    inner class DocItemViewHolder(binding: DoctorItemBinding) : RecyclerView.ViewHolder (binding.root),
    View.OnClickListener {
        val nameText : TextView = binding.textDoctorItemName
        val specText : TextView = binding.textDoctorItemSpec
        val emailText : TextView = binding.textDoctorItemEmail
        val phoneText : TextView = binding.textDoctorItemPhone
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
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DocItemViewHolder {
        binding = DoctorItemBinding.inflate(LayoutInflater.from(parent.context))
        return DocItemViewHolder(binding)
    }
    override fun onBindViewHolder(holder:DocItemViewHolder, position: Int) {
        val currDoc = docList[position]
        holder.nameText.text = currDoc.name
        holder.specText.text = currDoc.spec
        holder.emailText.text = currDoc.email
        holder.phoneText.text = currDoc.phone

    }

    override fun getItemCount() = docList.count()
}