package project.stn991503827.daniel.walkinclinicfinalproject.data

import android.provider.ContactsContract

data class Patient(
    val fName : String,
    val lName : String,
    val age : Int,
    val email : String,
    val phone : String,
    val notes : String
)
