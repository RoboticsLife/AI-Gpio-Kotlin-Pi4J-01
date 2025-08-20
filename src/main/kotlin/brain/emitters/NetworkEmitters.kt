package brain.emitters

import brain.ai.data.local.AIFlowDataModel
import brain.data.local.WeatherData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

object NetworkEmitters {

    //Weather service
    private val _weatherEmitter = MutableSharedFlow<WeatherData>()
    val weatherEmitter = _weatherEmitter.asSharedFlow()

    //AI
    private val _aiEmitter = MutableSharedFlow<AIFlowDataModel?>(replay = 1)
    val aiEmitter = _aiEmitter.asSharedFlow()

    fun emitWeatherResponse(weatherData: WeatherData) {
        CoroutineScope(Dispatchers.IO).launch {
            _weatherEmitter.emit(weatherData)
        }
    }

    fun emitAIResponse(aiFlowDataModel: AIFlowDataModel?) {
        CoroutineScope(Dispatchers.IO).launch {
            _aiEmitter.emit(aiFlowDataModel)
        }
    }
}