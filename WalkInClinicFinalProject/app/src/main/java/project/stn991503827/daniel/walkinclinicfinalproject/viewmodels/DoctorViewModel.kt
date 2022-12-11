package project.stn991503827.daniel.walkinclinicfinalproject.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import project.stn991503827.daniel.walkinclinicfinalproject.data.Doctor

class DoctorViewModel : ViewModel() {
    private val mutableDocData = MutableLiveData<MutableMap<String, Doctor>>()

    fun setDocs(docs : MutableMap<String, Doctor>){
        mutableDocData.value = docs
    }
    fun addDoc(doctor: Doctor) {
        val docList : MutableMap<String, Doctor>? = mutableDocData.value
        docList?.put(doctor.email, doctor)
        setDocs(docList!!)
    }
    fun getDoc(email : String) : Doctor {
        val docList : MutableMap<String, Doctor>? = mutableDocData.value
        val doc = docList?.get(email)
        return doc!!
    }
    fun getDocs() : Map<String, Doctor> {
        val docList : MutableMap<String, Doctor>? = mutableDocData.value
        return docList!!
    }
    fun getTherapists() : List<Doctor> {
        val docList : MutableMap<String, Doctor>? = mutableDocData.value
        var therList = mutableListOf<Doctor>()
        if (docList != null) {
            for (doc in docList) {
                if (doc.value.spec == "Therapist")
                    therList.add(doc.value)
            }
        }
        return therList
    }
    fun getPharmacists() : List<Doctor> {
        val docList : MutableMap<String, Doctor>? = mutableDocData.value
        var pharmList = mutableListOf<Doctor>()
        if (docList != null) {
            for (doc in docList) {
                if (doc.value.spec == "Pharmacist")
                    pharmList.add(doc.value)
            }
        }
        return pharmList
    }

    fun updateDoc(doctor: Doctor) {
        val docList : MutableMap<String, Doctor>? = mutableDocData.value
        docList?.put(doctor.email, doctor)
    }
}