package school.cactus.succulentshop.auth.ui

import android.view.View
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import school.cactus.succulentshop.R
import school.cactus.succulentshop.auth.repository.AuthRepository
import school.cactus.succulentshop.auth.viewmodel.LoginViewModel
import school.cactus.succulentshop.base.BaseFragment
import school.cactus.succulentshop.databinding.FragmentLoginBinding
import school.cactus.succulentshop.product.ui.ProductListFragmentDirections
import school.cactus.succulentshop.utils.ViewModelFactory
import school.cactus.succulentshop.utils.data.SharedPrefHelper
import school.cactus.succulentshop.utils.network.AuthorizationErrorResponse
import school.cactus.succulentshop.utils.network.GenericErrorResponse
import school.cactus.succulentshop.utils.validate

class LoginFragment : BaseFragment<FragmentLoginBinding>(R.layout.fragment_login),
    View.OnClickListener {

    private val loginViewModel: LoginViewModel by viewModels {
        ViewModelFactory(AuthRepository())
    }

    override fun FragmentLoginBinding.initializePageConfiguration() {
        setPageConfigurations()
        initializeObservers()
    }

    override fun onClick(view: View?) {
        when (view) {
            binding.createAccountButton -> navController.navigate(R.id.action_loginFragment_to_signUpFragment)
            binding.logInButton -> if (validateLogin()) {
                showLoading()
                sendLoginRequest()
            }
        }
    }

    private fun setPageConfigurations() {
        binding.createAccountButton.setOnClickListener(this)
        binding.logInButton.setOnClickListener(this)
    }

    private fun initializeObservers() {
        with(loginViewModel) {
            success.observe(viewLifecycleOwner, {
                SharedPrefHelper.setJwt(it.jwt)
                val action = ProductListFragmentDirections.actionGlobalProductList()
                hideLoading()
                navController.navigate(action)
            })

            failure.observe(viewLifecycleOwner, {
                hideLoading()
                val message = when (it) {
                    is GenericErrorResponse -> it.getErrorMessage()
                    is AuthorizationErrorResponse -> it.message
                }
                handleSnackBar(
                    message,
                    Snackbar.LENGTH_LONG
                )
            })

            unexpected.observe(viewLifecycleOwner, {
                hideLoading()
                handleSnackBar(
                    getString(it),
                    Snackbar.LENGTH_LONG
                ) { sendLoginRequest() }
            })
        }
    }

    private fun validateLogin() =
        with(binding) { tilUsernameOrEmail.validate(this) and tilPassword.validate(this) }

    private fun sendLoginRequest() = loginViewModel.sendLoginRequest(
        binding.titUsernameOrEmail.text.toString(),
        binding.titPassword.text.toString()
    )
}