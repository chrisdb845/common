package com.christian.commonlink.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    // ── Stats counts ────────────────────────────────────────────
    private val _membersCount = MutableStateFlow("0")
    val membersCount: StateFlow<String> = _membersCount

    private val _eventsCount = MutableStateFlow("0")
    val eventsCount: StateFlow<String> = _eventsCount

    private val _jobsCount = MutableStateFlow("0")
    val jobsCount: StateFlow<String> = _jobsCount

    // ── Loading state ────────────────────────────────────────────
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        // Later: replace this with your Firebase fetch
        loadMockCounts()
    }

    // ── Mock data — replace with Firebase later ──────────────────
    private fun loadMockCounts() {
        viewModelScope.launch {
            // Simulates a network call
            _membersCount.value = "1.2K"
            _eventsCount.value  = "38"
            _jobsCount.value    = "94"
            _isLoading.value    = false
        }
    }

    // ── Update functions — call these after Firebase writes ──────
    fun updateMembers(count: String) {
        viewModelScope.launch { _membersCount.value = count }
    }

    fun updateEvents(count: String) {
        viewModelScope.launch { _eventsCount.value = count }
    }

    fun updateJobs(count: String) {
        viewModelScope.launch { _jobsCount.value = count }
    }

    // ── When you connect Firebase, replace loadMockCounts() ──────
    // with this pattern:
    //
    // fun fetchCountsFromFirebase() {
    //     val db = FirebaseDatabase.getInstance().reference
    //
    //     db.child("stats/membersCount").addValueEventListener(
    //         object : ValueEventListener {
    //             override fun onDataChange(snapshot: DataSnapshot) {
    //                 _membersCount.value = snapshot.getValue(String::class.java) ?: "0"
    //                 _isLoading.value = false
    //             }
    //             override fun onCancelled(error: DatabaseError) {
    //                 _isLoading.value = false
    //             }
    //         }
    //     )
    //
    //     db.child("stats/eventsCount").addValueEventListener(...)
    //     db.child("stats/jobsCount").addValueEventListener(...)
    // }
}