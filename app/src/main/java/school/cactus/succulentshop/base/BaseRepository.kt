package school.cactus.succulentshop.base

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import school.cactus.succulentshop.R
import school.cactus.succulentshop.utils.convertAuthErrorBody
import school.cactus.succulentshop.utils.convertErrorBody
import school.cactus.succulentshop.utils.network.NetworkResult
import java.io.IOException

abstract class BaseRepository {

    suspend fun <T> safeApiCall(
        dispatcher: CoroutineDispatcher,
        apiCall: suspend () -> T
    ): NetworkResult<T> {
        return withContext(dispatcher) {
            try {
                NetworkResult.OnSuccess(apiCall())
            } catch (throwable: Throwable) {
                when (throwable) {
                    is IOException -> NetworkResult.OnUnexpected(R.string.err_http_internet)
                    is HttpException -> {
                        when (throwable.code()) {
                            401 -> NetworkResult.OnFailure(
                                throwable.response()!!.errorBody()!!.convertAuthErrorBody()
                            )
                            else -> NetworkResult.OnFailure(
                                throwable.response()!!.errorBody()!!.convertErrorBody()
                            )
                        }
                    }
                    else -> NetworkResult.OnUnexpected(R.string.err_http_unknown)
                }
            }
        }
    }
}