package project.stn991503827.daniel.walkinclinicfinalproject.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import project.stn991503827.daniel.walkinclinicfinalproject.data.VaxAppointment

class VaxApptViewModel : ViewModel(){
    private val mutableVaxData = MutableLiveData <MutableMap<String, VaxAppointment>>()
    fun setVaxAppt(vaxAppts : MutableMap<String, VaxAppointment>) {
        mutableVaxData.value = vaxAppts
    }
    fun addVaxAppt(vaxAppointment: VaxAppointment) {
        val vaxAppts = mutableVaxData.value
        vaxAppts?.put(vaxAppointment.vaId, vaxAppointment)
        setVaxAppt(vaxAppts!!)
    }
    fun getVaxAppt(vaId : String) : VaxAppointment{
        val vaxAppts = mutableVaxData.value
        val vaxAppt = vaxAppts?.get(vaId)
        return vaxAppt!!
    }
    fun getVaxAppts() : MutableMap<String, VaxAppointment> {
        val vaxAppts = mutableVaxData.value
        return vaxAppts!!
    }
}