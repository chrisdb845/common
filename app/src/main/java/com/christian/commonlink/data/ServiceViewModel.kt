package com.christian.commonlink.ui.screens.services

import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ServiceViewModel : ViewModel() {

    private val _services = MutableStateFlow<List<Service>>(emptyList())
    val services: StateFlow<List<Service>> = _services

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val dbRef = FirebaseDatabase.getInstance().reference.child("services")

    init { fetchServices() }

    private fun fetchServices() {
        _isLoading.value = true
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val serviceList = mutableListOf<Service>()
                for (child in snapshot.children) {
                    val service = child.getValue(Service::class.java)
                    if (service != null) serviceList.add(service)
                }
                _services.value = serviceList
                _isLoading.value = false
            }
            override fun onCancelled(error: DatabaseError) {
                _errorMessage.value = error.message
                _isLoading.value = false
            }
        })
    }

    fun addService(service: Service) {
        val newRef = dbRef.push()
        val newService = service.copy(id = newRef.key ?: "")
        newRef.setValue(newService)
    }

    fun deleteService(serviceId: String) {
        dbRef.child(serviceId).removeValue()
    }
}