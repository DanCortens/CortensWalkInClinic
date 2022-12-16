package project.stn991503827.daniel.walkinclinicfinalproject.viewmodels

import androidx.lifecycle.ViewModel
import project.stn991503827.daniel.walkinclinicfinalproject.data.DetailedAppItem

class AppointEditViewModel : ViewModel() {
    private lateinit var appoint : DetailedAppItem
    private var type = 0
    fun setAppt(a : DetailedAppItem, t : Int) {
        appoint = a
        type = t
    }
    fun getAppt() = appoint
    fun getType() = type
}