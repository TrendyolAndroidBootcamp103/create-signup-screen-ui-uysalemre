package school.cactus.succulentshop.product.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import school.cactus.succulentshop.base.BaseViewModel
import school.cactus.succulentshop.product.repository.ProductRepository
import school.cactus.succulentshop.product.repository.model.ProductItem

class ProductListViewModel(private val productRepository: ProductRepository) : BaseViewModel() {
    private val _success: MutableLiveData<List<ProductItem>> = MutableLiveData()
    val success: LiveData<List<ProductItem>> get() = _success

    init {
        sendProductListRequest()
    }

    fun sendProductListRequest() {
        viewModelScope.launch {
            parseResponse(
                _success,
                productRepository.makeProductListRequest()
            )
        }
    }
}