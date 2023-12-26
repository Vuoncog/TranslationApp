package com.example.translator.ui.screens.realtime

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.example.translator.navigation.Screen
import com.example.translator.ui.components.DropDownMenu
import com.example.translator.ui.components.LanguageCard
import com.example.translator.data.Character
import com.example.translator.data.Language
import com.example.translator.utils.getDownloadedAllModel
import com.example.translator.utils.setLanguage
import java.util.Locale

@Composable
fun RealTimeBoardScreen(
    realtimeViewModel: RealtimeViewModel,
    navController: NavHostController
) {
    val uiState by realtimeViewModel.realtimeUiState.collectAsState()
    val sysLanCheck = uiState.downloadedLanguages.contains(Locale.getDefault().language)
    val expanded = remember { mutableStateOf(false) }

    LaunchedEffect(key1 = true) {
        getDownloadedAllModel(
            onSuccess = {
                realtimeViewModel.getDownloadedLanguageModels(it)
            }
        )
    }

    Column(
        modifier = Modifier.fillMaxSize(),
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
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(end = 16.dp),
        ) {
            DropDownMenu(
                modifier = Modifier.padding(16.dp),
                setCharacter = {
                    realtimeViewModel.setTextRecognizerOptions(
                        textRecognizer = it.textRecognizer
                    )
                },
                characterType = Character.getFromTextRecognizer(uiState.textRecognition)
            )
            Text(
                modifier = Modifier.padding(16.dp),
                text = "Please ensure that the ${
                    Character.getFromTextRecognizer(uiState.textRecognition).name
                } language has been downloaded.",
                color = Color.Black.copy(0.7f),
                fontStyle = FontStyle.Italic,
                style = MaterialTheme.typography.titleMedium
            )
            if (!sysLanCheck) {
                Text(
                    modifier = Modifier.padding(16.dp),
                    text = "Your language (${Language.getFromTag(Locale.getDefault().language)}) is not downloaded.",
                    color = Color(0xFFE15566),
                    fontStyle = FontStyle.Italic,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Text(text = "Language translation")
            LanguageCard(
                language = uiState.targetLanguage,
                modifier = Modifier.clickable {
                    expanded.value = true
                }
            )
        }

        Button(
            onClick = {
                navController.navigate(Screen.RealTime.addArgument("analysis"))
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 16.dp,
                    vertical = 16.dp
                ),
            shape = RoundedCornerShape(4.dp)
        ) {
            Text(text = "Start realtime translator")
        }
    }

    if (expanded.value) {
        Dialog(onDismissRequest = { expanded.value = false }) {
            LanguageDialog(
                downloadedLanguages = uiState.downloadedLanguages,
                expanded = expanded,
                onLanguageClicked = {
                    realtimeViewModel.setTargetLanguage(it)
                }
            )
        }
    }
}

@Composable
fun LanguageDialog(
    downloadedLanguages: List<String>,
    expanded: MutableState<Boolean>,
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
        items(downloadedLanguages) { downloadedLanguage ->
            LanguageCard(
                language = setLanguage(downloadedLanguage),
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .clickable {
                        expanded.value = false
                        onLanguageClicked(setLanguage(downloadedLanguage))
                    },
                )
        }
    }
}