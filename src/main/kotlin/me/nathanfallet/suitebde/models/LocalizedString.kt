package me.nathanfallet.suitebde.models

enum class LocalizedString(val value: String) {

    ERROR_BODY_INVALID("Invalid body"),
    ERROR_MISSING_ID("Missing id"),
    ERROR_MOCK("Mock error"),

    ASSOCIATIONS_NOT_FOUND("Association not found"),

    USERS_VIEW_NOT_ALLOWED("You are not allowed to view users"),
    USERS_NOT_FOUND("User not found"),
    USERS_CREATE_NOT_ALLOWED("You cannot create users"),
    USERS_UPDATE_NOT_ALLOWED("You are not allowed to update users"),
    USERS_DELETE_NOT_ALLOWED("You cannot delete users"),

    AUTH_INVALID_CREDENTIALS("Invalid credentials"),
    AUTH_JOIN_EMAIL_SENT("Please check your inbox to complete your registration"),
    AUTH_JOIN_SUBMITTED("Your registration has been submitted, please wait for validation"),

}