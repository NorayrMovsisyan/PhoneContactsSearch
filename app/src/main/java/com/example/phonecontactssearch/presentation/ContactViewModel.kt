package com.example.phonecontactssearch.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.phonecontactssearch.domain.model.Contact
import com.example.phonecontactssearch.domain.use_case.GetContacts
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class ContactViewModel(private val getContacts: GetContacts) : ViewModel() {

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    private val _contact = MutableStateFlow(emptyList<Contact>())
    val contact = searchText
        .debounce(500L)
        .onEach { _isSearching.update { true } }
        .combine(_contact) { text, contact ->
            if (text.isBlank()) {
                contact
            } else {
                delay(1000L)
                contact.filter {
                    it.doesMatchSearchQuery(text)
                }
            }
        }
        .onEach { _isSearching.update { false } }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            _contact.value
        )

    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }

    fun getContactList() {
        viewModelScope.launch() {
            _contact.emit(getContacts())
        }
    }

}
