package me.nathanfallet.suitebde.services.jwt

interface IJWTService {

    fun generateJWT(userId: String, clientId: String, type: String): String

}
