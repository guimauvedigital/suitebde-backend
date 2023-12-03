package me.nathanfallet.suitebde.usecases.auth

import me.nathanfallet.ktorx.usecases.auth.ICreateSessionForUserUseCase
import me.nathanfallet.suitebde.models.auth.SessionPayload
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.usecases.users.ISessionPayload
import me.nathanfallet.usecases.users.IUser

class CreateSessionForUserUseCase : ICreateSessionForUserUseCase {

    override fun invoke(input: IUser): ISessionPayload {
        return SessionPayload((input as User).id)
    }

}
