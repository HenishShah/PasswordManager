package com.example.passwordmanager.repository

import com.example.passwordmanager.model.PasswordDetailsModel
import com.example.passwordmanager.roomdb.PasswordManagerDao
import com.example.passwordmanager.utils.DataState
import com.example.passwordmanager.utils.EncryptDecrypt
import com.example.passwordmanager.utils.logD
import com.example.passwordmanager.utils.logE
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

object PasswordDetailsRepository {

    val TAG = "PasswordDetailsRepository"

    suspend fun createPasswordDetails(
        passwordManagerDao: PasswordManagerDao,
        passwordDetailsModel: PasswordDetailsModel
    ): Flow<DataState<String>> = flow {
        try {
            if (passwordDetailsModel.accountType.isEmpty()) {
                emit(DataState.Error("Please enter account type"))
            } else if (passwordDetailsModel.userName.isEmpty()) {
                emit(DataState.Error("Please enter user name"))
            } else if (passwordDetailsModel.password.isEmpty()) {
                emit(DataState.Error("Please enter password"))
            } else {
                passwordDetailsModel.password = EncryptDecrypt().getEncryptedBody(
                    passwordDetailsModel.accountType,
                    passwordDetailsModel.password
                ) ?: passwordDetailsModel.password
                passwordManagerDao.insert(passwordDetailsModel)
                logD(TAG, "Successfully Inserted - $passwordDetailsModel")
                emit(DataState.Success("Data Inserted Successfully"))
            }
        } catch (e: Exception) {
            logE(TAG, "Error - ${e.message}")
            emit(DataState.Error("Something went wrong"))
        }
    }

    suspend fun updatePasswordDetails(
        passwordManagerDao: PasswordManagerDao,
        passwordDetailsModel: PasswordDetailsModel
    ): Flow<DataState<String>> = flow {
        try {
            if (passwordDetailsModel.accountType.isEmpty()) {
                emit(DataState.Error("Please enter account type"))
            } else if (passwordDetailsModel.userName.isEmpty()) {
                emit(DataState.Error("Please enter user name"))
            } else if (passwordDetailsModel.password.isEmpty()) {
                emit(DataState.Error("Please enter password"))
            } else {
                passwordDetailsModel.password = EncryptDecrypt().getEncryptedBody(
                    passwordDetailsModel.accountType,
                    passwordDetailsModel.password
                ) ?: passwordDetailsModel.password
                passwordManagerDao.update(
                    passwordDetailsModel.passwordDetailId,
                    passwordDetailsModel.accountType,
                    passwordDetailsModel.userName,
                    passwordDetailsModel.password,
                )
                logD(TAG, "Successfully Updated - $passwordDetailsModel")
                emit(DataState.Success("Data Updated Successfully"))
            }
        } catch (e: Exception) {
            logE(TAG, "Error - ${e.message}")
            emit(DataState.Error("Something went wrong"))
        }
    }

    suspend fun getPasswordDetails(
        passwordManagerDao: PasswordManagerDao
    ): Flow<DataState<List<PasswordDetailsModel>>> = flow {
        try {
            passwordManagerDao.getPasswordDetailsList().collect {
                logD(TAG, "List - $it")
                emit(DataState.Success(it))
            }
        } catch (e: Exception) {
            logE(TAG, "Error - ${e.message}")
            emit(DataState.Error("Something went wrong"))
        }
    }

    suspend fun deletePasswordDetails(
        passwordManagerDao: PasswordManagerDao,
        passwordDetailsModel: PasswordDetailsModel
    ): Flow<DataState<String>> = flow {
        try {
            passwordManagerDao.delete(passwordDetailsModel)
            logD(TAG, "Successfully Deleted - $passwordDetailsModel")
            emit(DataState.Success("Data Deleted Successfully"))
        } catch (e: Exception) {
            logE(TAG, "Error - ${e.message}")
            emit(DataState.Error("Something went wrong"))
        }
    }


}