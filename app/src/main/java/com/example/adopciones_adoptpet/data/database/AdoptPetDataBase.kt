package com.example.adopciones_adoptpet.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.adopciones_adoptpet.converters.Converters
import com.example.adopciones_adoptpet.data.dao.PetWithImagesDao
import com.example.adopciones_adoptpet.domain.model.BreedEntity
import com.example.adopciones_adoptpet.domain.model.PetEntity
import com.example.adopciones_adoptpet.domain.model.PetImageEntity
import com.example.adopciones_adoptpet.domain.model.RequestEntity
import com.example.adopciones_adoptpet.domain.model.ShelterEntity

@Database(
    entities = [PetEntity::class, RequestEntity::class, ShelterEntity::class, BreedEntity::class, PetImageEntity::class],
    version = 3,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AdoptPetDataBase : RoomDatabase() {
    abstract fun petWithImagesDao(): PetWithImagesDao

    companion object {
        @Volatile
        private var INSTANCE: AdoptPetDataBase? = null

        fun getDatabase(context: Context): AdoptPetDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AdoptPetDataBase::class.java,
                    "AdoptPetDataBase"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }


}