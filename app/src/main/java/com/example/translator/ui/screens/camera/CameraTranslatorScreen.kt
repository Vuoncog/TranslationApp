package com.example.translator.ui.screens.camera

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.translator.ui.components.DownloadedLanguageDialog
import com.example.translator.ui.components.LanguageCard
import com.example.translator.utils.convertToLanguageTag
import com.example.translator.utils.downloadModel
import com.example.translator.utils.getDownloadedAllModel
import com.example.translator.utils.setLanguage
import java.util.Locale

@Composable
fun CameraTranslatorScreen(
    cameraViewModel: CameraViewModel,
    onBackClicked: () -> Unit,
    context: Context
) {
    val text by cameraViewModel.textRecognition.collectAsState()
    val uiState by cameraViewModel.cameraUiState.collectAsState()
    LaunchedEffect(key1 = uiState.sourceLanguage) {
        cameraViewModel.checkModel()
        getDownloadedAllModel {
            cameraViewModel.getDownloadedLanguageModel(
                models = it
            )
        }
    }

    LaunchedEffect(key1 = uiState.models) {
        cameraViewModel.checkModel()
        cameraViewModel.translator(text)
    }

    LaunchedEffect(key1 = text) {
        cameraViewModel.translator(text)
    }

    val expanded = remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        IconButton(
            modifier = Modifier,
            onClick = {
                onBackClicked()
                cameraViewModel.setTargetLanguage(setLanguage(Locale.getDefault().language))
                cameraViewModel.resetTranslatedText()
            }
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Camera switch",
                tint = Color.Black
            )
        }
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 16.dp
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                LanguageCard(
                    language = uiState.sourceLanguage,
                    modifier = Modifier
                        .padding(
                            vertical = 6.dp
                        )
                        .weight(1f),
                    textColor = if (cameraViewModel.isDownloaded(uiState.sourceLanguage)) Color.Black
                    else Color(0xFFDA2C43)
                )
                if (!cameraViewModel.isDownloaded(uiState.sourceLanguage)) {
                    Column(
                        verticalArrangement = Arrangement.SpaceAround,
                        horizontalAlignment = Alignment.End,
                        modifier = Modifier.padding(start = 20.dp)
                    ) {
                        Text(
                            text = "${uiState.sourceLanguage.name} is not available",
                            style = MaterialTheme.typography.titleSmall,
                            color = Color.Black.copy(0.7f),
                            fontStyle = FontStyle.Italic
                        )

                        Text(
                            text = "Click to download",
                            style = MaterialTheme.typography.titleSmall,
                            modifier = Modifier.clickable {
                                Toast.makeText(
                                    context,
                                    "${uiState.sourceLanguage.name} is downloading",
                                    Toast.LENGTH_SHORT
                                ).show()
                                downloadModel(
                                    languageTag = convertToLanguageTag(uiState.sourceLanguage),
                                    context = context,
                                    onSuccess = {
                                        Toast.makeText(
                                            context,
                                            "Download ${uiState.sourceLanguage.name} successfully",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        cameraViewModel.getDownloadedLanguageModel(
                                            models = uiState.models + it
                                        )
                                    }
                                )
                            }
                        )
                    }
                }
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .background(Color.Gray.copy(alpha = 0.3f))
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = text,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 16.dp
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                LanguageCard(
                    language = uiState.targetLanguage,
                    modifier = Modifier
                        .padding(
                            vertical = 6.dp
                        )
                        .clickable {
                            expanded.value = true
                        }
                        .weight(1f),
                    textColor = if (cameraViewModel.isDownloaded(uiState.targetLanguage)) Color.Black
                    else Color(0xFFDA2C43)
                )
                if (!cameraViewModel.isDownloaded(uiState.targetLanguage)) {
                    Column(
                        verticalArrangement = Arrangement.SpaceAround,
                        horizontalAlignment = Alignment.End,
                        modifier = Modifier.padding(start = 20.dp)
                    ) {
                        Text(
                            text = "${uiState.targetLanguage.name} is not available",
                            style = MaterialTheme.typography.titleSmall,
                            color = Color.Black.copy(0.7f),
                            fontStyle = FontStyle.Italic
                        )

                        Text(
                            text = "Click to download",
                            style = MaterialTheme.typography.titleSmall,
                            modifier = Modifier.clickable {
                                Toast.makeText(
                                    context,
                                    "${uiState.targetLanguage.name} is downloading",
                                    Toast.LENGTH_SHORT
                                ).show()
                                downloadModel(
                                    languageTag = convertToLanguageTag(uiState.targetLanguage),
                                    context = context,
                                    onSuccess = {
                                        Toast.makeText(
                                            context,
                                            "Download ${uiState.targetLanguage.name} successfully",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        cameraViewModel.getDownloadedLanguageModel(
                                            models = uiState.models + it
                                        )
                                    }
                                )
                            }
                        )
                    }
                }
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .background(Color.Gray.copy(alpha = 0.3f))
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = if (uiState.translatorCheck) uiState.translatedText
                    else {
                        "Can't translate"
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        }
    }

    if (expanded.value) {
        Dialog(onDismissRequest = { expanded.value = false }) {
            DownloadedLanguageDialog(
                downloadedList = uiState.models,
                expanded = expanded,
                onLanguageChanged = {
                    cameraViewModel.setTargetLanguage(it)
                    cameraViewModel.resetTranslatedText()
                    cameraViewModel.checkModel()
                    cameraViewModel.translator(text)
                },
                onDownloadClicked = {
                    cameraViewModel.getDownloadedLanguageModel(
                        models = uiState.models + it
                    )
                },
                isTargetLanguage = true
            )
        }
    }
}

