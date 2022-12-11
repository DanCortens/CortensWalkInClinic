package project.stn991503827.daniel.walkinclinicfinalproject.data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import project.stn991503827.daniel.walkinclinicfinalproject.databinding.PatientItemBinding

class PatientItemRecycler( private val patientList : List<PatientItem>,
                           private val listener : OnItemClickListener) :
    RecyclerView.Adapter<PatientItemRecycler.PatientItemViewHolder>(){

    private lateinit var binding : PatientItemBinding

    inner class PatientItemViewHolder(binding: PatientItemBinding) : RecyclerView.ViewHolder (binding.root),
    View.OnClickListener {
        var id : String? = binding.id
        var nameText : TextView = binding.textPatientItemName
        var ageText : TextView = binding.textPatientItemAge
        var emailText : TextView = binding.textPatientItemEmail
        var phoneText : TextView = binding.textPatientItemPhone
        var notesText : TextView = binding.textPatientItemNotes

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PatientItemViewHolder {
        binding = PatientItemBinding.inflate(LayoutInflater.from(parent.context))
        return PatientItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PatientItemViewHolder, position: Int) {
        val currPatient = patientList[position]
        holder.id = currPatient.email
        holder.nameText.text = currPatient.name
        holder.ageText.text = currPatient.age
        holder.emailText.text = currPatient.email
        holder.phoneText.text = currPatient.phone
        holder.notesText.text = currPatient.notes
    }

    override fun getItemCount() = patientList.count()
}