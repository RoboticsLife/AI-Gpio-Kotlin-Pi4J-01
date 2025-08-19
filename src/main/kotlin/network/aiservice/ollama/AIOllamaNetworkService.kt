package network.aiservice.ollama

import brain.ai.data.local.AIConfiguration
import brain.data.local.WeatherData
import brain.emitters.NetworkEmitters
import network.InternetConnection
import runtime.setup.Settings

class AIOllamaNetworkService(aiConfiguration: AIConfiguration) {

    private val client = InternetConnection.getWeatherClient(aiConfiguration.aiServerBaseURL.toString())
    private val apiService = client.create(Api::class.java)

    fun getWeatherByName(city: String, units: String = "metric") {
        val response = apiService.getWeatherByName(
            appid = Settings.WEATHER_API_KEY,
            city = city,
            units = units
        ).execute()

        NetworkEmitters.emitWeatherResponse(
            WeatherData(
                weatherResponse = if (response.isSuccessful) response.body() else null,
                isSuccessful = response.isSuccessful,
                httpCode = response.code(),
                message = response.message()
            )
        )
    }
}