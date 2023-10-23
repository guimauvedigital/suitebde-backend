package me.nathanfallet.suitebde.usecases.users

import at.favre.lib.crypto.bcrypt.BCrypt

class HashPasswordUseCase : IHashPasswordUseCase {

    override fun invoke(input: String): String {
        return BCrypt.withDefaults().hashToString(12, input.toCharArray())
    }

}