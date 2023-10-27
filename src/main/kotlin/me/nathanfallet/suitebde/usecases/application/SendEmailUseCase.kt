package me.nathanfallet.suitebde.usecases.application

class SendEmailUseCase : ISendEmailUseCase {

    override suspend fun invoke(input: Triple<String, String, String>) {
        // TODO: Send email
        println(input)
    }

}