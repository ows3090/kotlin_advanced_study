package ows.kotlinstudy.delivery_application.data.repository

import ows.kotlinstudy.delivery_application.data.entity.ShippingCompany

interface ShippingCompanyRepository {

    suspend fun getShippingCompanies(): List<ShippingCompany>

    suspend fun getRecommendShippingCompany(invoice: String): ShippingCompany?
}