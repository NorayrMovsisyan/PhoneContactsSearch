package com.example.phonecontactssearch.domain.di

import com.example.phonecontactssearch.domain.use_case.GetContacts
import com.example.phonecontactssearch.presentation.ContactViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { ContactViewModel(get()) }
}

val useCaseModule = module {
    factory { GetContacts(get()) }
}