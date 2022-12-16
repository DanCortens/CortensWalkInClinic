package project.stn991503827.daniel.walkinclinicfinalproject.viewmodels

import androidx.lifecycle.ViewModel
import project.stn991503827.daniel.walkinclinicfinalproject.data.Patient

class PatientEditViewModel : ViewModel() {
    private lateinit var patient: Patient
    fun setPatient(p : Patient) {
        patient = p
    }
    fun getPatient() = patient
}