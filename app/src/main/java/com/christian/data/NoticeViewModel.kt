package com.christian.commonlink.ui.screens.noticeboard

import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class NoticeViewModel : ViewModel() {

    private val _notices = MutableStateFlow<List<Notice>>(emptyList())
    val notices: StateFlow<List<Notice>> = _notices

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val dbRef = FirebaseDatabase.getInstance().reference.child("notices")

    init { fetchNotices() }

    private fun fetchNotices() {
        _isLoading.value = true
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val noticeList = mutableListOf<Notice>()
                for (child in snapshot.children) {
                    val notice = child.getValue(Notice::class.java)
                    if (notice != null) noticeList.add(notice)
                }
                _notices.value = noticeList
                _isLoading.value = false
            }
            override fun onCancelled(error: DatabaseError) {
                _errorMessage.value = error.message
                _isLoading.value = false
            }
        })
    }

    fun addNotice(notice: Notice) {
        val newRef = dbRef.push()
        val newNotice = notice.copy(id = newRef.key ?: "")
        newRef.setValue(newNotice)
    }

    fun deleteNotice(noticeId: String) {
        dbRef.child(noticeId).removeValue()
    }
}