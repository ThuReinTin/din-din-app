package tin.thurein.dindin.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import tin.thurein.core.repositories.CategoryRepository
import tin.thurein.core.repositories.IngredientRepository
import tin.thurein.core.repositories.OrderRepository
import tin.thurein.data.repositories.CategoryRepositoryImpl
import tin.thurein.data.repositories.IngredientRepositoryImpl
import tin.thurein.data.repositories.OrderRepositoryImpl

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {
    @Provides
    fun provideOrderRepository(orderRepositoryImpl: OrderRepositoryImpl) : OrderRepository {
        return orderRepositoryImpl
    }

    @Provides
    fun provideCategoryRepository(categoryRepositoryImpl: CategoryRepositoryImpl) : CategoryRepository {
        return categoryRepositoryImpl
    }

    @Provides
    fun provideIngredientRepository(ingredientRepositoryImpl: IngredientRepositoryImpl) : IngredientRepository {
        return ingredientRepositoryImpl
    }
}