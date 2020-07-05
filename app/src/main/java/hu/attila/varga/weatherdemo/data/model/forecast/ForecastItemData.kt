package hu.attila.varga.weatherdemo.data.model.forecast

data class ForecastItemData(
    var dayName: String,
    var weatherImage: String,
    var tempMin: String,
    var tempMax: String
)