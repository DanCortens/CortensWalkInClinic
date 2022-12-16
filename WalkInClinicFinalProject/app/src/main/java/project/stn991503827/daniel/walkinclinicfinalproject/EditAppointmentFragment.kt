package project.stn991503827.daniel.walkinclinicfinalproject

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import project.stn991503827.daniel.walkinclinicfinalproject.data.DetailedAppItem
import project.stn991503827.daniel.walkinclinicfinalproject.data.Patient
import project.stn991503827.daniel.walkinclinicfinalproject.data.TherapyAppointment
import project.stn991503827.daniel.walkinclinicfinalproject.data.VaxAppointment
import project.stn991503827.daniel.walkinclinicfinalproject.databinding.FragmentEditAppointmentBinding
import project.stn991503827.daniel.walkinclinicfinalproject.viewmodels.*
import java.util.Date

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [EditAppointmentFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditAppointmentFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding : FragmentEditAppointmentBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var user : FirebaseUser
    private val db = Firebase.firestore
    private var date : Date? = null

    private lateinit var userViewModel : UserViewModel
    private lateinit var docsViewModel : DoctorViewModel
    private lateinit var patsViewModel : PatientViewModel
    private lateinit var vaxApptViewModel : VaxApptViewModel
    private lateinit var therapyApptViewModel : TherapyApptViewModel
    private lateinit var appointEditViewModel: AppointEditViewModel

    private lateinit var appointment : DetailedAppItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding = FragmentEditAppointmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        auth = FirebaseAuth.getInstance()

        //if no user, go back to main menu
        if (auth.currentUser == null) {
            Toast.makeText(activity, "Error: no user detected", Toast.LENGTH_LONG).show()
            activity?.supportFragmentManager?.popBackStack()
        }
        else {
            getViewBindings()
            appointment = appointEditViewModel.getAppt()
            binding.textEAPatName.text = appointment.pName
            date = Date(appointment.date)
            binding.editDatePicker
                .setText("${date?.year}/" +
                        "${date?.month}/" +
                        "${date?.day}")
            binding.editDatePicker.setOnClickListener {
                    val cal = Calendar.getInstance()
                    var mYear = cal.get(Calendar.YEAR)
                    var mMonth = cal.get(Calendar.MONTH)
                    var mDay = cal.get(Calendar.DAY_OF_MONTH)

                    val datePickerDialog = DatePickerDialog(this.requireContext(),
                        DatePickerDialog.OnDateSetListener() { view, year, monthOfYear, dayOfMonth ->
                            date = Date(year,monthOfYear,dayOfMonth)
                            binding.editDatePicker.setText("${year}/${monthOfYear}/${dayOfMonth}")
                        }, mYear,mMonth,mDay)
                    datePickerDialog.show()
                }
            binding.editTimePicker.setText("${date?.hours}:${date?.minutes}")
            binding.editTimePicker.setOnClickListener {
                val cal = Calendar.getInstance()
                var mHour = cal.get(Calendar.HOUR_OF_DAY)
                var mMinute = cal.get(Calendar.MINUTE)
                val timePickerDialogue = TimePickerDialog(this.requireContext(),
                    TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                        date!!.hours = hour
                        date!!.minutes = minute
                        binding.editTimePicker.setText("${hour}:${minute}")
                    },mHour,mMinute,false)
                timePickerDialogue.show()
            }
            binding.editDetails1Edit.setText(appointment.detail1)
            binding.editDetails2Edit.setText(appointment.detail2)
            val docNames = mutableListOf<String>()
            if (appointEditViewModel.getType() == 0) {
                //therapy
                binding.textEAAppType.text = "Therapy"
                binding.editDetails1.text = "Prescription"
                binding.editDetails2.text = "Notes"
                for (docs in docsViewModel.getTherapists())
                    docNames.add("Dr. ${docs.fName} ${docs.lName}")
            }
            else {
                //vax
                binding.textEAAppType.text = "Vaccination"
                binding.editDetails1.text = "Vaccine"
                binding.editDetails2.text = "Allergies"
                for (docs in docsViewModel.getPharmacists())
                    docNames.add("Dr. ${docs.fName} ${docs.lName}")
            }
            val docsAdapter : ArrayAdapter<String> = ArrayAdapter(
                this.requireContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, docNames)
            docsAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item)
            binding.editDocSpinner.adapter = docsAdapter
            var dName = "Dr. ${appointment.dName}"
            binding.editDocSpinner.setSelection(docsAdapter.getPosition(dName))
            binding.editSubmitButt.setOnClickListener { updateAppointment() }
            binding.editDeleteButt.setOnClickListener { deleteAppointment() }
        }
    }

    private fun updateAppointment() {
        val patient = patsViewModel.getPat(appointment.pEmail)

        if (appointEditViewModel.getType() == 0) {
            val doc = docsViewModel.getTherapists()[binding.editDocSpinner.selectedItemPosition]
            val therapyAppointment = TherapyAppointment(appointment.id,"${patient.fName} ${patient.lName}",patient.email,
                "Dr. ${doc.fName} ${doc.lName}",doc.email,date!!,
                binding.editDetails1Edit.text.toString(),binding.editDetails2Edit.text.toString())
            db.collection("therapy_appointments").document(therapyAppointment.taId).set(
                mapOf(
                    "pName" to therapyAppointment.pName,
                    "patient" to therapyAppointment.pEmail,
                    "dName" to therapyAppointment.dName,
                    "doctor" to therapyAppointment.dEmail,
                    "date" to therapyAppointment.date,
                    "prescription" to therapyAppointment.prescription,
                    "notes" to therapyAppointment.notes
            )).addOnCompleteListener {
                if (it.isSuccessful) {
                    therapyApptViewModel.addTherAppt(therapyAppointment)
                    Toast.makeText(activity, "Update successful!", Toast.LENGTH_LONG).show()
                    val appointmentsFrag = AppointmentsFragment()
                    val transaction = activity?.supportFragmentManager?.beginTransaction()
                    transaction?.replace(R.id.displayFrag,appointmentsFrag)?.commit()
                } else
                    Toast.makeText(activity, "Update failed!", Toast.LENGTH_LONG).show()
            }
        }

        else {
            val doc = docsViewModel.getPharmacists()[binding.editDocSpinner.selectedItemPosition]
            val vaxAppointment = VaxAppointment(appointment.id,"${patient.fName} ${patient.lName}",patient.email,
                "Dr. ${doc.fName} ${doc.lName}",doc.email,date!!,
                binding.editDetails2Edit.text.toString(),binding.editDetails1Edit.text.toString())
            db.collection("vax_appointments").document(vaxAppointment.vaId).set (
                mapOf(
                    "pName" to vaxAppointment.pName,
                    "patient" to vaxAppointment.pEmail,
                    "dName" to vaxAppointment.dName,
                    "doctor" to vaxAppointment.dEmail,
                    "date" to vaxAppointment.date,
                    "vaccine" to vaxAppointment.vaccine,
                    "allergies" to vaxAppointment.allergies
                )).addOnCompleteListener {
                if (it.isSuccessful) {
                    vaxApptViewModel.addVaxAppt(vaxAppointment)
                    Toast.makeText(activity, "Update successful!", Toast.LENGTH_LONG).show()
                    val appointmentsFrag = AppointmentsFragment()
                    val transaction = activity?.supportFragmentManager?.beginTransaction()
                    transaction?.replace(R.id.displayFrag,appointmentsFrag)?.commit()
                } else {
                    Toast.makeText(activity, "Update failed!", Toast.LENGTH_LONG).show()
                }
            }
        }

    }
    private fun deleteAppointment() {
        if (appointEditViewModel.getType() == 0) {
            db.collection("therapy_appointments").document(appointment.id)
                .delete().addOnCompleteListener {
                    if (it.isSuccessful) {
                        therapyApptViewModel.deleteTherAppt(appointment.id)
                        activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
                    } else {
                        Toast.makeText(activity, "Update failed!", Toast.LENGTH_LONG).show()
                    }
                }
        }
        else {
            db.collection("vax_appointments").document(appointment.id)
                .delete().addOnCompleteListener {
                    if (it.isSuccessful) {
                        vaxApptViewModel.deleteVaxAppt(appointment.id)
                        activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
                    } else {
                        Toast.makeText(activity, "Update failed!", Toast.LENGTH_LONG).show()
                    }
                }
        }
    }
    private fun getViewBindings() {
        userViewModel = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
        docsViewModel = ViewModelProvider(requireActivity()).get(DoctorViewModel::class.java)
        patsViewModel = ViewModelProvider(requireActivity()).get(PatientViewModel::class.java)
        vaxApptViewModel = ViewModelProvider(requireActivity()).get(VaxApptViewModel::class.java)
        therapyApptViewModel = ViewModelProvider(requireActivity()).get(TherapyApptViewModel::class.java)
        appointEditViewModel = ViewModelProvider(requireActivity()).get(AppointEditViewModel::class.java)
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment EditAppointmentFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            EditAppointmentFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}