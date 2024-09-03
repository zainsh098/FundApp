package com.example.fundapp.extensions

fun String.getEmailMasked(): String {
    val emailParts = this.split("@")
    val localPart = emailParts[0]
    val domain = emailParts[1]

    val maskedLocalPart =
        localPart.substring(0, 2) + "***" + localPart.substring(localPart.length - 2)

    return "$maskedLocalPart@$domain"
}