package school.cactus.succulentshop.product.repository

import school.cactus.succulentshop.product.repository.model.ProductItem
import school.cactus.succulentshop.product.repository.model.RelatedProducts
import school.cactus.succulentshop.utils.network.NetworkCallback
import school.cactus.succulentshop.utils.network.SucculentNetworkHelper.createService
import school.cactus.succulentshop.utils.network.SucculentNetworkHelper.handleNetworkCallback

object ProductNetworkHelper {
    fun requestAllProducts(
        networkCallback: NetworkCallback<List<ProductItem>>
    ) {
        createService(ProductNetworkService::class.java)
            .getProducts()
            .enqueue(handleNetworkCallback(networkCallback))
    }

    fun requestProductById(
        id: Int,
        networkCallback: NetworkCallback<ProductItem>
    ) {
        createService(ProductNetworkService::class.java)
            .getProductById(id)
            .enqueue(handleNetworkCallback(networkCallback))
    }

    fun requestRelatedProducts(
        id: Int,
        networkCallback: NetworkCallback<RelatedProducts>
    ) {
        createService(ProductNetworkService::class.java)
            .getRelatedProductsById(id)
            .enqueue(handleNetworkCallback(networkCallback))
    }
}