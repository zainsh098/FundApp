package com.example.fundapp.extensions

/**
 * Masks the local part of an email address while preserving the first and last two characters.
 *
 * This extension function masks the middle portion of the local part (the part before the `@` symbol)
 * of the email address with asterisks (`***`), keeping the first two and last two characters visible.
 * The domain part (the part after the `@` symbol) remains unchanged.
 *
 * @return A masked email address where the local part is partially obscured.
 *
 * Example usage:
 * ```kotlin
 * val email = "john.doe@example.com"
 * val maskedEmail = email.getEmailMasked()  // maskedEmail will be "jo***oe@example.com"
 * ```
 */
fun String.getEmailMasked(): String {
    val emailParts = this.split("@")
    val localPart = emailParts[0]
    val domain = emailParts[1]

    val maskedLocalPart =
        localPart.substring(0, 2) + "***" + localPart.substring(localPart.length - 2)

    return "$maskedLocalPart@$domain"
}
