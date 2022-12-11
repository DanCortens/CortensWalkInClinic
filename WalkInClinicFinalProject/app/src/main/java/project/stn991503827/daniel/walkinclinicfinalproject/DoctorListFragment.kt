package project.stn991503827.daniel.walkinclinicfinalproject

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import project.stn991503827.daniel.walkinclinicfinalproject.data.AppItemRecycler
import project.stn991503827.daniel.walkinclinicfinalproject.data.DocItem
import project.stn991503827.daniel.walkinclinicfinalproject.data.DocItemRecycler
import project.stn991503827.daniel.walkinclinicfinalproject.databinding.FragmentAdminHomeBinding
import project.stn991503827.daniel.walkinclinicfinalproject.databinding.FragmentDoctorListBinding
import project.stn991503827.daniel.walkinclinicfinalproject.viewmodels.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DoctorListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DoctorListFragment : Fragment(), DocItemRecycler.OnItemClickListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding : FragmentDoctorListBinding;
    private lateinit var auth : FirebaseAuth
    private lateinit var user : FirebaseUser

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
        binding = FragmentDoctorListBinding.inflate(inflater, container, false)
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
            var docList = mutableListOf<DocItem>()
            for (docs in docsViewModel.getDocs().values.toList()) {
                docList.add(DocItem("Dr. ${docs.fName} ${docs.lName}", docs.spec,docs.email,docs.phone))
            }
            binding.rViewDoctorList.adapter = DocItemRecycler(docList, this)
            binding.rViewDoctorList.layoutManager = LinearLayoutManager(this.context)
            binding.rViewDoctorList.setHasFixedSize(true)
        }
    }
    private fun getViewBindings() {
        docsViewModel = ViewModelProvider(requireActivity()).get(DoctorViewModel::class.java)
    }
    override fun onItemClick(position: Int) {
        //open specific appointment
        Toast.makeText(activity, "position $position", Toast.LENGTH_LONG).show()
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DoctorListFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DoctorListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}