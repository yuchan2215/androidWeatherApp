package xyz.miyayu.android.weatherapp.repositories

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import xyz.miyayu.android.weatherapp.network.WeatherApi
import xyz.miyayu.android.weatherapp.network.json.direct.Direct
import xyz.miyayu.android.weatherapp.utils.Response

object GeoRepository {
    private fun String?.toNullIfEmpty(): String? {
        return if (this.isNullOrEmpty()) null
        else this
    }

    /**
     * [areaName]の地域を取得する
     * @param areaName エリア名
     * @return APIキーが設定されていない場合は[Response.createApiKeyNotConfiguredResponse]を返す。
     * エリアの取得中にエラーが発生した場合は[Response.createUnknownError]を返す。
     * 何もなけれた[Response.createResponseFromRetrofit]を返す。
     */
    suspend fun fetchGeos(areaName: String): Response<List<Direct>> {
        return withContext(Dispatchers.IO) {
            try {
                //APIキーを読み込む
                val apiKey = SettingRepository.getApiKey().toNullIfEmpty()
                    ?: return@withContext Response.createApiKeyNotConfiguredResponse()

                val geoResponse = getGeos(areaName, apiKey)
                return@withContext Response.createResponseFromRetrofit(geoResponse)
            } catch (e: Throwable) {
                return@withContext Response.createUnknownError()
            }
        }
    }

    private suspend fun getGeos(
        areaName: String,
        apiKey: String
    ): retrofit2.Response<List<Direct>> {
        return WeatherApi.retrofitService.getAreas(apiKey, areaName)
    }
}