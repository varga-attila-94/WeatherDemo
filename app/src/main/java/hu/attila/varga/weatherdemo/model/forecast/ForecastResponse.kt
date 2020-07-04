package hu.attila.varga.weatherdemo.model.forecast


data class ForecastResponse(
    val daily: List<Daily>,
    val lat: Double,
    val lon: Double,
    val timezone: String,
    val timezone_offset: Int
)