package school.cactus.succulentshop.utils

import androidx.viewbinding.ViewBinding
import com.google.android.material.textfield.TextInputLayout
import school.cactus.succulentshop.databinding.FragmentLoginBinding
import school.cactus.succulentshop.databinding.FragmentSignupBinding
import school.cactus.succulentshop.product.models.Product

fun TextInputLayout.validate(binding: ViewBinding): Boolean {
    error = validator(binding, editText!!.text.toString())
    isErrorEnabled = error != null
    return !isErrorEnabled
}

private fun TextInputLayout.validator(binding: ViewBinding, content: String): String? {
    return when (binding) {
        is FragmentLoginBinding -> {
            when (this) {
                binding.tilUsernameOrEmail -> validateUsernameOrEmail(this.context, content)
                binding.tilPassword -> validatePassword(this.context, content)
                else -> null
            }
        }
        is FragmentSignupBinding -> {
            when (this) {
                binding.tilUsername -> validateUsername(this.context, content)
                binding.tilPassword -> validatePassword(this.context, content)
                binding.tilEmail -> validateEmail(this.context, content)
                else -> null
            }
        }
        else -> null
    }
}

fun List<Product>.findProduct(id: Int): Product? = this.find { it.id == id }

fun List<Product>.relatedProducts(id: Int): List<Product> =
    this.shuffled().filter { it.id != id }.subList(0, 4)