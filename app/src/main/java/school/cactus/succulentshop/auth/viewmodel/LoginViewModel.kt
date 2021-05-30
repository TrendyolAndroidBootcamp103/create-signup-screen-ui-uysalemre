package school.cactus.succulentshop.auth.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import school.cactus.succulentshop.auth.repository.AuthRepository
import school.cactus.succulentshop.auth.repository.model.AuthResponse
import school.cactus.succulentshop.auth.repository.model.LoginRequest
import school.cactus.succulentshop.base.BaseViewModel


class LoginViewModel(private val authRepository: AuthRepository) : BaseViewModel() {

    private val _success: MutableLiveData<AuthResponse> = MutableLiveData()
    val success: LiveData<AuthResponse> get() = _success

    fun sendLoginRequest(identifier: String, password: String) {
        viewModelScope.launch {
            parseResponse(
                _success,
                authRepository.makeLoginRequest(LoginRequest(identifier, password))
            )
        }
    }
}