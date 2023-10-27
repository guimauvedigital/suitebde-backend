package me.nathanfallet.suitebde.models

enum class LocalizedString(val value: String) {

    ERROR_BODY_INVALID("Invalid body"),
    ERROR_MISSING_ID("Missing id"),
    ERROR_MOCK("Mock error"),

    ERROR_ASSOCIATIONS_NOT_FOUND("Association not found"),
    ERROR_USERS_VIEW_NOT_ALLOWED("You are not allowed to view users"),
    ERROR_USERS_NOT_FOUND("User not found"),
    ERROR_USERS_CREATE_NOT_ALLOWED("You cannot create users"),
    ERROR_USERS_UPDATE_NOT_ALLOWED("You are not allowed to update users"),
    ERROR_USERS_DELETE_NOT_ALLOWED("You cannot delete users"),

    ERROR_AUTH_INVALID_CREDENTIALS("Invalid credentials"),

}