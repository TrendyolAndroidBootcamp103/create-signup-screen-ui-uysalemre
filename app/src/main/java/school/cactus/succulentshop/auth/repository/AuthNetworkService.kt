package school.cactus.succulentshop.auth.repository

import retrofit2.http.Body
import retrofit2.http.POST
import school.cactus.succulentshop.auth.repository.model.AuthResponse
import school.cactus.succulentshop.auth.repository.model.LoginRequest
import school.cactus.succulentshop.auth.repository.model.RegisterRequest

interface AuthNetworkService {
    @POST("/auth/local")
    suspend fun login(@Body loginRequest: LoginRequest): AuthResponse

    @POST("/auth/local/register")
    suspend fun register(@Body registerRequest: RegisterRequest): AuthResponse
}