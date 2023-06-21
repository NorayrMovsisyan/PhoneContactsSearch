package com.example.phonecontactssearch.domain.use_case

import android.content.Context
import com.example.phonecontactssearch.domain.model.Contact
import com.example.phonecontactssearch.extension.retrieveAllContacts
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetContacts(private val context: Context)  {
   suspend  operator fun invoke():List<Contact> = withContext(Dispatchers.IO){
       return@withContext context.retrieveAllContacts()
    }
}