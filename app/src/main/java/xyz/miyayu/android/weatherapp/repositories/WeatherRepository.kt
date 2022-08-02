package xyz.miyayu.android.weatherapp.repositories

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import xyz.miyayu.android.weatherapp.network.WeatherApi
import xyz.miyayu.android.weatherapp.network.json.Weather
import xyz.miyayu.android.weatherapp.utils.Response

object WeatherRepository {
    private fun String?.toNullIfEmpty(): String? {
        return if (this.isNullOrEmpty()) null
        else this
    }

    /**
     * [areaName]の天気を取得する。
     * @param areaName エリア名
     * @return APIキーが設定されていない場合は[Response.createApiKeyNotConfiguredResponse]を返す。
     * 天気の取得中にエラーが発生した場合は[Response.createUnknownError]を返す。
     * 何もなけれた[Response.createResponseFromRetrofit]を返す。
     */
    suspend fun fetchWeather(areaName: String): Response<Weather> {
        return withContext(Dispatchers.IO) {
            try {
                //APIキーを読み込む
                val apiKey = SettingRepository.getApiKey().toNullIfEmpty()
                    ?: return@withContext Response.createApiKeyNotConfiguredResponse()

                val weather = getWeather(areaName, apiKey)
                return@withContext Response.createResponseFromRetrofit(weather)
            } catch (e: Throwable) {
                return@withContext Response.createUnknownError()
            }
        }
    }

    private suspend fun getWeather(areaName: String, apiKey: String): retrofit2.Response<Weather> {
        return WeatherApi.retrofitService.getWeather(apiKey, areaName)
    }
}