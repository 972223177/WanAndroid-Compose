package com.ly.chatcompose.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ly.chatcompose.data.ProfileScreenState
import com.ly.chatcompose.data.colleagueProfile
import com.ly.chatcompose.data.meProfile

class ProfileViewModel : ViewModel() {
    private var userId: String = ""
    private val _userData = MutableLiveData<ProfileScreenState>()
    val userData: LiveData<ProfileScreenState> = _userData


    fun setUserId(newUserId: String?) {
        if (newUserId != userId) {
            userId = newUserId ?: meProfile.userId
        }
        _userData.value = if (userId == meProfile.userId || userId == meProfile.displayName) {
            meProfile
        } else {
            colleagueProfile
        }

    }
}