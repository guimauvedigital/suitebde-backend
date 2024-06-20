package com.suitebde.models.stripe

import kotlinx.serialization.Serializable

@Serializable
data class Customer(
    override val email: String,
) : ICustomer
