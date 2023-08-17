package com.ssafy.likloud.ui.nftlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.ssafy.likloud.data.repository.BaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NftGiftDialogViewModel @Inject constructor(
    private val baseRepository: BaseRepository
) : ViewModel() {

    private var _message: MutableStateFlow<String> = MutableStateFlow("")
    val massage: StateFlow<String>
        get() = _message.asStateFlow()
    fun setMessage(message: String){
        _message.value = message
    }
}