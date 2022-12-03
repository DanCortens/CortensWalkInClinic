package project.stn991503827.daniel.walkinclinicfinalproject

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.R
import androidx.fragment.app.ListFragment

class PatientNavListFrag : ListFragment() {
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val navOptions = arrayOf("Home", "Personal", "Appointments", "Logout")
        val adapter = ArrayAdapter(requireActivity(), R.layout.support_simple_spinner_dropdown_item, navOptions)
        listAdapter = adapter
    }

    override fun onListItemClick(l: ListView, v: View, position: Int, id: Long) {
        super.onListItemClick(l, v, position, id)

        (activity as MainActivity).setPatientDisplayFrag(position)

    }
}