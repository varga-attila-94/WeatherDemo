# WeatherDemo
Weather Demo App written in Kotlin using MVVM, LiveData, DataBinding and Retrofit

#### NOTE: You need an Open Weather Map API key to properly use the application.

## How To Use
* Sign up and Log in to the [Open Weather Map website](https://home.openweathermap.org)
Create a new API key [HERE](https://home.openweathermap.org/api_key)
* Go to the following directory on your Windows computer:
`C:\Users\your_username\.gradle`
* Create a new file named `gradle.properties` with the following content:
`OPEN_WEATHER_MAP_API_KEY=" *** YOUR API KEY *** "`

#### OR

* In the downloaded project open the `Utils.kt` file located in `WeatherDemo\app\src\main\java\hu\attila\varga\weatherdemo\utils` directory
* Paste your API KEY:
`const val OPEN_WEATHER_MAP_API_KEY = " *** YOUR API KEY *** "`
