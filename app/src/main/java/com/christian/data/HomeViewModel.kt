package com.christian.commonlink.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val _membersCount = MutableStateFlow("0")
    val membersCount: StateFlow<String> = _membersCount

    private val _eventsCount = MutableStateFlow("0")
    val eventsCount: StateFlow<String> = _eventsCount

    private val _jobsCount = MutableStateFlow("0")
    val jobsCount: StateFlow<String> = _jobsCount

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val dbRef = FirebaseDatabase.getInstance().reference.child("stats")

    init {
        fetchCountsFromFirebase()
    }

    private fun fetchCountsFromFirebase() {
        _isLoading.value = true

        // ── Members count ────────────────────────────────────────
        dbRef.child("membersCount").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                _membersCount.value = snapshot.getValue(String::class.java) ?: "0"
                _isLoading.value = false
            }
            override fun onCancelled(error: DatabaseError) {
                _isLoading.value = false
            }
        })

        // ── Events count ─────────────────────────────────────────
        dbRef.child("eventsCount").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                _eventsCount.value = snapshot.getValue(String::class.java) ?: "0"
            }
            override fun onCancelled(error: DatabaseError) {}
        })

        // ── Jobs count ───────────────────────────────────────────
        dbRef.child("jobsCount").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                _jobsCount.value = snapshot.getValue(String::class.java) ?: "0"
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    // ── Manual update functions ──────────────────────────────────
    fun updateMembers(count: String) {
        viewModelScope.launch { _membersCount.value = count }
    }

    fun updateEvents(count: String) {
        viewModelScope.launch { _eventsCount.value = count }
    }

    fun updateJobs(count: String) {
        viewModelScope.launch { _jobsCount.value = count }
    }
}