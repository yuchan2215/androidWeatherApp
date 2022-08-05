package xyz.miyayu.android.weatherapp.utils

import xyz.miyayu.android.weatherapp.R

/**
 * レスポンス
 */
sealed class Response<T> {
    companion object {
        /**
         * retrofit2のレスポンスからレスポンスを作成する。
         */
        fun <T> createResponseFromRetrofit(response: retrofit2.Response<T>): Response<T> {
            return when {
                response.isSuccessful && response.body() != null ->
                    SuccessResponse(response.body()!!)
                else -> ErrorResponse(ErrorStatus.create(response))
            }
        }


        /**
         * 不明なエラーを作成する
         */
        fun <T> createUnknownError(): ErrorResponse<T> {
            return ErrorResponse(errorStatus = ErrorStatus.ErrorWithMessage())
        }

        /**
         * APIキーが設定されていないというメッセージで、APIキーの再設定が必要なレスポンスを作成する
         */
        fun <T> createApiKeyNotConfiguredResponse(): Response<T> {
            return ErrorResponse(ErrorStatus.UnAuthorizedErrorWithMessage(R.string.api_key_not_found_error_message))
        }

    }

    /**成功した時のレスポンス*/
    data class SuccessResponse<T>(val body: T) : Response<T>()

    /**失敗した時のレスポンス*/
    data class ErrorResponse<T>(val errorStatus: ErrorStatus) : Response<T>()

    /**読み込み中*/
    class Loading<T> : Response<T>()
}


