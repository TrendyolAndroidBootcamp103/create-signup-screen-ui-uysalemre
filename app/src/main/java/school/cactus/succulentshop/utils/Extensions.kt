package school.cactus.succulentshop.utils

import androidx.viewbinding.ViewBinding
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.GsonBuilder
import okhttp3.ResponseBody
import retrofit2.Response
import school.cactus.succulentshop.R
import school.cactus.succulentshop.databinding.FragmentLoginBinding
import school.cactus.succulentshop.databinding.FragmentSignupBinding
import school.cactus.succulentshop.product.repository.model.SizedImage
import school.cactus.succulentshop.utils.network.AuthorizationErrorResponse
import school.cactus.succulentshop.utils.network.GenericErrorResponse
import school.cactus.succulentshop.utils.network.NetworkCallback
import school.cactus.succulentshop.utils.network.SucculentNetworkHelper

fun TextInputLayout.validate(binding: ViewBinding): Boolean {
    error = validator(binding, editText!!.text.toString())
    isErrorEnabled = error != null
    return !isErrorEnabled
}

private fun TextInputLayout.validator(binding: ViewBinding, content: String): String? {
    return when (binding) {
        is FragmentLoginBinding -> {
            when (this) {
                binding.tilUsernameOrEmail -> validateUsernameOrEmail(context, content)
                binding.tilPassword -> validatePassword(context, content)
                else -> null
            }
        }
        is FragmentSignupBinding -> {
            when (this) {
                binding.tilUsername -> validateUsername(context, content)
                binding.tilPassword -> validatePassword(context, content)
                binding.tilEmail -> validateEmail(context, content)
                else -> null
            }
        }
        else -> null
    }
}

private fun ResponseBody.convertErrorBody(): GenericErrorResponse =
    GsonBuilder().create().fromJson(this.string(), GenericErrorResponse::class.java)

private fun ResponseBody.convertAuthErrorBody(): AuthorizationErrorResponse =
    GsonBuilder().create().fromJson(this.string(), AuthorizationErrorResponse::class.java)

fun <Any> Response<Any>.handleResponse(networkCallback: NetworkCallback<Any>) {
    when (this.code()) {
        in 200..299 -> {
            networkCallback.onSuccess(this.body()!!)
        }
        in 400..499 -> {
            when (val errorBody = this.errorBody()) {
                null -> {
                    networkCallback.onUnexpectedError(R.string.err_http_unknown)
                }
                else -> {
                    when {
                        this.code() == 401 -> networkCallback.onFailure(errorBody.convertAuthErrorBody())
                        else -> networkCallback.onFailure(errorBody.convertErrorBody())
                    }
                }
            }
        }
        in 500..599 -> {
            networkCallback.onUnexpectedError(R.string.err_http_unknown)
        }
    }
}

fun SizedImage.getImageUrl() = SucculentNetworkHelper.BASE_URL + this.url