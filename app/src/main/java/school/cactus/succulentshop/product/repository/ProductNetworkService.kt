package school.cactus.succulentshop.product.repository

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import school.cactus.succulentshop.product.repository.model.ProductItem
import school.cactus.succulentshop.product.repository.model.RelatedProducts

interface ProductNetworkService {
    @GET("/products")
    fun getProducts(): Call<List<ProductItem>>

    @GET("/products/{id}")
    fun getProductById(@Path("id") id: Int): Call<ProductItem>

    @GET("/related-products/{id}")
    fun getRelatedProductsById(@Path("id") id: Int): Call<RelatedProducts>
}