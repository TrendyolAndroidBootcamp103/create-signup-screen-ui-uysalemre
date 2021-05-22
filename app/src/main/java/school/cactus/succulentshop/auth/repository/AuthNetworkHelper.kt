package school.cactus.succulentshop.auth.repository

import school.cactus.succulentshop.auth.repository.model.AuthResponse
import school.cactus.succulentshop.auth.repository.model.LoginRequest
import school.cactus.succulentshop.auth.repository.model.RegisterRequest
import school.cactus.succulentshop.utils.network.NetworkCallback
import school.cactus.succulentshop.utils.network.SucculentNetworkHelper.createService
import school.cactus.succulentshop.utils.network.SucculentNetworkHelper.handleNetworkCallback

object AuthNetworkHelper {
    fun requestLogin(
        identifier: String,
        password: String,
        networkCallback: NetworkCallback<AuthResponse>
    ) {
        createService(AuthNetworkService::class.java)
            .login(LoginRequest(identifier, password))
            .enqueue(handleNetworkCallback(networkCallback))
    }

    fun requestRegister(
        email: String,
        password: String,
        username: String,
        networkCallback: NetworkCallback<AuthResponse>
    ) {
        createService(AuthNetworkService::class.java)
            .register(RegisterRequest(email, password, username))
            .enqueue(handleNetworkCallback(networkCallback))
    }
}