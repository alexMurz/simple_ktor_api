package com.example.mods

import io.ktor.application.ApplicationCall

interface Mod {
    companion object {
        /// Compile error
        fun err(msg: String): String {
            return "{ \"err\" = \"$msg\" }"
        }
    }

    fun doc(): String
    suspend fun perform(call: ApplicationCall)
}
