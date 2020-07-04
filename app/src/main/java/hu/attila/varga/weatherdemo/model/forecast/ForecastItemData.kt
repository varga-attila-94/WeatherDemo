package hu.attila.varga.weatherdemo.model.forecast

data class ForecastItemData(
    var dayName: String,
    var weatherImage: String,
    var tempMin: String,
    var tempMax: String
)