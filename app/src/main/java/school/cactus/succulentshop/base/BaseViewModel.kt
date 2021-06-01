package school.cactus.succulentshop.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import school.cactus.succulentshop.utils.network.ErrorResponse
import school.cactus.succulentshop.utils.network.NetworkResult

open class BaseViewModel : ViewModel() {
    protected val _isLoadingVisible: MutableLiveData<Boolean> = MutableLiveData()
    val isLoadingVisible: LiveData<Boolean> get() = _isLoadingVisible

    protected val _failure: MutableLiveData<ErrorResponse> = MutableLiveData()
    val failure: LiveData<ErrorResponse> get() = _failure

    protected val _unexpected: MutableLiveData<Int> = MutableLiveData()
    val unexpected: LiveData<Int> get() = _unexpected

    protected fun <T> parseResponse(success: MutableLiveData<T>, response: NetworkResult<T>) {
        when (response) {
            is NetworkResult.OnSuccess -> {
                success.postValue(response.data)
            }
            is NetworkResult.OnFailure -> {
                _failure.postValue(response.error)
            }
            is NetworkResult.OnUnexpected -> {
                _unexpected.postValue(response.messageId)
            }
        }
    }
}