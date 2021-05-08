package school.cactus.succulentshop.auth.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import school.cactus.succulentshop.R
import school.cactus.succulentshop.databinding.FragmentSignupBinding
import school.cactus.succulentshop.utils.validate


class SignUpFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentSignupBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
            binding.btSignUp -> if (validateSignup()) {
                // TODO redirect user
            }
            binding.btAlreadyHaveAccount -> findNavController().popBackStack()
        }
    }

    private fun setPageConfigurations() {
        binding.btSignUp.setOnClickListener(this)
        binding.btAlreadyHaveAccount.setOnClickListener(this)
    }

    private fun validateSignup() = with(binding) {
        tilEmail.validate(this) and tilUsername.validate(this) and tilPassword.validate(this)
    }
}