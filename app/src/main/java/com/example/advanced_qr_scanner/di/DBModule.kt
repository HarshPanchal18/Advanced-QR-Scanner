package com.example.advanced_qr_scanner.di

import android.content.Context
import com.example.advanced_qr_scanner.Database
import com.squareup.sqldelight.android.AndroidSqliteDriver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

private const val DATABASE_NAME = "scan_history.db"

@Module
@InstallIn(SingletonComponent::class)
class DBModule {

    @Provides
    fun providesDb(@ApplicationContext context: Context) =
        AndroidSqliteDriver(Database.Schema, context, DATABASE_NAME).run { Database(this) }

    @Provides
    fun scanHistoryQueries(database: Database) = database.scanHistoryQueries
}
