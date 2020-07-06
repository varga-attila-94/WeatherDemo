# WeatherDemo
Weather Demo App written in Kotlin using MVVM, LiveData, DataBinding and Retrofit

#### NOTE: You need a free Open Weather Map API key to properly use the application.


## Screenshots
![Screenshot_1594054714](https://user-images.githubusercontent.com/67831641/86619413-ce0cf780-bfba-11ea-8df5-b0ef1314aba8.png)
![Screenshot_1594054720](https://user-images.githubusercontent.com/67831641/86619419-d06f5180-bfba-11ea-9bcb-8aa7127c4937.png)


## How To Use
* Sign up and Log in to the [Open Weather Map website](https://home.openweathermap.org)
* Create a new API key [HERE](https://home.openweathermap.org/api_keys)
* Go to the following directory on your Windows computer:
`C:\Users\your_username\.gradle`
* Create a new file named `gradle.properties` with the following content:

  ```
  OPEN_WEATHER_MAP_API_KEY=" *** YOUR API KEY *** "
  ```

#### OR
* Remove or comment out this line from `build.gradle` :
  ```
  buildConfigField 'String' , "OPEN_WEATHER_MAP_API_KEY", OPEN_WEATHER_MAP_API_KEY
  ```

* In the downloaded project open the `Utils.kt` file located in `WeatherDemo\app\src\main\java\hu\attila\varga\weatherdemo\utils` directory
* Paste your API key:

  ```
  const val OPEN_WEATHER_MAP_API_KEY = " *** YOUR API KEY *** "
  ```

## Author
* Attila Varga
## License
[![License: GPL v3](https://img.shields.io/badge/License-GPLv3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0)
