package com.example.imagesbrowser.presentation.base

import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.imagesbrowser.presentation.main.MainActivityViewModel

open class BaseFragment: Fragment() {

    protected val viewModel: MainActivityViewModel by activityViewModels()
}
