package school.cactus.succulentshop.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import school.cactus.succulentshop.auth.repository.AuthRepository
import school.cactus.succulentshop.auth.viewmodel.LoginViewModel
import school.cactus.succulentshop.auth.viewmodel.SignUpViewModel
import school.cactus.succulentshop.base.BaseRepository
import school.cactus.succulentshop.product.repository.ProductRepository
import school.cactus.succulentshop.product.viewmodel.ProductDetailViewModel
import school.cactus.succulentshop.product.viewmodel.ProductListViewModel

class ViewModelFactory(val repository: BaseRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>) = when (modelClass.name) {
        LoginViewModel::class.java.name -> LoginViewModel(repository as AuthRepository)
        SignUpViewModel::class.java.name -> SignUpViewModel(repository as AuthRepository)
        ProductListViewModel::class.java.name -> ProductListViewModel(repository as ProductRepository)
        ProductDetailViewModel::class.java.name -> ProductDetailViewModel(repository as ProductRepository)
        else -> {
            throw IllegalArgumentException("Unknown ViewModel Class")
        }
    } as T
}