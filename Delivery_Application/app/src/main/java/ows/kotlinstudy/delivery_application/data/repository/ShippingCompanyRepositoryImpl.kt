package ows.kotlinstudy.delivery_application.data.repository

import android.util.Log
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ows.kotlinstudy.delivery_application.data.api.SweetTrackerApi
import ows.kotlinstudy.delivery_application.data.db.ShippingCompanyDao
import ows.kotlinstudy.delivery_application.data.entity.ShippingCompany
import ows.kotlinstudy.delivery_application.data.preference.PreferenceManager

class ShippingCompanyRepositoryImpl(
    private val trackerApi: SweetTrackerApi,
    private val shippingCompanyDao: ShippingCompanyDao,
    private val preferenceManager: PreferenceManager,
    private val dispatcher: CoroutineDispatcher
) : ShippingCompanyRepository {

    override suspend fun getShippingCompanies(): List<ShippingCompany> = withContext(dispatcher) {
        val currentTimeMillis = System.currentTimeMillis()
        val lastDatabaseUpdatedTimeMilles = preferenceManager.getLong(
            KEY_LAST_DATEBASE_UPDATED_TIME_MILLIS
        )
        
        /**
         * DB 추가
         * 1. DB에 값이 없을 경우
         * 2. DB에 넣은 시점이 7일이 지난 경우
         */
        if (lastDatabaseUpdatedTimeMilles == null ||
            CACHE_MAX_AGE_MILLIS < (currentTimeMillis - lastDatabaseUpdatedTimeMilles)
        ) {
            val shippingCompanies = trackerApi.getShippingCompanies()
                .body()
                ?.shippingCompanies
                ?: emptyList()

            shippingCompanyDao.insert(shippingCompanies)
            preferenceManager.putLong(KEY_LAST_DATEBASE_UPDATED_TIME_MILLIS, currentTimeMillis)
        }

        shippingCompanyDao.getAll()
    }

    override suspend fun getRecommendShippingCompany(invoice: String): ShippingCompany? = withContext(dispatcher){
        try{
            // code가 낮을 수록 메이저 택배회사
            trackerApi.getRecommendShippingCompanies(invoice)
                .body()
                ?.shippingCompanies
                ?.minByOrNull { it.code.toIntOrNull() ?: Int.MAX_VALUE }
        }catch (exception: Exception){
            null
        }
    }

    companion object {
        private const val KEY_LAST_DATEBASE_UPDATED_TIME_MILLIS =
            "KEY_LAST_DATEBASE_UPDATED_TIME_MILLIS"

        // 7day to ms
        private const val CACHE_MAX_AGE_MILLIS = 1000L * 60 * 60 * 24 * 7
    }
}