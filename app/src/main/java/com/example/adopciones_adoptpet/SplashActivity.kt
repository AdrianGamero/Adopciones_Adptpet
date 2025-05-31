package com.example.adopciones_adoptpet

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.adopciones_adoptpet.data.dataSource.FirebasePetDataSource
import com.example.adopciones_adoptpet.data.dataSource.RoomPetDataSource
import com.example.adopciones_adoptpet.data.database.AdoptPetDataBase
import com.example.adopciones_adoptpet.data.repository.PetRepositoryImpl
import com.example.adopciones_adoptpet.domain.useCase.AddPetUseCase
import com.example.adopciones_adoptpet.domain.useCase.GetBreedsByTypeUseCase
import com.example.adopciones_adoptpet.domain.useCase.SyncAndLoadUseCase
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
        val petRepository= PetRepositoryImpl(firebasePetDataSource,roomPetDataSource)
        val syncAndLoadUseCase= SyncAndLoadUseCase(petRepository)
        val getBreedsByTypeUseCase= GetBreedsByTypeUseCase(petRepository)
        val addPetUseCase = AddPetUseCase(petRepository)
        val petViewModel = PetViewModel(syncAndLoadUseCase,getBreedsByTypeUseCase, addPetUseCase)

        setContent {
            SplashScreen(petViewModel){
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }

    }
}