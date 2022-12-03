package project.stn991503827.daniel.walkinclinicfinalproject.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import project.stn991503827.daniel.walkinclinicfinalproject.data.Patient

class PatientViewModel : ViewModel() {
    private val mutableDocData = MutableLiveData <MutableMap<String, Patient>>()

    fun setPats(pats : MutableMap<String, Patient>) {
        mutableDocData.value = pats
    }
    fun addPat(patient: Patient) {
        val patList = mutableDocData.value
        patList?.set(patient.email, patient)
        setPats(patList!!)
    }
    fun getPat(email : String) : Patient {
        val patList = mutableDocData.value
        val pat = patList?.get(email)
        return pat!!
    }
    fun getPats() : Map<String, Patient> {
        val patList = mutableDocData.value
        return patList!!
    }

}