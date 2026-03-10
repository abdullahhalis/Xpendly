package com.abdullahhalis.expensetracker.di

import android.content.Context
import androidx.room.Room
import com.abdullahhalis.expensetracker.data.local.ExpenseDatabase
import com.abdullahhalis.expensetracker.data.local.ExpenseDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideExpenseDatabase(
        @ApplicationContext context: Context,
    ): ExpenseDatabase {

        return Room.databaseBuilder(
            context,
            ExpenseDatabase::class.java,
            "expense_db"
        ).fallbackToDestructiveMigration(dropAllTables = true).build()
    }

    @Provides
    fun provideExpenseDao(db: ExpenseDatabase): ExpenseDao = db.expenseDao()
}