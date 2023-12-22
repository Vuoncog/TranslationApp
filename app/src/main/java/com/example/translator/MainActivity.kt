package com.example.translator

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.translator.navigation.SetUpNavigation
import com.example.translator.ui.screens.camera.CameraViewModel
import com.example.translator.ui.screens.language.LanguageViewModel
import com.example.translator.ui.screens.realtime.RealtimeViewModel
import com.example.translator.ui.screens.text.TextViewModel
import com.example.translator.ui.theme.TranslatorTheme
import com.example.translator.utils.getDownloadedAllModel
import java.util.*

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

    fun hasRequiredPermission(context: Context) = PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            context, it
        ) == PackageManager.PERMISSION_GRANTED
    }
}