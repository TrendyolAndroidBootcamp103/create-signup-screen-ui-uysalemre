package school.cactus.succulentshop.utils.network

sealed class NetworkResult<out T> {
    data class OnSuccess<out T>(val data: T) : NetworkResult<T>()
    data class OnFailure(val error: ErrorResponse) : NetworkResult<Nothing>()
    data class OnUnexpected(val messageId: Int) : NetworkResult<Nothing>()
}