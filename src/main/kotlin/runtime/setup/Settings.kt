package runtime.setup

object Settings {
    const val WEATHER_API_KEY = "095a8698ac1b2a74625287b8a392861c"
    const val DISTANCE_SENSOR_HC_SR04_DIVIDER_TO_CM = 58F
    const val DISTANCE_SENSOR_HC_SR04_DIVIDER_TO_INCH = 148F
    const val DISTANCE_SENSOR_HC_SR04_MAX_LIMIT_CM = 400F
    const val DISTANCE_SENSOR_HC_SR04_MAX_LIMIT_INCH = 158F
    const val DEFAULT_SDA_PIN = 1
    const val DEFAULT_SCL_PIN = 2
    const val CONNECTION_TYPE_I2C = "i2c"
    const val AI_DEFAULT_CONFIG_FILE_NAME = "aiconfig.json"
    const val AI_REMOTE_CONNECTION_TYPE = "remote"
    const val AI_LOCAL_CONNECTION_TYPE = "local"
    const val AI_FORMATTED_STRING_QUERY_WORDS_LIMITATION = "(limit response with %s words)"
    const val NETWORK_OKHTTP_DEFAULT_READ_TIMEOUT_SEC = 120L
    const val NETWORK_OKHTTP_DEFAULT_CONNECT_TIMEOUT_SEC = 120L
    const val NETWORK_OKHTTP_DEFAULT_WRITE_TIMEOUT_SEC = 120L
}