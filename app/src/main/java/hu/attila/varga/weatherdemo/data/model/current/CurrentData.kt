package hu.attila.varga.weatherdemo.data.model.current

data class CurrentData(
    var dayName: String,
    var city: String,
    var weatherDescription: String,
    var weatherImage: String,
    var humidity: Int,
    var temp: Int,
    var tempMin: Int,
    var tempMax: Int,
    var pressure: Int,
    var windSpeed: Double
)