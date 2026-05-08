package com.christian.commonlink.ui.screens.jobs  // ✅ Fixed package

import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class JobViewModel : ViewModel() {

    private val _jobs = MutableStateFlow<List<Job>>(emptyList())
    val jobs: StateFlow<List<Job>> = _jobs

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val dbRef = FirebaseDatabase.getInstance().reference.child("jobs")

    init { fetchJobs() }

    private fun fetchJobs() {
        _isLoading.value = true
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val jobList = mutableListOf<Job>()
                for (child in snapshot.children) {
                    val job = child.getValue(Job::class.java)
                    if (job != null) jobList.add(job)
                }
                _jobs.value = jobList
                _isLoading.value = false
            }
            override fun onCancelled(error: DatabaseError) {
                _errorMessage.value = error.message
                _isLoading.value = false
            }
        })
    }

    fun addJob(job: Job) {
        val newRef = dbRef.push()
        val newJob = job.copy(id = newRef.key ?: "")
        newRef.setValue(newJob)
    }

    fun deleteJob(jobId: String) {
        dbRef.child(jobId).removeValue()
    }
}  // ✅ Only one closing brace