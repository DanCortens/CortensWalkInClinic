package project.stn991503827.daniel.walkinclinicfinalproject.viewmodels

import androidx.lifecycle.ViewModel

class AppointEditViewModel : ViewModel() {
    private lateinit var email : String
    private lateinit var apptId : String
    fun setEmail(e : String) {
        email = e
    }
    fun setAppt(a : String) {
        apptId = a
    }
    fun getEmail() = email
    fun getApptID() = apptId
}