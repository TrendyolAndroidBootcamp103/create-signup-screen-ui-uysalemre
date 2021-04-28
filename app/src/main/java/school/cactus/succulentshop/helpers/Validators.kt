package school.cactus.succulentshop.helpers

import android.content.Context
import school.cactus.succulentshop.R

class Validators private constructor(private val context: Context) {

    companion object : SingletonHolder<Validators, Context>(::Validators) {
        private const val USERNAME_REGEX = "[a-z0-9_]{3,19}"
        private const val PASSWORD_REGEX = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[\\W]).{8,39})"
        private const val EMAIL_REGEX =
            "[a-z0-9_]{6,49}@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?"
        private const val EMAIL_OR_USERNAME_REGEX = "$USERNAME_REGEX||$EMAIL_REGEX"
    }

    fun validateUsername(field: String): String? {
        val username: String by lazy { context.getString(R.string.username) }
        return when {
            field.isEmpty() -> context.getString(R.string.err_field_is_required, username)
            field.length >= 20 -> context.getString(R.string.err_field_is_long, username)
            field.length <= 2 -> context.getString(R.string.err_field_is_short, username)
            !field.matches(USERNAME_REGEX.toRegex()) -> context.getString(
                R.string.err_username_is_invalid,
                username
            )
            else -> null
        }
    }

    fun validateEmail(field: String): String? {
        val email: String by lazy { context.getString(R.string.email) }
        return when {
            field.isEmpty() -> context.getString(R.string.err_field_is_required, email)
            !field.matches(EMAIL_REGEX.toRegex()) -> context.getString(
                R.string.err_email_is_invalid,
                email
            )
            else -> null
        }
    }

    fun validateUsernameOrEmail(field: String): String? {
        val emailOrUsername: String by lazy { context.getString(R.string.email_or_username) }
        return when {
            field.isEmpty() -> context.getString(R.string.err_field_is_required, emailOrUsername)
            !field.matches(EMAIL_OR_USERNAME_REGEX.toRegex()) -> context.getString(
                R.string.err_email_is_invalid,
                emailOrUsername
            )
            else -> null
        }
    }

    fun validatePassword(field: String): String? {
        val password: String by lazy { context.getString(R.string.password) }
        return when {
            field.isEmpty() -> context.getString(R.string.err_field_is_required, password)
            !field.matches(PASSWORD_REGEX.toRegex()) -> context.getString(
                R.string.err_password_invalid,
                password
            )
            else -> null
        }
    }
}