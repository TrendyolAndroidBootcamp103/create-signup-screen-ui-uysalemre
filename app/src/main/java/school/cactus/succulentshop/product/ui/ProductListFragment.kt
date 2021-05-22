package school.cactus.succulentshop.product.ui

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS
import androidx.recyclerview.widget.StaggeredGridLayoutManager.VERTICAL
import com.google.android.material.snackbar.Snackbar
import school.cactus.succulentshop.MainActivity
import school.cactus.succulentshop.R
import school.cactus.succulentshop.auth.ui.LoginFragmentDirections
import school.cactus.succulentshop.databinding.FragmentProductListBinding
import school.cactus.succulentshop.product.repository.ProductNetworkHelper
import school.cactus.succulentshop.product.repository.model.ProductItem
import school.cactus.succulentshop.product.ui.adapter.ProductListAdapter
import school.cactus.succulentshop.utils.data.SharedPrefHelper
import school.cactus.succulentshop.utils.network.AuthorizationErrorResponse
import school.cactus.succulentshop.utils.network.ErrorResponse
import school.cactus.succulentshop.utils.network.GenericErrorResponse
import school.cactus.succulentshop.utils.network.NetworkCallback

class ProductListFragment : Fragment() {
    private var _binding: FragmentProductListBinding? = null
    private val binding get() = _binding!!
    private val productAdapter = ProductListAdapter()
    private val activity: MainActivity by lazy {
        requireActivity() as MainActivity
    }
    private val navController: NavController by lazy {
        findNavController()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!SharedPrefHelper.getInstance(requireContext()).isJwtExist()) {
            navController.navigate(R.id.action_global_loginFragment)
            return
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        setAdapterOptions()
        setStaggeredLayoutOptions()
        sendProductListRequest()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_options, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_item_logout -> {
                SharedPrefHelper.getInstance(requireContext()).setJwt("")
                val action = LoginFragmentDirections.actionGlobalLoginFragment()
                navController.navigate(action)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setAdapterOptions() {
        productAdapter.itemClickListener = {
            val action = ProductListFragmentDirections
                .actionProductListToProductDetailFragment(it)
            navController.navigate(action)
        }
        productAdapter.isDetail = false
    }

    private fun setStaggeredLayoutOptions() {
        with(binding.rvProducts) {
            val staggeredGridLayoutManager = StaggeredGridLayoutManager(2, VERTICAL)
            staggeredGridLayoutManager.gapStrategy = GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS
            staggeredGridLayoutManager.invalidateSpanAssignments()
            layoutManager = staggeredGridLayoutManager
            adapter = productAdapter
        }
    }

    private fun sendProductListRequest() {
        activity.showLoading()
        ProductNetworkHelper.requestAllProducts(
            object : NetworkCallback<List<ProductItem>> {
                override fun onSuccess(response: List<ProductItem>) {
                    productAdapter.submitList(response)
                    activity.hideLoading()
                }

                override fun onFailure(response: ErrorResponse) {
                    activity.hideLoading()
                    when (response) {
                        is GenericErrorResponse -> {
                            activity.handleSnackBar(
                                response.message[0].messages[0].message,
                                Snackbar.LENGTH_LONG
                            )
                        }
                        is AuthorizationErrorResponse -> {
                            activity.handleSnackBar(
                                response.message,
                                Snackbar.LENGTH_LONG
                            )
                            SharedPrefHelper.getInstance(requireContext()).setJwt("")
                            val action = LoginFragmentDirections.actionGlobalLoginFragment()
                            navController.navigate(action)
                        }
                    }
                }

                override fun onUnexpectedError(resourceId: Int) {
                    activity.hideLoading()
                    activity.handleSnackBar(
                        getString(resourceId),
                        Snackbar.LENGTH_INDEFINITE
                    ) { sendProductListRequest() }
                }
            }
        )
    }

}