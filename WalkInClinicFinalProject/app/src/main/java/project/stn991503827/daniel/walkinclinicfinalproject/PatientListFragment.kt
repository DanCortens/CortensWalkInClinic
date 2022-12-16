package project.stn991503827.daniel.walkinclinicfinalproject

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import project.stn991503827.daniel.walkinclinicfinalproject.data.PatientItem
import project.stn991503827.daniel.walkinclinicfinalproject.data.PatientItemRecycler
import project.stn991503827.daniel.walkinclinicfinalproject.databinding.FragmentPatientListBinding
import project.stn991503827.daniel.walkinclinicfinalproject.viewmodels.PatientEditViewModel
import project.stn991503827.daniel.walkinclinicfinalproject.viewmodels.PatientViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PatientListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PatientListFragment : Fragment(), PatientItemRecycler.OnItemClickListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding : FragmentPatientListBinding;
    private lateinit var auth : FirebaseAuth
    private lateinit var user : FirebaseUser
    private var patList = mutableListOf<PatientItem>()

    private lateinit var patientViewModel : PatientViewModel
    private lateinit var patientEditViewModel: PatientEditViewModel

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
        binding = FragmentPatientListBinding.inflate(inflater, container, false)
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

            patList.clear()
            for (pats in patientViewModel.getPats().values.toList()) {
                patList.add(PatientItem("${pats.fName} ${pats.lName}","${pats.age}",
                    pats.email,pats.phone,pats.notes))
            }
            binding.rViewPatientList.adapter = PatientItemRecycler(patList, this)
            binding.rViewPatientList.layoutManager = LinearLayoutManager(this.context)
            binding.rViewPatientList.setHasFixedSize(true)
        }
    }

    private fun getViewBindings() {
        patientViewModel = ViewModelProvider(requireActivity()).get(PatientViewModel::class.java)
        patientEditViewModel = ViewModelProvider(requireActivity()).get(PatientEditViewModel::class.java)
    }
    override fun onItemClick(position: Int) {
        patientEditViewModel.setPatient(
            patientViewModel.getPat(patList[position].email)
        )
        val patientProfileFragment = PatientProfileFragment()
        val transaction = activity?.supportFragmentManager?.beginTransaction()
        transaction?.remove(this)
        transaction?.add(R.id.displayFrag,patientProfileFragment)?.commit()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PatientListFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PatientListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}