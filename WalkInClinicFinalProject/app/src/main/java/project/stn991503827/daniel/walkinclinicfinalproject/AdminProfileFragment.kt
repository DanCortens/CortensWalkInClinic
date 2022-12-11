package project.stn991503827.daniel.walkinclinicfinalproject

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.text.set
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import project.stn991503827.daniel.walkinclinicfinalproject.data.Doctor
import project.stn991503827.daniel.walkinclinicfinalproject.databinding.FragmentAdminProfileBinding
import project.stn991503827.daniel.walkinclinicfinalproject.databinding.FragmentDoctorListBinding
import project.stn991503827.daniel.walkinclinicfinalproject.viewmodels.DoctorViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AdminProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AdminProfileFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding : FragmentAdminProfileBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var user : FirebaseUser

    private val db = Firebase.firestore
    private lateinit var docsViewModel : DoctorViewModel

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
        binding = FragmentAdminProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun getViewBindings() {
        docsViewModel = ViewModelProvider(requireActivity()).get(DoctorViewModel::class.java)
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
            var doc = docsViewModel.getDoc(user.email!!)
            binding.editDocProfFirstName.setText(doc.fName)
            binding.editDocProfLastName.setText(doc.lName)
            binding.editDocProfSpec.setText(doc.spec)
            binding.textDocProfEmail.text = doc.email
            binding.editDocProfPhone.setText(doc.phone)
            binding.buttDocProfUpdate.setOnClickListener {
                updateProfile()
            }
        }
    }

    private fun updateProfile() {
        if (binding.editDocProfFirstName.text.toString() == "") {
            Toast.makeText(activity, "Error: First name cannot be empty", Toast.LENGTH_LONG).show()
        }
        else if (binding.editDocProfLastName.text.toString() == "") {
            Toast.makeText(activity, "Error: Last name cannot be empty", Toast.LENGTH_LONG).show()
        }
        else if (binding.editDocProfSpec.text.toString() == "") {
            Toast.makeText(activity, "Error: Specialization cannot be empty", Toast.LENGTH_LONG).show()
        }
        else if (binding.editDocProfSpec.text.toString() != "Therapist" &&
            binding.editDocProfSpec.text.toString() != "Pharmacist") {
            Toast.makeText(activity, "Error: Specialization must be Therapist or Pharmacist", Toast.LENGTH_LONG).show()
        }
        else if (binding.editDocProfPhone.text.toString() == "") {
            Toast.makeText(activity, "Error: Phone cannot be empty", Toast.LENGTH_LONG).show()
        }
        else if (binding.editDocProfPhone.text.toString().length != 12) {
            Toast.makeText(activity, "Error: Phone must be valid (format ###-###-####)", Toast.LENGTH_LONG).show()
        }
        else {
            val newDoc = Doctor(binding.editDocProfFirstName.text.toString(),binding.editDocProfLastName.text.toString(),
                binding.editDocProfSpec.text.toString(),binding.editDocProfPhone.text.toString(),
                binding.textDocProfEmail.text.toString())
            db.collection("doctors").document(newDoc.email)
                .update(mapOf(
                    "fName" to newDoc.fName,
                    "lName" to newDoc.lName,
                    "phone" to newDoc.phone,
                    "spec" to newDoc.spec
                )).addOnCompleteListener {
                    if (it.isSuccessful)
                        Toast.makeText(activity, "Update successful!", Toast.LENGTH_LONG).show()
                    else
                        Toast.makeText(activity, "Update failed!", Toast.LENGTH_LONG).show()
                }
            docsViewModel.updateDoc(newDoc)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AdminProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AdminProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}