package project.stn991503827.daniel.walkinclinicfinalproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import project.stn991503827.daniel.walkinclinicfinalproject.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLoginBinding
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        auth = FirebaseAuth.getInstance()
        if (auth.currentUser != null) {
            //user already logged in
            startHome()
        }
        else {
            binding.buttConfLogin.setOnClickListener {
                val email = binding.editLoginName.text.toString()
                val password = binding.editLoginPassword.text.toString()
                if (email.isBlank() || password.isBlank())
                    Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
                else
                    loginUser(email, password)
            }
        }
    }

    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Login successful", Toast.LENGTH_LONG).show()
                startHome()
            }
            else {
                Toast.makeText(this, "Login failed", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun startHome() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}