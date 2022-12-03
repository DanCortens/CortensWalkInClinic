package project.stn991503827.daniel.walkinclinicfinalproject

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.ListFragment
import androidx.fragment.app.viewModels
import project.stn991503827.daniel.walkinclinicfinalproject.viewmodels.UserViewModel

class AdminNavListFrag : ListFragment() {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val navOptions = arrayOf("Home", "Personal", "Patients", "Doctors", "Appointments", "Logout")
        val adapter = ArrayAdapter(requireActivity(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, navOptions)
        listAdapter = adapter
    }

    override fun onListItemClick(l: ListView, v: View, position: Int, id: Long) {
        super.onListItemClick(l, v, position, id)

        (activity as MainActivity).setAdminDisplayFrag(position)

    }
}