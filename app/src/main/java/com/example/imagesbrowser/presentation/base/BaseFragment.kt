package com.example.imagesbrowser.presentation.base

import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.imagesbrowser.presentation.main.MainViewModel

open class BaseFragment: Fragment() {

    protected val mainViewModel: MainViewModel by activityViewModels()
}
