package ows.kotlinstudy.shopping_application.data.response

data class ProductsResponse(
    val items : List<ProductResponse>,
    val count: Int
)
