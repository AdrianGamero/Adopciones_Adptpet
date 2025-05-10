package com.example.adopciones_adoptpet

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.adopciones_adoptpet.data.dataSource.FirebasePetDataSource
import com.example.adopciones_adoptpet.data.dataSource.RoomPetDataSource
import com.example.adopciones_adoptpet.data.database.AdoptPetDataBase
import com.example.adopciones_adoptpet.data.repository.PetRepositoryImpl
import com.example.adopciones_adoptpet.ui.components.screens.SplashScreen
import com.example.adopciones_adoptpet.ui.components.viewmodel.PetViewModel
import com.google.firebase.firestore.FirebaseFirestore


class SplashActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = AdoptPetDataBase.getDatabase(this)

        val dao = db.petWithImagesDao()
        val firebaseDb = FirebaseFirestore.getInstance()
        val firebasePetDataSource = FirebasePetDataSource(firebaseDb)
        val roomPetDataSource = RoomPetDataSource(dao)
        val petRepository= PetRepositoryImpl(dao,firebasePetDataSource,roomPetDataSource)
        val petViewModel = PetViewModel(petRepository)
        Log.d("SplashActivity", "onCreate: SplashActivity cargada")

        setContent {
            SplashScreen(petViewModel){
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }

    }
}