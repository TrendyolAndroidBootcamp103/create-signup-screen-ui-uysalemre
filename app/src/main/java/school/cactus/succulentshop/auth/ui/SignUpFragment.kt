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
import school.cactus.succulentshop.auth.repository.AuthNetworkHelper
import school.cactus.succulentshop.auth.repository.model.AuthResponse
import school.cactus.succulentshop.databinding.FragmentSignupBinding
import school.cactus.succulentshop.product.ui.ProductDetailFragmentDirections
import school.cactus.succulentshop.utils.data.SharedPrefHelper
import school.cactus.succulentshop.utils.network.AuthorizationErrorResponse
import school.cactus.succulentshop.utils.network.ErrorResponse
import school.cactus.succulentshop.utils.network.GenericErrorResponse
import school.cactus.succulentshop.utils.network.NetworkCallback
import school.cactus.succulentshop.utils.validate

class SignUpFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentSignupBinding? = null
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
        _binding = FragmentSignupBinding.inflate(inflater, container, false)
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
            binding.btSignUp -> if (validateRegister()) requestRegister()
            binding.btAlreadyHaveAccount -> navController.popBackStack()
        }
    }

    private fun setPageConfigurations() {
        binding.btSignUp.setOnClickListener(this)
        binding.btAlreadyHaveAccount.setOnClickListener(this)
    }

    private fun validateRegister() = with(binding) {
        tilEmail.validate(this) and tilUsername.validate(this) and tilPassword.validate(this)
    }

    private fun requestRegister() {
        activity.showLoading()
        AuthNetworkHelper.requestRegister(
            binding.titEmail.text.toString(),
            binding.titPassword.text.toString(),
            binding.titUsername.text.toString(),
            object : NetworkCallback<AuthResponse> {
                override fun onSuccess(response: AuthResponse) {
                    SharedPrefHelper.getInstance(requireContext()).setJwt(response.jwt)
                    val action = ProductDetailFragmentDirections.actionGlobalProductList()
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
                    ) { requestRegister() }
                }
            }
        )
    }
}