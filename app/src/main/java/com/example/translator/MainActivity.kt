package com.example.translator

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.translator.navigation.SetUpNavigation
import com.example.translator.ui.screens.camera.CameraViewModel
import com.example.translator.ui.screens.language.LanguageViewModel
import com.example.translator.ui.screens.realtime.RealtimeViewModel
import com.example.translator.ui.screens.text.TextViewModel
import com.example.translator.ui.theme.TranslatorTheme
import com.example.translator.utils.getDownloadedAllModel

class MainActivity : ComponentActivity() {
    private lateinit var navController: NavHostController
    private val cameraViewModel: CameraViewModel by viewModels()
    private val textViewModel: TextViewModel by viewModels()
    private val languageViewModel: LanguageViewModel by viewModels()
    private val realtimeViewModel: RealtimeViewModel by viewModels()

    init {
        getDownloadedAllModel {
            languageViewModel.getDownloadedLanguageModels(it)
            realtimeViewModel.getDownloadedLanguageModels(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!hasRequiredPermission(this)) {
            ActivityCompat.requestPermissions(
                this,
                PERMISSIONS,
                0
            )
        }
        setContent {
            navController = rememberNavController()
            TranslatorTheme {
                SetUpNavigation(
                    navController = navController,
                    context = this,
                    cameraViewModel = cameraViewModel,
                    textViewModel = textViewModel,
                    languageViewModel = languageViewModel,
                    realtimeViewModel = realtimeViewModel
                )
            }
        }
    }

    companion object {
        val PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
        )
    }

    private fun hasRequiredPermission(context: Context) = PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            context, it
        ) == PackageManager.PERMISSION_GRANTED
    }
}