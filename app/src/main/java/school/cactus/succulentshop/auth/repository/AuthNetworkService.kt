package school.cactus.succulentshop.auth.repository

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import school.cactus.succulentshop.auth.repository.model.AuthResponse
import school.cactus.succulentshop.auth.repository.model.LoginRequest
import school.cactus.succulentshop.auth.repository.model.RegisterRequest

interface AuthNetworkService {
    @POST("/auth/local")
    fun login(@Body loginRequest: LoginRequest): Call<AuthResponse>

    @POST("/auth/local/register")
    fun register(@Body registerRequest: RegisterRequest): Call<AuthResponse>
}