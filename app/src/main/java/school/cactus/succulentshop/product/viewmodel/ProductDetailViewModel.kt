package school.cactus.succulentshop.product.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import school.cactus.succulentshop.base.BaseViewModel
import school.cactus.succulentshop.product.repository.ProductRepository
import school.cactus.succulentshop.product.repository.model.ProductItem
import school.cactus.succulentshop.product.repository.model.RelatedProducts

class ProductDetailViewModel(private val productRepository: ProductRepository) : BaseViewModel() {
    private val _successProductItem: MutableLiveData<ProductItem> = MutableLiveData()
    val successProductItem: LiveData<ProductItem> get() = _successProductItem

    private val _successRelatedProducts: MutableLiveData<RelatedProducts> = MutableLiveData()
    val successRelatedProducts: LiveData<RelatedProducts> get() = _successRelatedProducts

    fun sendProductItemRequest(id: Int) {
        viewModelScope.launch {
            parseResponse(
                _successProductItem,
                productRepository.makeProductByIdRequest(id)
            )
        }
    }

    fun sendRelatedProductsRequest(id: Int) {
        viewModelScope.launch {
            parseResponse(
                _successRelatedProducts,
                productRepository.makeRelatedProductsRequest(id)
            )
        }
    }
}