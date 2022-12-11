package project.stn991503827.daniel.walkinclinicfinalproject

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import project.stn991503827.daniel.walkinclinicfinalproject.data.Patient
import project.stn991503827.daniel.walkinclinicfinalproject.data.TherapyAppointment
import project.stn991503827.daniel.walkinclinicfinalproject.data.VaxAppointment
import project.stn991503827.daniel.walkinclinicfinalproject.databinding.FragmentBookAppointmentBinding
import project.stn991503827.daniel.walkinclinicfinalproject.viewmodels.*
import java.util.Date

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BookAppointmentFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BookAppointmentFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var date : Date? = null
    private lateinit var binding : FragmentBookAppointmentBinding

    private lateinit var auth : FirebaseAuth
    private lateinit var user : FirebaseUser
    private val db = Firebase.firestore

    private lateinit var docsViewModel : DoctorViewModel
    private lateinit var patsViewModel : PatientViewModel
    private lateinit var userViewModel: UserViewModel
    private lateinit var therapyApptViewModel: TherapyApptViewModel
    private lateinit var vaxApptViewModel: VaxApptViewModel
    private lateinit var appointEditViewModel: AppointEditViewModel

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
        binding = FragmentBookAppointmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        auth = FirebaseAuth.getInstance()

        //if no user, go back to main menu
        if (auth.currentUser == null) {
            Toast.makeText(activity, "Error: no user detected", Toast.LENGTH_LONG).show()
            activity?.supportFragmentManager?.popBackStack()
        }
        else {
            getViewBindings()
            user = auth.currentUser!!
            val currUser = userViewModel.getUser()
            if (currUser.admin) {
                binding.bookPatientRow.visibility = View.VISIBLE

                val patientNames = mutableListOf<String>()
                for (patients in patsViewModel.getPats().values.toList())
                    patientNames.add("${patients.fName} ${patients.lName}")
                val patientAdapter : ArrayAdapter<String> = ArrayAdapter(
                    this.requireContext(),android.R.layout.simple_spinner_item,patientNames)
                patientAdapter.setDropDownViewResource(
                    android.R.layout.simple_spinner_dropdown_item)
                binding.bookPatientSpinner.adapter = patientAdapter
            }
            val apptTypes = arrayOf<String>("Therapy", "Vaccine")
            val typesAdapter : ArrayAdapter<String> = ArrayAdapter(
                this.requireContext(),android.R.layout.simple_spinner_item,apptTypes)
            typesAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item)
            binding.bookTypeSpinner.adapter = typesAdapter
            binding.bookTypeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    apptTypeSelected(position)
                }

                override fun onNothingSelected(p: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }

            }
        }
    }

    private fun apptTypeSelected(position : Int) {
        binding.bookDoctorRow.visibility = View.VISIBLE
        val docNames = mutableListOf<String>()
        if (position == 0) {
            for (docs in docsViewModel.getTherapists())
                docNames.add("Dr. ${docs.fName} ${docs.lName}")
            binding.bookSubmitButt.setOnClickListener {
                createTherapyAppt()
            }
        }
        else {
            for (docs in docsViewModel.getPharmacists())
                docNames.add("Dr. ${docs.fName} ${docs.lName}")
            binding.bookSubmitButt.setOnClickListener {
                createVaxAppt()
            }
        }
        val docsAdapter : ArrayAdapter<String> = ArrayAdapter(
            this.requireContext(), android.R.layout.simple_spinner_item, docNames)
        docsAdapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item)
        binding.bookDocSpinner.adapter = docsAdapter
        binding.bookDocSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p: AdapterView<*>?, view: View?, position: Int, id: Long) {
                docSelected()
            }

            override fun onNothingSelected(p: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }
    }
    private fun docSelected() {
        binding.bookDateRow.visibility = View.VISIBLE
        binding.bookDatePicker.setOnClickListener {
            val cal = Calendar.getInstance()
            var mYear = cal.get(Calendar.YEAR)
            var mMonth = cal.get(Calendar.MONTH)
            var mDay = cal.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(this.requireContext(),
                DatePickerDialog.OnDateSetListener() { view, year, monthOfYear, dayOfMonth ->
                    date = Date(year,monthOfYear,dayOfMonth)
                    binding.bookDatePicker.setText("${year}/${monthOfYear}/${dayOfMonth}")
                    dateSelected()
            }, mYear,mMonth,mDay)
            datePickerDialog.show()
        }
    }
    private fun dateSelected() {
        binding.bookTimeRow.visibility = View.VISIBLE
        binding.bookTimePicker.setOnClickListener {
            val cal = Calendar.getInstance()
            var mHour = cal.get(Calendar.HOUR_OF_DAY)
            var mMinute = cal.get(Calendar.MINUTE)
            val timePickerDialogue = TimePickerDialog(this.requireContext(),
                TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                    date!!.hours = hour
                    date!!.minutes = minute
                    binding.bookTimePicker.setText("${hour}:${minute}")
                    timeSelected()
                },mHour,mMinute,false)
            timePickerDialogue.show()
        }
    }
    private fun timeSelected() {
        if (binding.bookTypeSpinner.selectedItemPosition == 0 && userViewModel.getUser().admin) {
            binding.bookPrescriptionRow.visibility = View.VISIBLE
            binding.bookNotesRow.visibility = View.VISIBLE
        }
        else if (binding.bookTypeSpinner.selectedItemPosition == 1) {
            binding.bookVaccineRow.visibility = View.VISIBLE
            binding.bookAllergiesRow.visibility = View.VISIBLE
        }
        binding.bookSubmitRow.visibility = View.VISIBLE
    }
    private fun createTherapyAppt() {
        val patient : Patient
        if (userViewModel.getUser().admin)
            patient = patsViewModel.getPats().values.toList()[binding.bookPatientSpinner.selectedItemPosition]
        else
            patient = patsViewModel.getPat(userViewModel.getUser().email)
        val doc = docsViewModel.getTherapists()[binding.bookDocSpinner.selectedItemPosition]
        val therapyAppointment = TherapyAppointment("temp","${patient.fName} ${patient.lName}",patient.email,
            "Dr. ${doc.fName} ${doc.lName}",doc.email,date!!,
            binding.bookPrescriptionEdit.text.toString(),binding.bookNotesEdit.text.toString())
        db.collection("therapy_appointments").add(
            mapOf(
                "pName" to therapyAppointment.pName,
                "patient" to therapyAppointment.pEmail,
                "dName" to therapyAppointment.dName,
                "doctor" to therapyAppointment.dEmail,
                "date" to therapyAppointment.date,
                "prescription" to therapyAppointment.prescription,
                "notes" to therapyAppointment.notes
            )
        ).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(activity, "Update successful!", Toast.LENGTH_LONG).show()
                activity?.supportFragmentManager?.popBackStack()
            }
            else
                Toast.makeText(activity, "Update failed!", Toast.LENGTH_LONG).show()
        }

    }
    private fun createVaxAppt() {
        val patient : Patient
        if (userViewModel.getUser().admin)
            patient = patsViewModel.getPats().values.toList()[binding.bookPatientSpinner.selectedItemPosition]
        else
            patient = patsViewModel.getPat(userViewModel.getUser().email)
        val doc = docsViewModel.getPharmacists()[binding.bookDocSpinner.selectedItemPosition]
        val vaxAppt = VaxAppointment("temp","${patient.fName} ${patient.lName}",patient.email,
            "Dr. ${doc.fName} ${doc.lName}",doc.email,date!!,
            binding.bookVaccineEdit.text.toString(),binding.bookAllergiesEdit.text.toString())
        db.collection("vax_appointments").add(
            mapOf(
                "pName" to vaxAppt.pName,
                "patient" to vaxAppt.pEmail,
                "dName" to vaxAppt.dName,
                "doctor" to vaxAppt.dEmail,
                "date" to vaxAppt.date,
                "vaccince" to vaxAppt.vaccine,
                "allergies" to vaxAppt.allergies
            )
        ).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(activity, "Update successful!", Toast.LENGTH_LONG).show()
                activity?.supportFragmentManager?.popBackStack()
            }
            else
                Toast.makeText(activity, "Update failed!", Toast.LENGTH_LONG).show()
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
         * @return A new instance of fragment BookAppointmentFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BookAppointmentFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}