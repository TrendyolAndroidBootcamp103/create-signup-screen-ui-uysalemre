package school.cactus.succulentshop.auth.repository

import kotlinx.coroutines.Dispatchers
import school.cactus.succulentshop.auth.repository.model.LoginRequest
import school.cactus.succulentshop.auth.repository.model.RegisterRequest
import school.cactus.succulentshop.base.BaseRepository
import school.cactus.succulentshop.utils.network.SucculentNetworkHelper

class AuthRepository : BaseRepository() {

    suspend fun makeLoginRequest(requestBody: LoginRequest) = safeApiCall(Dispatchers.IO) {
        SucculentNetworkHelper.createService(AuthNetworkService::class.java).login(requestBody)
    }

    suspend fun makeSignUpRequest(registerRequest: RegisterRequest) = safeApiCall(Dispatchers.IO) {
        SucculentNetworkHelper.createService(AuthNetworkService::class.java)
            .register(registerRequest)
    }
}