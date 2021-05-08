package school.cactus.succulentshop.product.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager.VERTICAL
import school.cactus.succulentshop.databinding.FragmentProductListBinding
import school.cactus.succulentshop.product.models.productStore

class ProductListFragment : Fragment() {
    private var _binding: FragmentProductListBinding? = null
    private val binding get() = _binding!!
    private val productAdapter = ProductListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProductListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding.rvProducts) {
            layoutManager = StaggeredGridLayoutManager(2, VERTICAL)
            adapter = productAdapter
            productAdapter.isDetail = false
        }

        productAdapter.submitList(productStore)
        productAdapter.itemClickListener = {
            val action = ProductListFragmentDirections.actionProductListToProductDetailFragment(it)
            findNavController().navigate(action)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}