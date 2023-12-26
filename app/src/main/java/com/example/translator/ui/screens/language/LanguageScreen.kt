package com.example.translator.ui.screens.language

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.translator.ui.components.LanguageCard
import com.example.translator.utils.downloadModel
import com.example.translator.utils.removeModel
import com.example.translator.utils.setLanguage

@Composable
fun LanguageScreen(
    languageViewModel: LanguageViewModel,
    navController: NavHostController,
) {
    val languageUiState by languageViewModel.languageUiState.collectAsState()
    val downloadedModels = languageUiState.downloadedLanguages
    val notDownloadedModels = languageUiState.notDownloadedLanguages
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        IconButton(
            modifier = Modifier,
            onClick = {
                navController.popBackStack()
            }
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Camera switch",
                tint = Color.Black
            )
        }
        Column {
            LanguageList(
                languages = downloadedModels,
                isDownloaded = true,
                icon = Icons.Default.Close,
                onTrailingIcon = {
                    Toast.makeText(
                        context,
                        "Removing ${setLanguage(it).name}",
                        Toast.LENGTH_SHORT
                    ).show()
                    removeModel(
                        languageTag = it,
                        context = context,
                        onSuccess = { tag ->
                            languageViewModel.getDownloadedLanguageModels(
                                downloadedLanguages = languageUiState.downloadedLanguages - tag
                            )
                        }
                    )
                }
            )
            LanguageList(
                languages = notDownloadedModels,
                isDownloaded = false,
                icon = Icons.Default.Download,
                onTrailingIcon = {
                    Toast.makeText(
                        context,
                        "Downloading ${setLanguage(it).name}",
                        Toast.LENGTH_SHORT
                    ).show()
                    downloadModel(
                        languageTag = it,
                        context = context,
                        onSuccess = { tag ->
                            languageViewModel.getDownloadedLanguageModels(
                                downloadedLanguages = languageUiState.downloadedLanguages + tag
                            )
                        }
                    )
                }
            )
        }
    }
}

@Composable
fun LanguageList(
    languages: List<String>,
    isDownloaded: Boolean,
    icon: ImageVector,
    onTrailingIcon: (String) -> Unit
) {
    val text = if (isDownloaded) "Downloaded" else "Not downloaded"

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight(400),
            modifier = Modifier.padding(bottom = 8.dp),
            color = Color(0xFF90EE91)
        )
        languages.forEach { language ->
            Row(
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                LanguageCard(
                    language = setLanguage(tag = language),
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = {
                    onTrailingIcon(language)
                }) {
                    Icon(
                        imageVector = icon,
                        contentDescription = "Delete",
                        tint = Color.Black.copy(0.7f),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}