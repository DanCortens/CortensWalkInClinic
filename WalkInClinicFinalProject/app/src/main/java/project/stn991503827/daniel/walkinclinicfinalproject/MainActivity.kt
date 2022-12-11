package project.stn991503827.daniel.walkinclinicfinalproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import project.stn991503827.daniel.walkinclinicfinalproject.data.*
import project.stn991503827.daniel.walkinclinicfinalproject.databinding.ActivityMainBinding
import project.stn991503827.daniel.walkinclinicfinalproject.viewmodels.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var user : FirebaseUser

    private val db = Firebase.firestore

    private val userViewModel : UserViewModel by viewModels()
    private val docsViewModel : DoctorViewModel by viewModels()
    private val patsViewModel : PatientViewModel by viewModels()
    private val vaxApptViewModel : VaxApptViewModel by viewModels()
    private val therapyApptViewModel : TherapyApptViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        auth = FirebaseAuth.getInstance()
        setUpDB()
        //if no user, go back to main menu
        if (auth.currentUser == null) {
            Toast.makeText(this, "Error: no user detected", Toast.LENGTH_LONG).show()
            logout()

        }
        //set up page
        else {
            user = auth.currentUser!!
            var userData : User
            //search db for user email
            val doc = db.collection("users").document(user.email.toString())
            doc.get()
                .addOnCompleteListener() { result ->
                    if (result.isSuccessful) {
                        if (result != null) {
                            //found, set userData
                            userData = User(user.email.toString(),
                                result.result.data?.getValue("admin") as Boolean
                            )
                        }
                        else {
                            //not found, create new userData
                            Toast.makeText(this, "New user, creating", Toast.LENGTH_SHORT).show()
                            userData = User(user.email.toString(), false)
                            val data = hashMapOf("admin" to false)
                            db.collection("users").document(userData.email).set(data, SetOptions.merge())
                        }
                        //userData set - store value of admin and start fragments
                        userViewModel.setUser(userData)
                        val manager = supportFragmentManager
                        val transaction = manager.beginTransaction()
                        if (userData.admin) {
                            val adminNav = AdminNavListFrag()
                            transaction.add(R.id.navFrag, adminNav)
                            val adminHome = AdminHomeFragment()
                            transaction.replace(R.id.displayFrag, adminHome).commit()
                        }

                        else {
                            val patNav = PatientNavListFrag()
                            transaction.add(R.id.navFrag, patNav)
                            val patHome = PatientHomeFrag()
                            transaction.replace(R.id.displayFrag, patHome).commit()
                            //transaction.replace(R.id.displayFrag, userHomeFragment).commit()
                        }

                    }
                    else {
                        Toast.makeText(this, "Error: could not connect to DB", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
    private fun setUpDB() {
        docsViewModel.setDocs(createDocMap())
        patsViewModel.setPats(createPatMap())
        vaxApptViewModel.setVaxAppt(createVaxApptMap())
        therapyApptViewModel.setTherAppt(createTherApptMap())
    }

    private fun createPatMap(): MutableMap<String, Patient> {
        //create map
        val map = mutableMapOf<String,Patient>()
        //get patient table
        val pats = db.collection("patients")
            .get()
            .addOnCompleteListener {
                //loop through creating patients
                if (it.isSuccessful) {
                    for (document in it.result!!) {
                        val pat = Patient (
                            document.data.getValue("fName").toString(),
                            document.data.getValue("lName").toString(),
                            document.data.getValue("age").toString().toInt(),
                            document.id.toString(),
                            document.data.getValue("phone").toString(),
                            document.data.getValue("notes").toString()
                                )
                        //add patient to map
                        map[pat.email] = pat
                    }
                }
            }
        return map
    }

    private fun createDocMap(): MutableMap<String, Doctor> {
        //create map
        val map = mutableMapOf<String,Doctor>()
        //get doctor table
        val docs = db.collection("doctors")
            .get()
            .addOnCompleteListener {
                //loop through creating docs
                if (it.isSuccessful) {
                    for (document in it.result!!) {
                        val doc = Doctor (
                            document.data.getValue("fName").toString(),
                            document.data.getValue("lName").toString(),
                            document.data.getValue("spec").toString(),
                            document.data.getValue("phone").toString(),
                            document.id.toString()
                                )
                        //add docs to map
                        map[doc.email] = doc
                    }
                }
            }
        return map
    }

    private fun createVaxApptMap() : MutableMap<String,VaxAppointment> {
        val map = mutableMapOf<String,VaxAppointment>()
        val vaxs = db.collection("vax_appointments")
            .get()
            .addOnCompleteListener {
                if(it.isSuccessful) {
                    for (document in it.result!!) {
                        val vaxAppointment = VaxAppointment (
                            document.id.toString(),
                            document.data.getValue("pName").toString(),
                            document.data.getValue("patient").toString(),
                            document.data.getValue("dName").toString(),
                            document.data.getValue("doctor").toString(),
                            (document.data.getValue("date") as Timestamp).toDate(),
                            document.data.getValue("allergies").toString(),
                            document.data.getValue("vaccine").toString()
                                )
                        map[vaxAppointment.vaId] = vaxAppointment
                    }
                }
            }
        return map
    }
    private fun createTherApptMap() : MutableMap<String,TherapyAppointment> {
        val map = mutableMapOf<String,TherapyAppointment>()
        val therps = db.collection("therapy_appointments")
            .get()
            .addOnCompleteListener {
                if(it.isSuccessful) {
                    for (document in it.result!!) {
                        val therapyAppointment = TherapyAppointment (
                            document.id.toString(),
                            document.data.getValue("pName").toString(),
                            document.data.getValue("patient").toString(),
                            document.data.getValue("dName").toString(),
                            document.data.getValue("doctor").toString(),
                            (document.data.getValue("date") as Timestamp).toDate(),
                            document.data.getValue("prescription").toString(),
                            document.data.getValue("notes").toString(),
                                )
                        map[therapyAppointment.taId] = therapyAppointment
                    }
                }
            }
        return map
    }

    private fun logout() {
        if (user != null) {
            Toast.makeText(this, "Signing out...", Toast.LENGTH_SHORT).show()
            auth.signOut()
        }
        val intent = Intent(this, StartActivity::class.java)
        startActivity(intent)
        finish()
    }
    fun setAdminDisplayFrag(choice : Int) {
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        when (choice) {
            0 -> {
                val adminHome = AdminHomeFragment()
                transaction.replace(R.id.displayFrag, adminHome).commit()
            }
            1 -> {
                val adminProfile = AdminProfileFragment()
                transaction.replace(R.id.displayFrag, adminProfile).commit()
            }
            2 -> {
                val patList = PatientListFragment()
                transaction.replace(R.id.displayFrag, patList).commit()
            }
            3 -> {
                val docList = DoctorListFragment()
                transaction.replace(R.id.displayFrag, docList).commit()
            }
            4 -> {
                val appList = AppointmentsFragment()
                transaction.replace(R.id.displayFrag,appList).commit()
            }
            else -> {
                logout()
            }
        }
    }

    fun setPatientDisplayFrag (choice : Int) {
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        when (choice) {
            0 -> {
                val patientHome = PatientHomeFrag()
                transaction.replace(R.id.displayFrag, patientHome).commit()
            }
            1 -> {
                val patientProf = PatientProfileFragment()
                transaction.replace(R.id.displayFrag, patientProf).commit()
            }
            2 -> {
                val bookAppointment = BookAppointmentFragment()
                transaction.replace(R.id.displayFrag, bookAppointment).commit()
            }
            else -> {
                logout()
            }
        }
    }

    override fun onDestroy() {
        auth.signOut()
        super.onDestroy()
    }
}
/*
// Create a new user with a first, middle, and last name
val user = hashMapOf(
        "first" to "Alan",
        "middle" to "Mathison",
        "last" to "Turing",
        "born" to 1912
)

// Add a new document with a generated ID
db.collection("users")
    .add(user)
    .addOnSuccessListener { documentReference ->
        Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
    }
    .addOnFailureListener { e ->
        Log.w(TAG, "Error adding document", e)
    }
 */
/*
db.collection("users")
        .get()
        .addOnSuccessListener { result ->
            for (document in result) {
                Log.d(TAG, "${document.id} => ${document.data}")
            }
        }
        .addOnFailureListener { exception ->
            Log.w(TAG, "Error getting documents.", exception)
        }
 */