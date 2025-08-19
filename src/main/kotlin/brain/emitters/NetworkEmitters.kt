package brain.emitters

import brain.data.local.WeatherData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

object NetworkEmitters {

    //Weather service
    private val _weatherEmitter = MutableSharedFlow<WeatherData>()
    val weatherEmitter = _weatherEmitter.asSharedFlow()

    //AI
    private val _aiEmitter = MutableStateFlow<String>("")
    val aiEmitter = _aiEmitter.asStateFlow()

    fun emitWeatherResponse(weatherData: WeatherData) {
        CoroutineScope(Dispatchers.IO).launch {
            _weatherEmitter.emit(weatherData)
        }
    }

}