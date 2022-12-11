package project.stn991503827.daniel.walkinclinicfinalproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import project.stn991503827.daniel.walkinclinicfinalproject.data.Patient
import project.stn991503827.daniel.walkinclinicfinalproject.databinding.ActivityNewPatientBinding

class NewPatientActivity : AppCompatActivity() {
    private lateinit var binding : ActivityNewPatientBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var user : FirebaseUser

    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_new_patient)
        auth = FirebaseAuth.getInstance()
        binding.buttNewPatCreate.setOnClickListener {
            createPatient()
        }

    }
    private fun createPatient() {
        if (binding.editNewPatFirst.text.toString() == "") {
            Toast.makeText(this, "Error: First name cannot be empty", Toast.LENGTH_LONG).show()
        }
        else if (binding.editNewPatLast.text.toString() == "") {
            Toast.makeText(this, "Error: Last name cannot be empty", Toast.LENGTH_LONG).show()
        }
        else if (binding.editNewPatAge.text.toString() == "") {
            Toast.makeText(this, "Error: Age cannot be empty", Toast.LENGTH_LONG).show()
        }
        else if (binding.editNewPatPhone.text.toString() == "") {
            Toast.makeText(this, "Error: Phone cannot be empty", Toast.LENGTH_LONG).show()
        }
        else if (binding.editNewPatPhone.text.toString().length != 12) {
            Toast.makeText(this, "Error: Phone must be valid (format ###-###-####)", Toast.LENGTH_LONG).show()
        }
        else {
            val newPatient = Patient (binding.editNewPatFirst.text.toString(),binding.editNewPatLast.text.toString(),
                binding.editNewPatAge.text.toString().toInt(), auth.currentUser!!.email.toString(),
                binding.editNewPatPhone.text.toString(), "")
            db.collection("patients").document(newPatient.email)
                .set(mapOf(
                    "fName" to newPatient.fName,
                    "lName" to newPatient.lName,
                    "age" to newPatient.age,
                    "phone" to newPatient.phone,
                    "notes" to newPatient.notes
                )).addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(this, "Welcome!", Toast.LENGTH_LONG).show()
                        startHome()
                    }
                    else
                        Toast.makeText(this, "Something went wrong!", Toast.LENGTH_LONG).show()
                }
        }
    }
    private fun startHome() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}