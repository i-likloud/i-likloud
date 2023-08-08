package com.ssafy.likloud.ui.nftlist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.likloud.data.api.onError
import com.ssafy.likloud.data.api.onSuccess
import com.ssafy.likloud.data.model.NftGiftDto
import com.ssafy.likloud.data.model.NftListDto
import com.ssafy.likloud.data.repository.BaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "차선호"
@HiltViewModel
class NftListFragmentViewModel @Inject constructor(
    private val baseRepository: BaseRepository
) : ViewModel() {

    private var _myNftDtoList = MutableLiveData<MutableList<NftListDto>>()
    val myNftDtoList: LiveData<MutableList<NftListDto>>
        get() = _myNftDtoList
    fun getMyNftDtoList(){
        viewModelScope.launch {
            baseRepository.getMyNftList().onSuccess {
                _myNftDtoList.value = it
            }
        }
    }

    var isShowSearchUserFragment = false



    private var _giftNftDtoList = MutableLiveData<MutableList<NftGiftDto>>()
    val giftNftDtoList: LiveData<MutableList<NftGiftDto>>
        get() = _giftNftDtoList
    fun getNftGiftList(){
        viewModelScope.launch {
            baseRepository.getNftGiftList().onSuccess {
                _giftNftDtoList.value = it
            }
        }
    }

    lateinit var nftGiftConfimDialog: NftGiftConfimDialog
    fun setNftGiftConfirmDialog(nftGiftDto: NftGiftDto){
        nftGiftConfimDialog = NftGiftConfimDialog(nftGiftDto)
    }

    private var _isAccepted = MutableLiveData<Boolean>()
    val isAccepted: LiveData<Boolean>
        get() = _isAccepted
    fun acceptGift(nftGiftDto: NftGiftDto){
        viewModelScope.launch {
            baseRepository.acceptGift(nftGiftDto.transferId, nftGiftDto.nftId).onSuccess {
                _isAccepted.value = true
            }
        }
    }

    fun rejectGift(nftGiftDto: NftGiftDto){
        viewModelScope.launch {
            baseRepository.rejectGift(nftGiftDto.transferId, nftGiftDto.nftId).onSuccess {
                _isAccepted.value = false
            }
        }
    }
}