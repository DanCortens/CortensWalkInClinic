package project.stn991503827.daniel.walkinclinicfinalproject.data

import java.util.Date

data class TherapyAppointment(
    val taId : String,
    val pName: String,
    val pEmail: String,
    val dName: String,
    val dEmail: String,
    val date: Date,
    val prescription: String,
    val notes: String
)
