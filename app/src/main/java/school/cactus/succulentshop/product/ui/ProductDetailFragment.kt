package school.cactus.succulentshop.product.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.snackbar.Snackbar
import school.cactus.succulentshop.MainActivity
import school.cactus.succulentshop.R
import school.cactus.succulentshop.auth.ui.LoginFragmentDirections
import school.cactus.succulentshop.databinding.FragmentProductDetailBinding
import school.cactus.succulentshop.product.repository.ProductNetworkHelper
import school.cactus.succulentshop.product.repository.model.ProductItem
import school.cactus.succulentshop.product.repository.model.RelatedProducts
import school.cactus.succulentshop.product.ui.adapter.ProductListAdapter
import school.cactus.succulentshop.utils.data.SharedPrefHelper
import school.cactus.succulentshop.utils.getImageUrl
import school.cactus.succulentshop.utils.network.AuthorizationErrorResponse
import school.cactus.succulentshop.utils.network.ErrorResponse
import school.cactus.succulentshop.utils.network.GenericErrorResponse
import school.cactus.succulentshop.utils.network.NetworkCallback


class ProductDetailFragment : Fragment() {
    private val args: ProductDetailFragmentArgs by navArgs()
    private var _binding: FragmentProductDetailBinding? = null
    private val binding get() = _binding!!
    private val productAdapter = ProductListAdapter()
    private val activity: MainActivity by lazy {
        requireActivity() as MainActivity
    }
    private val navController: NavController by lazy {
        findNavController()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvRelatedProducts.adapter = productAdapter
        binding.rvRelatedProducts.layoutManager =
            LinearLayoutManager(context, HORIZONTAL, false)
        productAdapter.itemClickListener = {
            val action = ProductDetailFragmentDirections
                .actionProductDetailFragmentSelf(it)
            navController.navigate(action)
        }
        sendDetailScreenRequest()
    }

    private fun handleErrorResponse(response: ErrorResponse) {
        activity.hideLoading()
        when (response) {
            is GenericErrorResponse -> {
                activity.handleSnackBar(
                    response.message[0].messages[0].message,
                    Snackbar.LENGTH_LONG
                )
            }
            is AuthorizationErrorResponse -> {
                SharedPrefHelper.getInstance(requireContext()).setJwt("")
                val action = LoginFragmentDirections.actionGlobalLoginFragment()
                navController.navigate(action)
            }
        }
    }

    private fun handleUnexpectedError(resourceId: Int) {
        activity.hideLoading()
        activity.handleSnackBar(
            getString(resourceId),
            Snackbar.LENGTH_INDEFINITE
        ) { sendDetailScreenRequest() }
    }

    private fun sendDetailScreenRequest() {
        activity.showLoading()
        ProductNetworkHelper.requestProductById(
            args.productId,
            object : NetworkCallback<ProductItem> {
                override fun onSuccess(response: ProductItem) {
                    sendRelatedProductsRequest()
                    with(binding) {
                        titleText.text = response.title
                        descriptionText.text = response.description
                        priceText.text =
                            binding.root.context.getString(
                                R.string.product_price_tag,
                                response.price.toString()
                            )
                        Glide.with(imageView.context)
                            .load(response.image.formats.large.getImageUrl())
                            .placeholder(R.drawable.ic_splash_background)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(imageView)
                    }
                }

                override fun onFailure(response: ErrorResponse) {
                    handleErrorResponse(response)
                }

                override fun onUnexpectedError(resourceId: Int) {
                    handleUnexpectedError(resourceId)
                }
            }
        )
    }

    private fun sendRelatedProductsRequest() {
        ProductNetworkHelper.requestRelatedProducts(args.productId,
            object : NetworkCallback<RelatedProducts> {
                override fun onSuccess(response: RelatedProducts) {
                    when {
                        response.products.isEmpty() -> {
                            binding.tvRelatedProducts.visibility = GONE
                            binding.rvRelatedProducts.visibility = GONE
                        }
                        else -> {
                            productAdapter.isDetail = true
                            productAdapter.submitList(response.products)
                        }
                    }
                    activity.hideLoading()
                }

                override fun onFailure(response: ErrorResponse) {
                    handleErrorResponse(response)
                }

                override fun onUnexpectedError(resourceId: Int) {
                    handleUnexpectedError(resourceId)
                }

            })
    }
}