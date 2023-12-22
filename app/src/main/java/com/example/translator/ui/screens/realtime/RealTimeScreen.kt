package com.example.translator.ui.screens.realtime

import android.content.Context
import android.view.ViewGroup
import androidx.camera.core.AspectRatio
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.example.translator.ui.components.LanguageCard
import com.example.translator.ui.screens.camera.CameraPreview
import com.example.translator.utils.Language
import com.example.translator.utils.setLanguage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RealTimeScreen(
    realtimeViewModel: RealtimeViewModel
) {
    val context: Context = LocalContext.current
    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
    val uiState by realtimeViewModel.realtimeUiState.collectAsState()
    val cameraController: LifecycleCameraController =
        remember { LifecycleCameraController(context) }
    var detectedText by remember { mutableStateOf("No text detected yet..") }
    var expanded by remember { mutableStateOf(false) }

    fun onTextUpdated(updatedText: String) {
        detectedText = updatedText
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { TopAppBar(title = { Text("Text Scanner") }) },
    ) { paddingValues: PaddingValues ->
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = androidx.compose.ui.Alignment.BottomCenter
        ) {

            AndroidView(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                factory = { context ->
                    PreviewView(context).apply {
                        setBackgroundColor(Color.Black.toArgb())
                        implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                        scaleType = PreviewView.ScaleType.FILL_START
                    }.also { previewView ->
                        startTextRecognition(
                            context = context,
                            cameraController = cameraController,
                            lifecycleOwner = lifecycleOwner,
                            previewView = previewView,
                            sourceLanguage = uiState.sourceLanguage,
                            targetLanguage = uiState.targetLanguage,
                            onDetectedTextUpdated = ::onTextUpdated,
                            downloadLanguages = uiState.downloadedLanguages,
                            onDetectedLanguageUpdated = {
                                realtimeViewModel.setSourceLanguage(
                                    language = setLanguage(it)
                                )
                            }
                        )
                    }
                }
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                LanguageCard(language = uiState.targetLanguage,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { expanded = true })
                Text(
                    text = detectedText,
                )
            }
        }
    }

    if (expanded) {
        Dialog(onDismissRequest = { expanded = false }) {
            RealtimeDialog(
                downloadLanguages = uiState.downloadedLanguages,
                onLanguageClicked = {
                    realtimeViewModel.setTargetLanguage(it)
                    expanded = false
                }
            )
        }
    }
}

@Composable
fun RealtimeDialog(
    downloadLanguages: List<String>,
    onLanguageClicked: (Language) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .fillMaxHeight(0.5f)
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White)
            .padding(16.dp)
    ) {
        items(downloadLanguages) { language ->
            LanguageCard(language = setLanguage(language),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clickable {
                        onLanguageClicked(setLanguage(language))
                    }
            )
        }
    }
}

private fun startTextRecognition(
    context: Context,
    cameraController: LifecycleCameraController,
    lifecycleOwner: LifecycleOwner,
    previewView: PreviewView,
    onDetectedTextUpdated: (String) -> Unit,
    onDetectedLanguageUpdated: (String) -> Unit,
    sourceLanguage: Language,
    targetLanguage: Language,
    downloadLanguages: List<String>
) {

    cameraController.imageAnalysisTargetSize = CameraController.OutputSize(Int.MAX_VALUE)
    cameraController.setImageAnalysisAnalyzer(
        ContextCompat.getMainExecutor(context),
        TextRecognitionAnalyzer(
            onDetectedTextUpdated = onDetectedTextUpdated,
            onDetectedLanguageUpdated = onDetectedLanguageUpdated,
            sourceLanguage = sourceLanguage,
            targetLanguage = targetLanguage,
            downloadedLanguages = downloadLanguages
        )
    )

    cameraController.bindToLifecycle(lifecycleOwner)
    previewView.controller = cameraController
}