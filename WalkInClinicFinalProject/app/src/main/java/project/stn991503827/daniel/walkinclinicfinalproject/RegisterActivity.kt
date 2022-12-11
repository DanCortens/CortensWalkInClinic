package project.stn991503827.daniel.walkinclinicfinalproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import project.stn991503827.daniel.walkinclinicfinalproject.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding : ActivityRegisterBinding
    private lateinit var auth : FirebaseAuth
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_register)
        auth = FirebaseAuth.getInstance()

        binding.buttConfRegister.setOnClickListener {
            val email = binding.editRegisterName.text.toString()
            val password = binding.editRegisterPassword.text.toString()
            val confPass = binding.editRegisterConfirmPassword.text.toString()

            if (password != confPass) {
                Toast.makeText(this, "Passwords must match", Toast.LENGTH_SHORT).show()
            }
            else if (email.isBlank() || password.isBlank() || confPass.isBlank())
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
            else if (password.length < 8)
                Toast.makeText(this, "Passwords must be at least 8 characters long", Toast.LENGTH_SHORT).show()
            else {
                registerUser(email, password)
            }
        }
    }
    private fun registerUser(email : String, password : String) {
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                val data = hashMapOf("admin" to false)
                db.collection("users").document(email).set(data, SetOptions.merge())
                Toast.makeText(this, "Registration successful", Toast.LENGTH_LONG).show()
                startNewUser()
            }
            else {
                Toast.makeText(this, "Registration failed", Toast.LENGTH_LONG).show()
            }

        }
    }
    private fun startNewUser() {
        val intent = Intent(this, NewPatientActivity::class.java)
        startActivity(intent)
        finish()
    }
}