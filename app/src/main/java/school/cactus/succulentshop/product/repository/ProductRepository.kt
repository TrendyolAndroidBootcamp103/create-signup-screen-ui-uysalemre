package school.cactus.succulentshop.product.repository

import kotlinx.coroutines.Dispatchers
import school.cactus.succulentshop.base.BaseRepository
import school.cactus.succulentshop.utils.network.SucculentNetworkHelper

class ProductRepository : BaseRepository() {

    suspend fun makeProductListRequest() = safeApiCall(Dispatchers.IO) {
        SucculentNetworkHelper.createService(ProductNetworkService::class.java)
            .getProducts()
    }

    suspend fun makeProductByIdRequest(id: Int) = safeApiCall(Dispatchers.IO) {
        SucculentNetworkHelper.createService(ProductNetworkService::class.java)
            .getProductById(id)
    }

    suspend fun makeRelatedProductsRequest(id: Int) = safeApiCall(Dispatchers.IO) {
        SucculentNetworkHelper.createService(ProductNetworkService::class.java)
            .getRelatedProductsById(id)
    }

}