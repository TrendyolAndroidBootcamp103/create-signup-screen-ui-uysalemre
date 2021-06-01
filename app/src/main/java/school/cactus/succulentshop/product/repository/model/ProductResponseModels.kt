package school.cactus.succulentshop.product.repository.model

import com.google.gson.annotations.SerializedName

data class RelatedProducts(
    val id: Int,
    val publishedAt: String,
    val createdAt: String,
    val updatedAt: String,
    val products: List<ProductItem>
)

data class ProductItem(
    val image: Image,
    @SerializedName("updated_at")
    val updatedAt: String,
    val price: Double,
    val description: String,
    @SerializedName("created_at")
    val createdAt: String,
    val id: Int,
    val title: String,
    val publishedAt: String
)

data class Formats(
    val small: SizedImage,
    val thumbnail: SizedImage,
    val large: SizedImage,
    val medium: SizedImage
)

data class Image(
    val ext: String,
    val formats: Formats,
    val previewUrl: String?,
    val mime: String,
    val caption: String,
    @SerializedName("created_at")
    val createdAt: String,
    val url: String,
    val size: Double,
    @SerializedName("updated_at")
    val updatedAt: String,
    val provider: String,
    val name: String,
    val width: Int?,
    @SerializedName("provider_metadata")
    val providerMetadata: String?,
    val id: Int,
    val alternativeText: String,
    val hash: String,
    val height: Int
)

data class SizedImage(
    val ext: String,
    val path: String?,
    val size: Double,
    val mime: String,
    val name: String,
    val width: Int,
    val url: String,
    val hash: String,
    val height: Int
)


