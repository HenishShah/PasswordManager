package com.example.passwordmanager.roomdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.passwordmanager.model.PasswordDetailsModel

@Database(entities = [PasswordDetailsModel::class], version = 1)
abstract class PasswordManagerDatabase: RoomDatabase() {

    abstract fun getPasswordManagerDao(): PasswordManagerDao

    companion object {

        @Volatile
        private var INSTANCE : PasswordManagerDatabase? = null

        private var LOCK = Any()

        operator fun invoke(context: Context) = INSTANCE ?: synchronized(LOCK){
            INSTANCE ?: createDatabase(context).also { INSTANCE = it }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                PasswordManagerDatabase::class.java,
                "password_db.db"
            ).allowMainThreadQueries().build()
    }
}