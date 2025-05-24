package icesi.edu.co.fitscan.features.home.ui.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import icesi.edu.co.fitscan.domain.repositories.IDashboardRepository
import icesi.edu.co.fitscan.domain.usecases.IGetDashboardDataUseCase
import icesi.edu.co.fitscan.features.common.data.remote.RetrofitInstance
import icesi.edu.co.fitscan.features.home.data.dataSources.IDashboardDataSource
import icesi.edu.co.fitscan.features.home.data.repositories.DashboardRepositoryImpl
import icesi.edu.co.fitscan.features.home.data.usecases.GetDashboardDataUseCaseImpl
import icesi.edu.co.fitscan.features.home.ui.viewmodel.DashboardViewModel

class DashboardViewModelFactory: ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DashboardViewModel::class.java)) {
            val dashboardDataSource = RetrofitInstance.create(IDashboardDataSource::class.java)

            val dashboardRepository: IDashboardRepository = DashboardRepositoryImpl(
                datasource = dashboardDataSource
            )

            val getDashboardDataUseCase: IGetDashboardDataUseCase = GetDashboardDataUseCaseImpl(dashboardRepository)

            @Suppress("UNCHECKED_CAST")
            return DashboardViewModel(
                getDashboardDataUseCase = getDashboardDataUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}