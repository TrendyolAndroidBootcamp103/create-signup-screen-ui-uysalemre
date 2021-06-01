package school.cactus.succulentshop.auth.ui

import android.view.View
import androidx.fragment.app.viewModels
import school.cactus.succulentshop.R
import school.cactus.succulentshop.auth.repository.AuthRepository
import school.cactus.succulentshop.auth.viewmodel.SignUpViewModel
import school.cactus.succulentshop.base.BaseFragment
import school.cactus.succulentshop.databinding.FragmentSignupBinding
import school.cactus.succulentshop.product.ui.ProductListFragmentDirections
import school.cactus.succulentshop.utils.ViewModelFactory
import school.cactus.succulentshop.utils.data.SharedPrefHelper
import school.cactus.succulentshop.utils.network.AuthorizationErrorResponse
import school.cactus.succulentshop.utils.network.GenericErrorResponse
import school.cactus.succulentshop.utils.validate

class SignUpFragment : BaseFragment<FragmentSignupBinding>(R.layout.fragment_signup),
    View.OnClickListener {

    private val signUpViewModel: SignUpViewModel by viewModels {
        ViewModelFactory(AuthRepository())
    }

    override fun FragmentSignupBinding.initializePageConfiguration() {
        setPageConfigurations()
        initializeObservers()
    }

    override fun onClick(view: View?) {
        when (view) {
            binding.btSignUp -> if (validateRegister()) {
                showLoading()
                sendRegisterRequest()
            }
            binding.btAlreadyHaveAccount -> navController.popBackStack()
        }
    }

    private fun setPageConfigurations() {
        binding.btSignUp.setOnClickListener(this)
        binding.btAlreadyHaveAccount.setOnClickListener(this)
    }

    private fun initializeObservers() {
        with(signUpViewModel) {
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
                    com.google.android.material.snackbar.Snackbar.LENGTH_LONG
                )
            })

            unexpected.observe(viewLifecycleOwner, {
                hideLoading()
                handleSnackBar(
                    getString(it),
                    com.google.android.material.snackbar.Snackbar.LENGTH_LONG
                ) { sendRegisterRequest() }
            })
        }
    }

    private fun validateRegister() = with(binding) {
        tilEmail.validate(this) and tilUsername.validate(this) and tilPassword.validate(this)
    }

    private fun sendRegisterRequest() = signUpViewModel.sendSignUpRequest(
        binding.titEmail.text.toString(),
        binding.titUsername.text.toString(),
        binding.titPassword.text.toString()
    )
}