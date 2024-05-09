package com.example.passwordmanager.roomdb

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.passwordmanager.model.PasswordDetailsModel
import kotlinx.coroutines.flow.Flow

@Dao
interface PasswordManagerDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(passwordDetailsModel: PasswordDetailsModel)

    @Query("UPDATE password_details_table SET accountType = :accountType, userName = :userName, password = :password WHERE passwordDetailId = :passwordDetailId")
    suspend fun update(passwordDetailId: Int, accountType: String, userName: String, password: String)

    @Delete
    suspend fun delete(passwordDetailsModel: PasswordDetailsModel)

    @Query("SELECT * FROM password_details_table")
    fun getPasswordDetailsList(): Flow<List<PasswordDetailsModel>>
}