package school.cactus.succulentshop

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import school.cactus.succulentshop.databinding.ActivityLoginBinding
import school.cactus.succulentshop.helpers.validate

class LoginActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setPageConfigurations()
    }

    override fun onClick(view: View?) {
        when (view) {
            binding.createAccountButton -> navigateToSignUp()
            binding.logInButton -> navigateToMain()
        }
    }

    private fun setPageConfigurations() {
        supportActionBar?.title = getString(R.string.log_in)
        binding.createAccountButton.setOnClickListener(this)
        binding.logInButton.setOnClickListener(this)
    }

    private fun navigateToSignUp() {
        startActivity(Intent(this, SignupActivity::class.java))
    }

    private fun navigateToMain() {
        with(binding) {
            val isIdentifierValid = identifierInputLayout.validate(this)
            val isPasswordValid = passwordInputLayout.validate(this)
            if (isIdentifierValid && isPasswordValid) {
                // TODO handle service response and navigate to main activity of succulent shop
                Toast.makeText(this@LoginActivity, "Login Success", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
