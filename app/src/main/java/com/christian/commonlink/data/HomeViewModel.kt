package com.christian.commonlink.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.christian.commonlink.models.Event

import com.google.firebase.database.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class HomeViewModel : ViewModel() {

    // ── State ───────────────────────────────────────────────
    private val _membersCount = MutableStateFlow("0")
    val membersCount: StateFlow<String> = _membersCount

    private val _eventsCount = MutableStateFlow("0")
    val eventsCount: StateFlow<String> = _eventsCount

    private val _jobsCount = MutableStateFlow("0")
    val jobsCount: StateFlow<String> = _jobsCount

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _events = MutableStateFlow<List<Event>>(emptyList())
    val events: StateFlow<List<Event>> = _events

    // ── Firebase References ────────────────────────────────
    private val statsRef = FirebaseDatabase.getInstance().reference.child("stats")
    private val eventsRef = FirebaseDatabase.getInstance().getReference("events")

    init {
        fetchCountsFromFirebase()
        fetchEventsFromFirebase()
    }

    // ── Fetch Stats ─────────────────────────────────────────
    private fun fetchCountsFromFirebase() {
        _isLoading.value = true

        statsRef.child("membersCount")
            .addValueEventListener(createListener { _membersCount.value = it })

        statsRef.child("eventsCount")
            .addValueEventListener(createListener { _eventsCount.value = it })

        statsRef.child("jobsCount")
            .addValueEventListener(createListener { _jobsCount.value = it })
    }

    // ── Fetch Events ────────────────────────────────────────
    private fun fetchEventsFromFirebase() {
        eventsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                val list = mutableListOf<Event>()

                for (snap in snapshot.children) {
                    val event = snap.getValue(Event::class.java)
                    event?.let { list.add(it) }
                }

                _events.value = list.reversed()
                _eventsCount.value = list.size.toString()
                _isLoading.value = false
            }

            override fun onCancelled(error: DatabaseError) {
                _isLoading.value = false
            }
        })
    }

    // ── Reusable Listener (Cleaner) ─────────────────────────
    private fun createListener(onUpdate: (String) -> Unit)
            = object : ValueEventListener {

        override fun onDataChange(snapshot: DataSnapshot) {
            val value = when (val v = snapshot.value) {
                is String -> v
                is Long -> v.toString()
                is Double -> v.toLong().toString()
                else -> "0"
            }
            onUpdate(value)
            _isLoading.value = false
        }

        override fun onCancelled(error: DatabaseError) {
            _isLoading.value = false
        }
    }

    // ── Manual Updates (unchanged behavior) ─────────────────
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