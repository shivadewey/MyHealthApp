package com.shiva.myhealth.models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shiva.myhealth.data.Person
import com.shiva.myhealth.room.HealthRoomDb
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val mainDB: HealthRoomDb
) : ViewModel() {
    var person = MutableLiveData<Person>()
    var inSystem = MutableStateFlow(false)
    var registrationDialog = MutableLiveData(false)


    private fun getPerson() = viewModelScope.launch {
        person.value = mainDB.personDao.getPerson()
    }

    private fun setPerson(person: Person) = viewModelScope.launch {
        mainDB.personDao.addPerson(person)
        getPerson()
    }

    fun delPerson(id: Int) = viewModelScope.launch {
        mainDB.personDao.deletePerson(id)
        inSystem.value = false
        showRegDialog()
    }

    init {
        getPerson()
        if (person.value?.name== "") {
            inSystem.value = false
            showRegDialog()
        }
        else {
            registrationDialog.value = false
            inSystem.value = true
        }


    }

    fun showRegDialog(value: Boolean = true) {
        registrationDialog.value = value
    }

    fun setPersonDate(p: Person) {
        this.person.value = p
        setPerson(p)
        inSystem.value = true
        showRegDialog(false)
    }
}