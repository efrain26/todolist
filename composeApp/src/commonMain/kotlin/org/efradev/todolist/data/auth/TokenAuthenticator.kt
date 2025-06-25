package org.efradev.todolist.data.auth

import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpClientPlugin
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.plugin
import io.ktor.client.request.header
import io.ktor.http.HttpStatusCode
import io.ktor.util.AttributeKey
import io.ktor.http.Url
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class TokenAuthenticator private constructor(
    config: Config
) : KoinComponent {
    private val authManager: AuthManager by inject()
    private val excludePaths = config.excludePaths

    class Config {
        var excludePaths = listOf("/api/v1/auth/login", "/api/v1/auth/refresh")
    }

    companion object Plugin : HttpClientPlugin<Config, TokenAuthenticator> {
        override val key = AttributeKey<TokenAuthenticator>("TokenAuthenticator")

        override fun prepare(block: Config.() -> Unit): TokenAuthenticator {
            return TokenAuthenticator(Config().apply(block))
        }

        override fun install(plugin: TokenAuthenticator, scope: HttpClient) {
            val httpSend = scope.plugin(HttpSend)

            httpSend.intercept { request ->
                val originalCall = execute(request)
                val path = Url(request.url.toString()).encodedPath

                if (originalCall.response.status == HttpStatusCode.Unauthorized &&
                    path !in plugin.excludePaths
                ) {
                    plugin.authManager.refreshToken().fold(
                        onSuccess = { token ->
                            request.header("Authorization", "Bearer ${token.accessToken}")
                            execute(request)
                        },
                        onFailure = {
                            originalCall
                        }
                    )
                } else {
                    originalCall
                }
            }
        }
    }
}
