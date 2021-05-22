package school.cactus.succulentshop.utils.network

data class Message(val id: String, val message: String)

data class Messages(val messages: List<Message>)

data class GenericErrorResponse(
    override val statusCode: Int,
    override val error: String,
    val message: List<Messages>
) : ErrorResponse()

data class AuthorizationErrorResponse(
    override val statusCode: Int,
    override val error: String,
    val message: String
) : ErrorResponse()

sealed class ErrorResponse {
    abstract val statusCode: Int
    abstract val error: String
}

