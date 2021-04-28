package school.cactus.succulentshop

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import school.cactus.succulentshop.databinding.ActivitySignupBinding
import school.cactus.succulentshop.helpers.validate

class SignupActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setPageConfigurations()
    }

    override fun onClick(view: View?) {
        when (view) {
            binding.btSignUp -> signUpUser()
            binding.btAlreadyHaveAccount -> onBackPressed()
        }
    }

    private fun setPageConfigurations() {
        supportActionBar?.title = getString(R.string.sign_up)
        binding.btSignUp.setOnClickListener(this)
        binding.btAlreadyHaveAccount.setOnClickListener(this)
    }

    private fun signUpUser() {
        with(binding) {
            val isEmailValid = tilEmail.validate(this)
            val isUsernameValid = tilUsername.validate(this)
            val isPasswordValid = tilPassword.validate(this)
            if (isEmailValid && isUsernameValid && isPasswordValid) {
                // TODO handle service response and signup user
                Toast.makeText(this@SignupActivity, "Signup Success", Toast.LENGTH_SHORT).show()
            }
        }
    }
}