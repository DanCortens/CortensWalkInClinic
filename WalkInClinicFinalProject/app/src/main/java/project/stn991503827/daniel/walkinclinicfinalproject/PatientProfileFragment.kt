package project.stn991503827.daniel.walkinclinicfinalproject

import android.os.Bundle
import android.text.InputType.TYPE_NULL
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import project.stn991503827.daniel.walkinclinicfinalproject.data.Patient
import project.stn991503827.daniel.walkinclinicfinalproject.databinding.FragmentPatientProfileBinding
import project.stn991503827.daniel.walkinclinicfinalproject.viewmodels.PatientEditViewModel
import project.stn991503827.daniel.walkinclinicfinalproject.viewmodels.PatientViewModel
import project.stn991503827.daniel.walkinclinicfinalproject.viewmodels.UserViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "pEmail"

/**
 * A simple [Fragment] subclass.
 * Use the [PatientProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PatientProfileFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var patEmail : String
    private lateinit var binding : FragmentPatientProfileBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var user : FirebaseUser

    private val db = Firebase.firestore
    private lateinit var patientViewModel : PatientViewModel
    private lateinit var patientEditViewModel: PatientEditViewModel
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPatientProfileBinding.inflate(inflater, container, false)
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
                patEmail = patientEditViewModel.getPatient().email
                binding.editPatProfPhone.inputType = TYPE_NULL
            }
            else {
                patEmail = currUser.email
                binding.editPatProfNotes.visibility = View.INVISIBLE
            }
            val patient = patientViewModel.getPat(patEmail)
            binding.editPatProfFirstName.setText(patient.fName)
            binding.editPatProfLastName.setText(patient.lName)
            binding.editPatProfAge.setText(patient.age.toString())
            binding.textPatProfEmail.text = patient.email
            binding.editPatProfPhone.setText(patient.phone)
            binding.editPatProfNotes.setText(patient.notes)
            binding.buttPatProfUpdate.setOnClickListener {
                updatePatientProfile()
            }
        }
    }
    private fun getViewBindings() {
        userViewModel = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
        patientViewModel = ViewModelProvider(requireActivity()).get(PatientViewModel::class.java)
        patientEditViewModel = ViewModelProvider(requireActivity()).get(PatientEditViewModel::class.java)
    }

    private fun updatePatientProfile() {
        if (binding.editPatProfFirstName.text.toString() == "") {
            Toast.makeText(activity, "Error: First name cannot be empty", Toast.LENGTH_LONG).show()
        }
        else if (binding.editPatProfLastName.text.toString() == "") {
            Toast.makeText(activity, "Error: Last name cannot be empty", Toast.LENGTH_LONG).show()
        }
        else if (binding.editPatProfAge.text.toString() == "") {
            Toast.makeText(activity, "Error: Age cannot be empty", Toast.LENGTH_LONG).show()
        }
        else if (binding.editPatProfPhone.text.toString() == "") {
            Toast.makeText(activity, "Error: Phone cannot be empty", Toast.LENGTH_LONG).show()
        }
        else if (binding.editPatProfPhone.text.toString().length != 12) {
            Toast.makeText(activity, "Error: Phone must be valid (format ###-###-####)", Toast.LENGTH_LONG).show()
        }
        else {
            val newPatient = Patient (binding.editPatProfFirstName.text.toString(),binding.editPatProfLastName.text.toString(),
            binding.editPatProfAge.text.toString().toInt(),binding.textPatProfEmail.text.toString(),binding.editPatProfPhone.text.toString(),
            binding.editPatProfNotes.text.toString())
            db.collection("patients").document(newPatient.email)
                .update(mapOf(
                    "fName" to newPatient.fName,
                    "lName" to newPatient.lName,
                    "age" to newPatient.age,
                    "phone" to newPatient.phone,
                    "notes" to newPatient.notes
                )).addOnCompleteListener {
                    if (it.isSuccessful) {
                        patientViewModel.updatePat(newPatient)
                        Toast.makeText(activity, "Update successful!", Toast.LENGTH_LONG).show()
                        activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
                    }
                    else
                        Toast.makeText(activity, "Update failed!", Toast.LENGTH_LONG).show()
                }

        }
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PatientProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(pEmail: String) =
            PatientProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, pEmail)
                }
            }
    }
}