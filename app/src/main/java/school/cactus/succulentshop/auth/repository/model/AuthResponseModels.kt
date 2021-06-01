package school.cactus.succulentshop.auth.repository.model

import com.google.gson.annotations.SerializedName

data class AuthResponse(
    val jwt: String,
    val user: User
)

data class User(
    val role: Role,
    val blocked: Boolean?,
    @SerializedName("updated_at")
    val updatedAt: String,
    val provider: String,
    @SerializedName("created_at")
    val createdAt: String,
    val id: Int,
    val confirmed: Boolean,
    val email: String,
    val username: String
)

data class Role(
    val name: String,
    val description: String,
    val id: Int,
    val type: String
)