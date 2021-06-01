package school.cactus.succulentshop.auth.repository.model

data class LoginRequest(
    private val identifier: String,
    private val password: String
)

data class RegisterRequest(
    private val email: String,
    private val password: String,
    private val username: String
)