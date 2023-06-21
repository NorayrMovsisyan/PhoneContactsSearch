package com.example.phonecontactssearch.domain.model

import android.net.Uri

data class Contact(
    val contactId: Long,
    val name: String,
    val phoneNumber: String,
    val avatar: Uri?
) {
    fun doesMatchSearchQuery(query: String): Boolean {
        val matchingCombinations = listOf(
            name, phoneNumber,
        )

        return matchingCombinations.any {
            it.contains(query, ignoreCase = true)
        }
    }
}
