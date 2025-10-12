package com.kreggscode.morsecode.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.kreggscode.morsecode.data.local.dao.*
import com.kreggscode.morsecode.data.local.entities.*
import com.kreggscode.morsecode.util.Constants

@Database(
    entities = [
        TranslationEntity::class,
        LearnProgressEntity::class,
        GameScoreEntity::class,
        ChatMessageEntity::class
    ],
    version = Constants.DATABASE_VERSION,
    exportSchema = false
)
abstract class MorseDatabase : RoomDatabase() {
    
    abstract fun translationDao(): TranslationDao
    abstract fun learnProgressDao(): LearnProgressDao
    abstract fun gameScoreDao(): GameScoreDao
    abstract fun chatMessageDao(): ChatMessageDao
    
    companion object {
        @Volatile
        private var INSTANCE: MorseDatabase? = null
        
        fun getDatabase(context: Context): MorseDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MorseDatabase::class.java,
                    Constants.DATABASE_NAME
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
