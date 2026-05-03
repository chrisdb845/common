package com.christian.commonlink.ui.screens.events

import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class EventViewModel : ViewModel() {

    private val _events = MutableStateFlow<List<Event>>(emptyList())
    val events: StateFlow<List<Event>> = _events

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    // ── Firebase reference ───────────────────────────────────────
    private val dbRef = FirebaseDatabase.getInstance().reference.child("events")

    init {
        fetchEvents()
    }

    // ── Fetch all events from Firebase in real time ──────────────
    private fun fetchEvents() {
        _isLoading.value = true
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val eventList = mutableListOf<Event>()
                for (child in snapshot.children) {
                    val event = child.getValue(Event::class.java)
                    if (event != null) eventList.add(event)
                }
                _events.value = eventList
                _isLoading.value = false
            }

            override fun onCancelled(error: DatabaseError) {
                _errorMessage.value = error.message
                _isLoading.value = false
            }
        })
    }

    // ── Add a new event to Firebase ──────────────────────────────
    fun addEvent(event: Event) {
        val newRef = dbRef.push()
        val newEvent = event.copy(id = newRef.key ?: "")
        newRef.setValue(newEvent)
    }

    // ── Delete an event from Firebase ────────────────────────────
    fun deleteEvent(eventId: String) {
        dbRef.child(eventId).removeValue()
    }
}