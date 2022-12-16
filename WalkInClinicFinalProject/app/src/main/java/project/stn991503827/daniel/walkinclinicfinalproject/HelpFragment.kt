package project.stn991503827.daniel.walkinclinicfinalproject

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import project.stn991503827.daniel.walkinclinicfinalproject.databinding.FragmentHelpBinding
import project.stn991503827.daniel.walkinclinicfinalproject.viewmodels.UserViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HelpFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HelpFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding : FragmentHelpBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var users : UserViewModel

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
        binding = FragmentHelpBinding.inflate(inflater, container, false)
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
            users = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
            val currUser = users.getUser()
            if (currUser.admin) {
                binding.helpText.text = "Hello administrator!\n\n" +
                        "Home: From this menu, you can see all of your appointments booked" +
                        " along with some basic information for each.\n\n" +
                        "Personal: Here you can view and edit your personal information.\n\n" +
                        "Patients: Here you can view all current patients registered with the system. You can also click on a patient to edit their information, but not their contact info.\n\n" +
                        "Doctors: Here you can view all doctors currently working at Cortens Walk-In Clinic. To add more, please contact your system administrator.\n\n" +
                        "Appointments: Here you can view all booked appointments, as well as update or cancel appointments by clicking on them. You can also book a new appointment for a patient.\n\n" +
                        "Logout: This will log you out of the system."
            }
            else {
                binding.helpText.text = "Hello user!\n\n" +
                        "Home: From this menu, you can see all of your appointments along with " +
                        "some basic information about that appointment.\n\n" +
                        "Book: Here, you can book yourself a new walk in appointment.\n\n" +
                        "Logout: This will log you out of the system."
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
         * @return A new instance of fragment HelpFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HelpFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}