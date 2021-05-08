package school.cactus.succulentshop.product.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.HORIZONTAL
import school.cactus.succulentshop.databinding.FragmentProductDetailBinding
import school.cactus.succulentshop.product.list.ProductListAdapter
import school.cactus.succulentshop.product.models.productStore
import school.cactus.succulentshop.utils.findProduct
import school.cactus.succulentshop.utils.relatedProducts


class ProductDetailFragment : Fragment() {
    private val args: ProductDetailFragmentArgs by navArgs()
    private var _binding: FragmentProductDetailBinding? = null
    private val binding get() = _binding!!
    private val productAdapter = ProductListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProductDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val product = productStore.findProduct(args.productId)!!
        productAdapter.isDetail = true
        productAdapter.itemClickListener = {
            val directions = ProductDetailFragmentDirections.actionProductDetailFragmentSelf(it)
            findNavController().navigate(directions)
        }

        with(binding) {
            rvRelatedProducts.adapter = productAdapter
            rvRelatedProducts.layoutManager = LinearLayoutManager(context, HORIZONTAL, false)
            imageView.setImageResource(product.imageUrl)
            titleText.text = product.title
            priceText.text = product.price
            descriptionText.text = product.description
        }

        productAdapter.submitList(productStore.relatedProducts(args.productId))
    }
}