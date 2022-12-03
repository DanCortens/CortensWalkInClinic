package project.stn991503827.daniel.walkinclinicfinalproject.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import project.stn991503827.daniel.walkinclinicfinalproject.data.TherapyAppointment

class TherapyApptViewModel : ViewModel(){
    private val mutableTherpData = MutableLiveData <MutableMap<String, TherapyAppointment>>()

    fun setTherAppt(therAppts : MutableMap<String,TherapyAppointment>) {
        mutableTherpData.value = therAppts
    }
    fun addTherAppt(therapyAppointment: TherapyAppointment) {
        val therAppts = mutableTherpData.value
        therAppts?.put(therapyAppointment.taId, therapyAppointment)
        setTherAppt(therAppts!!)
    }
    fun getTherAppt(taId : String) : TherapyAppointment{
        val therAppts = mutableTherpData.value
        val therAppt = therAppts?.get(taId)
        return therAppt!!
    }
    fun getTherAppts() : MutableMap<String, TherapyAppointment> {
        val therAppts = mutableTherpData.value
        return therAppts!!
    }
}