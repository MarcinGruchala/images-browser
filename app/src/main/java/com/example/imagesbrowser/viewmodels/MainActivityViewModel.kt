package com.example.imagesbrowser.viewmodels

import androidx.lifecycle.ViewModel
import com.example.imagesbrowser.repository.RepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val repository: RepositoryImpl
) : ViewModel() {
}