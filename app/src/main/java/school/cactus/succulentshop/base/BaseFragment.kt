package school.cactus.succulentshop.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import school.cactus.succulentshop.MainActivity
import school.cactus.succulentshop.R
import school.cactus.succulentshop.utils.data.SharedPrefHelper

open class BaseFragment<Binding : ViewDataBinding>(@LayoutRes private val layoutId: Int) :
    Fragment(layoutId) {

    private var _binding: Binding? = null
    val binding get() = _binding!!

    protected val navController: NavController by lazy {
        findNavController()
    }

    protected val activity: MainActivity by lazy {
        requireActivity() as MainActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!SharedPrefHelper.isJwtExist() &&
            (navController.currentDestination?.id != R.id.loginFragment && navController.currentDestination?.id != R.id.signUpFragment)
        ) {
            navController.navigate(R.id.action_global_loginFragment)
            return
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(layoutInflater, layoutId, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.initializeView()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.initializePageConfiguration()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    protected fun handleSnackBar(message: String, length: Int, retry: (() -> Unit)? = null) {
        val snackBar = Snackbar.make(
            binding.root,
            message,
            length
        )
        retry?.let {
            snackBar.setAction(R.string.err_snackbar_retry) { it() }
        }
        snackBar.show()
    }

    protected fun hideLoading() = activity.hideLoading()

    protected fun showLoading() = activity.showLoading()

    open fun Binding.initializeView() {}

    open fun Binding.initializePageConfiguration() {}
}