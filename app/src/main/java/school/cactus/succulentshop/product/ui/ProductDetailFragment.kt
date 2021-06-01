package school.cactus.succulentshop.product.ui

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.snackbar.Snackbar
import school.cactus.succulentshop.R
import school.cactus.succulentshop.base.BaseFragment
import school.cactus.succulentshop.databinding.FragmentProductDetailBinding
import school.cactus.succulentshop.product.repository.ProductRepository
import school.cactus.succulentshop.product.ui.adapter.ProductListAdapter
import school.cactus.succulentshop.product.viewmodel.ProductDetailViewModel
import school.cactus.succulentshop.utils.ViewModelFactory
import school.cactus.succulentshop.utils.getImageUrl
import school.cactus.succulentshop.utils.network.AuthorizationErrorResponse
import school.cactus.succulentshop.utils.network.GenericErrorResponse


class ProductDetailFragment :
    BaseFragment<FragmentProductDetailBinding>(R.layout.fragment_product_detail) {
    private val args: ProductDetailFragmentArgs by navArgs()
    private val productAdapter: ProductListAdapter = ProductListAdapter()
    private val productDetailViewModel: ProductDetailViewModel by viewModels {
        ViewModelFactory(ProductRepository())
    }

    override fun FragmentProductDetailBinding.initializePageConfiguration() {
        setAdapterOptions()
        initializeObservers()
        sendRequests()
    }

    private fun setAdapterOptions() {
        binding.rvRelatedProducts.adapter = productAdapter
        binding.rvRelatedProducts.layoutManager =
            LinearLayoutManager(context, HORIZONTAL, false)
        productAdapter.itemClickListener = {
            val action = ProductDetailFragmentDirections
                .actionProductDetailFragmentSelf(it)
            navController.navigate(action)
        }
        productAdapter.isDetail = true
    }

    private fun initializeObservers() {
        with(productDetailViewModel) {
            successRelatedProducts.observe(viewLifecycleOwner, {
                productAdapter.submitList(it.products)
                hideLoading()
            })

            successProductItem.observe(viewLifecycleOwner, {
                with(binding) {
                    titleText.text = it.title
                    descriptionText.text = it.description
                    priceText.text =
                        binding.root.context.getString(
                            R.string.product_price_tag,
                            it.price.toString()
                        )
                    Glide.with(imageView.context)
                        .load(it.image.formats.large.getImageUrl())
                        .placeholder(R.drawable.ic_splash_background)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(imageView)
                }
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
                ) { sendRequests() }
            })
        }
    }

    private fun sendRequests() {
        with(productDetailViewModel) {
            sendProductItemRequest(args.productId)
            sendRelatedProductsRequest(args.productId)
        }
    }
}