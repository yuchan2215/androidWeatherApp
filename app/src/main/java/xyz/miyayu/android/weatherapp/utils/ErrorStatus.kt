package xyz.miyayu.android.weatherapp.utils

import xyz.miyayu.android.weatherapp.R
import xyz.miyayu.android.weatherapp.constant.HTTPResponseCode

/**
 * エラーのステータス
 */
sealed class ErrorStatus {
    companion object {
        /**
         * [retrofit2]のレスポンスからエラーを作成する。
         * HTTPのコードによって、[ErrorWithMessage]と[UnAuthorizedErrorWithMessage]と[AreaErrorWithMessage]を使い分ける。
         */
        fun <T> create(response: retrofit2.Response<T>?): ErrorStatus {
            val errorBody = response?.errorBody()
            return if (errorBody == null) ErrorWithMessage()
            else {
                return when (response.code()) {
                    HTTPResponseCode.UNAUTHORIZED -> UnAuthorizedErrorWithMessage()
                    HTTPResponseCode.NOT_FOUND -> AreaErrorWithMessage()
                    else -> ErrorWithMessage()
                }
            }
        }
    }

    /**メッセージ付きのエラー*/
    data class ErrorWithMessage(val messageId: Int = R.string.error) :
        ErrorStatus()

    /**エリア関係のエラー*/
    data class AreaErrorWithMessage(val messageId: Int = R.string.area_check_notfound_message) :
        ErrorStatus()

    /**API関係のエラー*/
    data class UnAuthorizedErrorWithMessage(val messageId: Int = R.string.error_unauthorized) :
        ErrorStatus()
}