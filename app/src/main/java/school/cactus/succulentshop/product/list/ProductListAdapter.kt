package school.cactus.succulentshop.product.list

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import school.cactus.succulentshop.R
import school.cactus.succulentshop.databinding.ItemProductListBinding
import school.cactus.succulentshop.product.models.Product
import kotlin.math.roundToInt


class ProductListAdapter :
    ListAdapter<Product, ProductListAdapter.ProductHolder>(DIFF_CALLBACK) {

    var itemClickListener: (Int) -> Unit = {}
    var isDetail: Boolean = false

    companion object {

        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Product>() {
            override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean =
                oldItem == newItem
        }

        fun from(
            parent: ViewGroup,
            itemClickListener: (Int) -> Unit,
            isDetail: Boolean
        ): ProductHolder {
            val binding = ItemProductListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return ProductHolder(binding, itemClickListener, isDetail)
        }
    }


    class ProductHolder(
        private val binding: ItemProductListBinding,
        private val itemClickListener: (Int) -> Unit,
        private val isDetail: Boolean
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product) {
            binding.tvProductTitle.text = product.title
            binding.tvProductPrice.text = product.price

            if (isDetail) {
                binding.ivProductImage.layoutParams.height =
                    binding.root.context.resources.getDimension(R.dimen.detail_iv_height).toInt()
                binding.ivProductImage.layoutParams.width =
                    binding.root.context.resources.getDimension(R.dimen.detail_iv_width).toInt()
                binding.tvProductTitle.maxLines = 1
                val params: LinearLayout.LayoutParams =
                    LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
                params.setMargins(
                    0, 0,
                    (16 * binding.root.context.resources.displayMetrics.density).roundToInt(), 0
                )
                binding.root.layoutParams = params
            }

            Glide.with(binding.root.context)
                .load(product.imageUrl)
                .override(512)
                .into(binding.ivProductImage)

            binding.root.setOnClickListener {
                itemClickListener(product.id)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductHolder =
        from(parent, itemClickListener, isDetail)

    override fun onBindViewHolder(holder: ProductHolder, position: Int) =
        holder.bind(getItem(position))
}