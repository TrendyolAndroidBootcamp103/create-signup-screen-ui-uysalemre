package school.cactus.succulentshop.product.ui

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS
import androidx.recyclerview.widget.StaggeredGridLayoutManager.VERTICAL
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import school.cactus.succulentshop.R
import school.cactus.succulentshop.auth.ui.LoginFragmentDirections
import school.cactus.succulentshop.base.BaseFragment
import school.cactus.succulentshop.databinding.FragmentProductListBinding
import school.cactus.succulentshop.product.repository.ProductRepository
import school.cactus.succulentshop.product.ui.adapter.ProductListAdapter
import school.cactus.succulentshop.product.viewmodel.ProductListViewModel
import school.cactus.succulentshop.utils.ViewModelFactory
import school.cactus.succulentshop.utils.data.SharedPrefHelper
import school.cactus.succulentshop.utils.network.AuthorizationErrorResponse
import school.cactus.succulentshop.utils.network.GenericErrorResponse

class ProductListFragment :
    BaseFragment<FragmentProductListBinding>(R.layout.fragment_product_list),
    SwipeRefreshLayout.OnRefreshListener {
    private val productAdapter: ProductListAdapter = ProductListAdapter()
    private val productListViewModel: ProductListViewModel by viewModels {
        ViewModelFactory(ProductRepository())
    }

    override fun FragmentProductListBinding.initializePageConfiguration() {
        setHasOptionsMenu(true)
        setAdapterOptions()
        setStaggeredLayoutOptions()
        initializeObservers()
        binding.srRefresh.setOnRefreshListener(this@ProductListFragment)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_options, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_item_logout -> {
                SharedPrefHelper.setJwt("")
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

    private fun initializeObservers() {
        with(productListViewModel) {
            success.observe(viewLifecycleOwner, {
                productAdapter.submitList(it)
                hideLoading()
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
                ) { productListViewModel.sendProductListRequest() }
            })
        }
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

    override fun onRefresh() {
        showLoading()
        productListViewModel.sendProductListRequest()
        binding.srRefresh.isRefreshing = false
    }
}