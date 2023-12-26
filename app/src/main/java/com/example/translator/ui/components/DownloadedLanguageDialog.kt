package com.example.translator.ui.components

import android.content.Context
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import com.example.translator.utils.Constant.supportedLanguages
import com.example.translator.data.Language
import com.example.translator.utils.downloadModel
import com.example.translator.utils.setLanguage
import java.util.Locale

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DownloadedLanguageDialog(
    downloadedList: List<String>,
    expanded: MutableState<Boolean>,
    onLanguageChanged: (Language) -> Unit,
    onDownloadClicked: (String) -> Unit,
    isTargetLanguage: Boolean = false
) {
    val context = LocalContext.current
    val systemLanguage = Locale.getDefault().language
    val languageList =
        if (isTargetLanguage) remember { supportedLanguages }
        else {
            remember { supportedLanguages - systemLanguage }
        }

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .fillMaxHeight(0.5f)
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White)
            .padding(16.dp)
    ) {
        if (isTargetLanguage){
            stickyHeader {
                LanguageInfo(
                    language = systemLanguage,
                    context = context,
                    isDownload = downloadedList.contains(systemLanguage),
                    onDownloadClicked = onDownloadClicked,
                    onCardClicked = {
                        expanded.value = false
                        onLanguageChanged(setLanguage(systemLanguage))
                    }
                )
            }
        }
        items(
            count = languageList.size,
            key = {
                languageList[it]
            }
        ) { index ->
            val language = languageList[index]
            LanguageInfo(
                language = language,
                context = context,
                isDownload = downloadedList.contains(language),
                onDownloadClicked = onDownloadClicked,
                onCardClicked = {
                    expanded.value = false
                    onLanguageChanged(setLanguage(language))
                }
            )
        }

    }
}


@Composable
fun LanguageInfo(
    language: String,
    context: Context,
    isDownload: Boolean,
    onDownloadClicked: (String) -> Unit,
    onCardClicked: () -> Unit
) {
    val status = remember {
        mutableStateOf("Not downloaded")
    }
    val statusColor = remember {
        mutableStateOf(Color(0xFFD9D9D9))
    }
    if (isDownload) {
        status.value = "Downloaded"
        statusColor.value = Color(0xFF90EE91)
    }
    Column(
        modifier = Modifier
            .clickable(enabled = status.value == "Downloaded") {
                onCardClicked()
            }
            .background(color = Color.White)
            .padding(vertical = 4.dp)
    ) {
        LanguageCard(language = setLanguage(language))
        Row {
            Text(
                text = status.value,
                color = statusColor.value,
                style = MaterialTheme.typography.bodyMedium,
                fontStyle = FontStyle.Italic,
                modifier = Modifier
                    .padding(top = 4.dp)
                    .weight(1f)
            )
            if (status.value == "Not downloaded") {
                Icon(
                    imageVector = Icons.Default.Download,
                    contentDescription = "download",
                    tint = Color.Black.copy(0.7f),
                    modifier = Modifier
                        .size(16.dp)
                        .clickable {
                            status.value = "Downloading"
                            downloadModel(
                                languageTag = language,
                                context = context,
                                onSuccess = {
                                    statusColor.value = Color(0xFF90EE91)
                                    status.value = "Downloaded"
                                    onDownloadClicked(it)
                                }
                            )
                        }
                )
            }
        }
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            color = Color.Black.copy(0.1f)
        )
    }
}