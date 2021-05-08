package school.cactus.succulentshop.auth.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import school.cactus.succulentshop.R
import school.cactus.succulentshop.databinding.FragmentLoginBinding
import school.cactus.succulentshop.utils.validate

class LoginFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
            binding.createAccountButton -> findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
            binding.logInButton -> {
                if (validateLogin()) {
                    // TODO navigate user to product page
                }
            }
        }
    }

    private fun setPageConfigurations() {
        binding.createAccountButton.setOnClickListener(this)
        binding.logInButton.setOnClickListener(this)
    }

    private fun validateLogin() =
        with(binding) { tilUsernameOrEmail.validate(this) and tilPassword.validate(this) }
}