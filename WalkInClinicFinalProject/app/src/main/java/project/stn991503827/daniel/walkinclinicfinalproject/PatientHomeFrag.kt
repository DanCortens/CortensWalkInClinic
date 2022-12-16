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
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import project.stn991503827.daniel.walkinclinicfinalproject.data.AppItemRecycler
import project.stn991503827.daniel.walkinclinicfinalproject.data.ApptItem
import project.stn991503827.daniel.walkinclinicfinalproject.databinding.FragmentPatientHomeBinding
import project.stn991503827.daniel.walkinclinicfinalproject.viewmodels.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PatientHomeFrag.newInstance] factory method to
 * create an instance of this fragment.
 */
class PatientHomeFrag : Fragment(), AppItemRecycler.OnItemClickListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding : FragmentPatientHomeBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var user : FirebaseUser

    private val db = Firebase.firestore

    private lateinit var userViewModel : UserViewModel
    private lateinit var docsViewModel : DoctorViewModel
    private lateinit var patsViewModel : PatientViewModel
    private lateinit var vaxApptViewModel : VaxApptViewModel
    private lateinit var therapyApptViewModel : TherapyApptViewModel
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
        // Inflate the layout for this fragment
        binding = FragmentPatientHomeBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
        return binding.root
    }
    override fun onStart() {
        super.onStart()
        //if no user, go back to main menu
        if (auth.currentUser == null) {
            Toast.makeText(activity, "Error: no user detected", Toast.LENGTH_LONG).show()
            activity?.supportFragmentManager?.popBackStack()

        }
        else {
            getViewBindings()
            user = auth.currentUser!!
            var appts = mutableListOf<ApptItem>()
            for (tAppts in therapyApptViewModel.getTherAppts().values.toList()) {
                if (tAppts.pEmail == user.email!!)
                    appts.add(ApptItem(tAppts.taId,tAppts.dName,tAppts.dEmail,
                        tAppts.date.toString(),tAppts.notes))
            }
            for (vAppts in vaxApptViewModel.getVaxAppts().values.toList()) {
                if (vAppts.pEmail == user.email!!)
                    appts.add(ApptItem(vAppts.vaId,vAppts.dName,vAppts.dEmail,
                        vAppts.date.toString(),vAppts.vaccine))
            }
            binding.rViewPatientHome.adapter = AppItemRecycler(appts,this)
            binding.rViewPatientHome.layoutManager = LinearLayoutManager(this.context)
            binding.rViewPatientHome.setHasFixedSize(true)
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

    override fun onItemClick(position: Int) {
        //do nothing
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PatientHomeFrag.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PatientHomeFrag().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}