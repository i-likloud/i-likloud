package com.ssafy.likloud.ui.nftlist

import androidx.lifecycle.ViewModel
import com.ssafy.likloud.data.repository.BaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NftListFragmentViewModel @Inject constructor(
    private val baseRepository: BaseRepository
) : ViewModel() {

}