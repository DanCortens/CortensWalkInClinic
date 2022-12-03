package project.stn991503827.daniel.walkinclinicfinalproject.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import project.stn991503827.daniel.walkinclinicfinalproject.data.User

class UserViewModel : ViewModel() {
    private val mutableUserData = MutableLiveData<User>()
    val userData : LiveData<User> get() = mutableUserData
    fun setUser(user : User) {
        mutableUserData.value = user
    }
    fun getUser() : User {
        val user = mutableUserData.value
        return user!!
    }
}