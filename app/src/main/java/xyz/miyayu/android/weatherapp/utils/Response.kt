package xyz.miyayu.android.weatherapp.utils

import xyz.miyayu.android.weatherapp.R

/**
 * レスポンス
 */
sealed class Response {
    companion object {
        /**
         * retrofit2のレスポンスからレスポンスを作成する。
         */
        fun <T> createResponseFromRetrofit(response: retrofit2.Response<T>): Response {
            return when {
                response.isSuccessful && response.body() != null ->
                    SuccessResponse(response.body()!!)
                else -> ErrorResponse(ErrorStatus.create(response))
            }
        }


        /**
         * 不明なエラーを作成する
         */
        fun createUnknownError(): Response {
            return ErrorResponse(errorStatus = ErrorStatus.ErrorWithMessage())
        }

        /**
         * APIキーが設定されていないというメッセージで、APIキーの再設定が必要なレスポンスを作成する
         */
        fun createApiKeyNotConfiguredResponse(): Response {
            return ErrorResponse(ErrorStatus.UnAuthorizedErrorWithMessage(R.string.api_key_not_found_error_message))
        }

    }

    /**成功した時のレスポンス*/
    data class SuccessResponse<T>(val body: T) : Response()

    /**失敗した時のレスポンス*/
    data class ErrorResponse(val errorStatus: ErrorStatus) : Response()

    /**読み込み中*/
    object Loading : Response()
}


