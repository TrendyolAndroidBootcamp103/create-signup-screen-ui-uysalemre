package school.cactus.succulentshop.product.repository

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import school.cactus.succulentshop.product.repository.model.ProductItem
import school.cactus.succulentshop.product.repository.model.RelatedProducts

interface ProductNetworkService {
    @GET("/products")
    suspend fun getProducts(): List<ProductItem>

    @GET("/products/{id}")
    suspend fun getProductById(@Path("id") id: Int): ProductItem

    @GET("/related-products/{id}")
    suspend fun getRelatedProductsById(@Path("id") id: Int): RelatedProducts
}