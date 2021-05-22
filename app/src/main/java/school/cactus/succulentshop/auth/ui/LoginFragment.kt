package school.cactus.succulentshop.auth.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import school.cactus.succulentshop.MainActivity
import school.cactus.succulentshop.R
import school.cactus.succulentshop.auth.repository.AuthNetworkHelper
import school.cactus.succulentshop.auth.repository.model.AuthResponse
import school.cactus.succulentshop.databinding.FragmentLoginBinding
import school.cactus.succulentshop.product.ui.ProductListFragmentDirections
import school.cactus.succulentshop.utils.data.SharedPrefHelper
import school.cactus.succulentshop.utils.network.AuthorizationErrorResponse
import school.cactus.succulentshop.utils.network.ErrorResponse
import school.cactus.succulentshop.utils.network.GenericErrorResponse
import school.cactus.succulentshop.utils.network.NetworkCallback
import school.cactus.succulentshop.utils.validate

class LoginFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val navController: NavController by lazy {
        findNavController()
    }
    private val activity: MainActivity by lazy {
        requireActivity() as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setPageConfigurations()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onClick(view: View?) {
        when (view) {
            binding.createAccountButton -> navController.navigate(R.id.action_loginFragment_to_signUpFragment)
            binding.logInButton -> if (validateLogin()) sendLoginRequest()
        }
    }

    private fun setPageConfigurations() {
        binding.createAccountButton.setOnClickListener(this)
        binding.logInButton.setOnClickListener(this)
    }

    private fun validateLogin() =
        with(binding) { tilUsernameOrEmail.validate(this) and tilPassword.validate(this) }

    private fun sendLoginRequest() {
        activity.showLoading()
        AuthNetworkHelper.requestLogin(
            binding.titUsernameOrEmail.text.toString(),
            binding.titPassword.text.toString(),
            object : NetworkCallback<AuthResponse> {
                override fun onSuccess(response: AuthResponse) {
                    SharedPrefHelper.getInstance(requireContext()).setJwt(response.jwt)
                    val action = ProductListFragmentDirections.actionGlobalProductList()
                    navController.navigate(action)
                }

                override fun onFailure(response: ErrorResponse) {
                    activity.hideLoading()
                    val message = when (response) {
                        is GenericErrorResponse -> response.message[0].messages[0].message
                        is AuthorizationErrorResponse -> response.message
                    }
                    activity.handleSnackBar(
                        message,
                        Snackbar.LENGTH_LONG
                    )
                }

                override fun onUnexpectedError(resourceId: Int) {
                    activity.hideLoading()
                    activity.handleSnackBar(
                        getString(resourceId),
                        Snackbar.LENGTH_LONG
                    ) { sendLoginRequest() }
                }
            }
        )
    }
}