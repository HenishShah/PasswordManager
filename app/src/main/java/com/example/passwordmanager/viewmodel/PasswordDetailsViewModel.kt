package com.example.passwordmanager.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.passwordmanager.model.PasswordDetailsModel
import com.example.passwordmanager.repository.PasswordDetailsRepository
import com.example.passwordmanager.roomdb.PasswordManagerDao
import com.example.passwordmanager.roomdb.PasswordManagerDatabase
import com.example.passwordmanager.utils.DataState
import kotlinx.coroutines.launch

class PasswordDetailsViewModel(application: Application) : AndroidViewModel(application) {
    private val _insertDataState: MutableLiveData<DataState<String>> = MutableLiveData()
    private val _updateDataState: MutableLiveData<DataState<String>> = MutableLiveData()
    private val _getPasswordDetailsListDataState: MutableLiveData<DataState<List<PasswordDetailsModel>>> = MutableLiveData()
    private val _deleteDataState: MutableLiveData<DataState<String>> = MutableLiveData()

    init {
        val dao = PasswordManagerDatabase.invoke(application.applicationContext).getPasswordManagerDao()
        setPasswordDetailsListDataStateEvent(dao)
    }

    val insertDataState: LiveData<DataState<String>>
        get() = _insertDataState

    fun setInsertDataStateEvent(
        passwordManagerDao: PasswordManagerDao,
        passwordDetailsModel: PasswordDetailsModel
    ) {
        _insertDataState.value = DataState.Loading
        viewModelScope.launch {
            PasswordDetailsRepository.createPasswordDetails(passwordManagerDao, passwordDetailsModel)
                .collect { dataState -> _insertDataState.value = dataState }
        }
    }

    val updateDataState: LiveData<DataState<String>>
        get() = _updateDataState

    fun setUpdateDataStateEvent(
        passwordManagerDao: PasswordManagerDao,
        passwordDetailsModel: PasswordDetailsModel
    ) {
        _updateDataState.value = DataState.Loading
        viewModelScope.launch {
            PasswordDetailsRepository.updatePasswordDetails(passwordManagerDao, passwordDetailsModel)
                .collect { dataState -> _updateDataState.value = dataState }
        }
    }

    val getPasswordDetailsListDataState: LiveData<DataState<List<PasswordDetailsModel>>>
        get() = _getPasswordDetailsListDataState

    fun setPasswordDetailsListDataStateEvent(
        passwordManagerDao: PasswordManagerDao
    ) {
        _getPasswordDetailsListDataState.value = DataState.Loading
        viewModelScope.launch {
            PasswordDetailsRepository.getPasswordDetails(passwordManagerDao)
                .collect { dataState -> _getPasswordDetailsListDataState.value = dataState }
        }
    }

    val deleteDataState: LiveData<DataState<String>>
        get() = _deleteDataState

    fun setDeleteDataStateEvent(
        passwordManagerDao: PasswordManagerDao,
        passwordDetailsModel: PasswordDetailsModel
    ) {
        _deleteDataState.value = DataState.Loading
        viewModelScope.launch {
            PasswordDetailsRepository.deletePasswordDetails(passwordManagerDao, passwordDetailsModel)
                .collect { dataState -> _deleteDataState.value = dataState }
        }
    }
}