package me.nathanfallet.suitebde.usecases.auth

import at.favre.lib.crypto.bcrypt.BCrypt

class VerifyPasswordUseCase : IVerifyPasswordUseCase {

    override fun invoke(input: Pair<String, String>): Boolean {
        return BCrypt.verifyer().verify(input.first.toCharArray(), input.second).verified
    }

}