package school.cactus.succulentshop.helpers

import androidx.viewbinding.ViewBinding
import com.google.android.material.textfield.TextInputLayout
import school.cactus.succulentshop.databinding.ActivityLoginBinding
import school.cactus.succulentshop.databinding.ActivitySignupBinding

fun TextInputLayout.validate(binding: ViewBinding): Boolean {
    error = validator(binding, editText!!.text.toString())
    isErrorEnabled = error != null
    return !isErrorEnabled
}

private fun TextInputLayout.validator(binding: ViewBinding, content: String): String? {
    val instance: Validators = Validators.getInstance(this.context)
    return when (binding) {
        is ActivityLoginBinding -> {
            when (this) {
                binding.identifierInputLayout -> instance.validateUsernameOrEmail(content)
                binding.passwordInputLayout -> instance.validatePassword(content)
                else -> null
            }
        }
        is ActivitySignupBinding -> {
            when (this) {
                binding.tilUsername -> instance.validateUsername(content)
                binding.tilPassword -> instance.validatePassword(content)
                binding.tilEmail -> instance.validateEmail(content)
                else -> null
            }
        }
        else -> null
    }
}