package com.depromeet.baton.annotation

import javax.inject.Qualifier

@Qualifier
annotation class Server(val type: ServerType)

enum class ServerType {
    User,
    Search
}
