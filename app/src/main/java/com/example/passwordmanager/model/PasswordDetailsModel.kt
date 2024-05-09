package com.example.passwordmanager.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("password_details_table")
data class PasswordDetailsModel(
    @PrimaryKey(autoGenerate = true)
    var passwordDetailId: Int = 0,
    var accountType: String,
    var userName: String,
    var password: String
)
