package school.cactus.succulentshop.product.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import school.cactus.succulentshop.R
import school.cactus.succulentshop.databinding.ItemProductListBinding
import school.cactus.succulentshop.product.repository.model.ProductItem
import school.cactus.succulentshop.utils.getImageUrl
import kotlin.math.roundToInt


class ProductListAdapter :
    ListAdapter<ProductItem, ProductListAdapter.ProductHolder>(DIFF_CALLBACK) {

    var itemClickListener: (Int) -> Unit = {}
    var isDetail: Boolean = false

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ProductItem>() {
            override fun areItemsTheSame(oldItem: ProductItem, newItem: ProductItem): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: ProductItem, newItem: ProductItem): Boolean =
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

        fun bind(product: ProductItem) {
            binding.tvProductTitle.text = product.title
            binding.tvProductPrice.text =
                binding.root.context.getString(R.string.product_price_tag, product.price.toString())

            if (isDetail) {
                binding.ivProductImage.layoutParams.height =
                    binding.root.context.resources.getDimension(R.dimen.detail_iv_height).toInt()
                binding.ivProductImage.layoutParams.width =
                    binding.root.context.resources.getDimension(R.dimen.detail_iv_width).toInt()
                binding.ivProductImage.scaleType = ImageView.ScaleType.CENTER_CROP
                binding.tvProductTitle.maxLines = 1
                val params: LinearLayout.LayoutParams =
                    LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
                params.setMargins(
                    0,
                    8,
                    (16 * binding.root.context.resources.displayMetrics.density).roundToInt(),
                    8
                )
                binding.root.layoutParams = params
            }

            Glide.with(binding.root.context)
                .load(product.image.formats.small.getImageUrl())
                .placeholder(R.drawable.ic_splash_background)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
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