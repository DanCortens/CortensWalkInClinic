package project.stn991503827.daniel.walkinclinicfinalproject

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.get
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import project.stn991503827.daniel.walkinclinicfinalproject.data.DetailedAppItem
import project.stn991503827.daniel.walkinclinicfinalproject.data.DetailedAppRecycler
import project.stn991503827.daniel.walkinclinicfinalproject.databinding.DetailedAppointmentItemBinding
import project.stn991503827.daniel.walkinclinicfinalproject.databinding.FragmentAppointmentsBinding
import project.stn991503827.daniel.walkinclinicfinalproject.viewmodels.AppointEditViewModel
import project.stn991503827.daniel.walkinclinicfinalproject.viewmodels.TherapyApptViewModel
import project.stn991503827.daniel.walkinclinicfinalproject.viewmodels.VaxApptViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AppointmentsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AppointmentsFragment : Fragment(), DetailedAppRecycler.OnItemClickListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding : FragmentAppointmentsBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var user : FirebaseUser

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
        binding = FragmentAppointmentsBinding.inflate(inflater, container, false)
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
            var allAppointments = mutableListOf<DetailedAppItem>()
            for (vApps in vaxApptViewModel.getVaxAppts().values.toList()) {
                allAppointments.add(DetailedAppItem(vApps.vaId,vApps.pName,vApps.pEmail,vApps.dName,
                    vApps.dEmail,vApps.date.toString(),vApps.vaccine,vApps.allergies))
            }
            for (tApps in therapyApptViewModel.getTherAppts().values.toList()) {
                allAppointments.add(DetailedAppItem(tApps.taId,tApps.pName,tApps.pEmail,tApps.dName,
                    tApps.dEmail,tApps.date.toString(),tApps.prescription,tApps.notes))
            }
            binding.rViewApptList.adapter = DetailedAppRecycler(allAppointments, this)
            binding.rViewApptList.layoutManager = LinearLayoutManager(this.context)
            binding.rViewApptList.setHasFixedSize(true)
        }
    }
    private fun getViewBindings() {
        vaxApptViewModel = ViewModelProvider(requireActivity()).get(VaxApptViewModel::class.java)
        therapyApptViewModel = ViewModelProvider(requireActivity()).get(TherapyApptViewModel::class.java)
        appointEditViewModel = ViewModelProvider(requireActivity()).get(AppointEditViewModel::class.java)
    }
    override fun onItemClick(position: Int) {
        //open specific appointment

        val card = binding.rViewApptList.get(position)
        appointEditViewModel.setEmail(
            card.findViewById<TextView>(R.id.textAppListPatientEmail).text.toString()
        )

        appointEditViewModel.setAppt(
            card.findViewById<TextView>(R.id.detAppId).text.toString()
        )
        Toast.makeText(activity,"${appointEditViewModel.getApptID()}",Toast.LENGTH_SHORT).show()
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AppointmentsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AppointmentsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}