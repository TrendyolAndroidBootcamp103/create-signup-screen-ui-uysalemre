package school.cactus.succulentshop.utils.network

interface NetworkCallback<Any> {
    fun onSuccess(response: Any)

    fun onFailure(response: ErrorResponse)

    fun onUnexpectedError(resourceId: Int)
}