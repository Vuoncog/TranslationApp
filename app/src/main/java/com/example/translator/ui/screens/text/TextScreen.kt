package com.example.translator.ui.screens.text

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.launch
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Autorenew
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.example.translator.ui.components.DownloadedLanguageDialog
import com.example.translator.ui.components.LanguageCard
import com.example.translator.utils.getDownloadedAllModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun TextScreen(
    navController: NavHostController,
    textViewModel: TextViewModel
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val uiState by textViewModel.textUiState.collectAsState()
    val speechToTextContract = rememberLauncherForActivityResult(
        contract = SpeechToTextContract(uiState.sourceLanguage),
        onResult = {
            if (it != null) {
                textViewModel.setText(it[0])
            }
        }
    )
    val sourceExpanded = remember {
        mutableStateOf(false)
    }

    val targetExpanded = remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = true) {
        getDownloadedAllModel {
            textViewModel.getDownloadedLanguageModel(
                models = it
            )
        }
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
                .fillMaxWidth()
                .weight(1f)
        ) {
            Row {
                LanguageCard(
                    language = uiState.sourceLanguage,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp)
                        .clickable {
                            sourceExpanded.value = true
                        }
                )
                IconButton(
                    modifier = Modifier,
                    onClick = {
                        speechToTextContract.launch()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Mic,
                        contentDescription = "Mic",
                        tint = Color.Black
                    )
                }
            }
            OutlinedTextField(
                value = uiState.text,
                onValueChange = {
                    textViewModel.setText(it)
                },
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
                    .weight(1f),
                placeholder = {
                    Text(
                        modifier = Modifier.alpha(0.8f),
                        text = "Input paragraph",
                    )
                },
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.Black,
                    containerColor = Color.Transparent,
                ),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                    }
                )
            )
        }

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            IconButton(
                modifier = Modifier.align(Alignment.Center),
                onClick = {
                    val index = uiState.sourceLanguage
                    textViewModel.setSourceLanguage(uiState.targetLanguage)
                    textViewModel.setTargetLanguage(index)
                    textViewModel.setText(uiState.translatedText)
                    textViewModel.translator()
                }
            ) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    imageVector = Icons.Default.Autorenew,
                    contentDescription = "Switch",
                    tint = Color.Black.copy(0.7f)
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Row {
                LanguageCard(
                    language = uiState.targetLanguage,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp)
                        .clickable {
                            targetExpanded.value = true
                        }
                )
                IconButton(
                    modifier = Modifier,
                    onClick = {

                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.VolumeUp,
                        contentDescription = "Speaker",
                        tint = Color.Black
                    )
                }
            }
            OutlinedTextField(
                value = uiState.translatedText,
                enabled = false,
                onValueChange = {
                    textViewModel.setText(it)
                },
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
                    .weight(1f),
                placeholder = {
                    Text(
                        modifier = Modifier.alpha(0.8f),
                        text = "Translation",
                    )
                },
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.Black,
                    containerColor = Color.Transparent,
                    disabledTextColor = Color.Black
                ),
            )
        }
        Button(
            onClick = {
                textViewModel.translator()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 16.dp,
                    vertical = 16.dp
                ),
            shape = RoundedCornerShape(4.dp)
        ) {
            Text(text = "Translate")
        }
    }

    if (sourceExpanded.value) {
        Dialog(onDismissRequest = { sourceExpanded.value = false }) {
            DownloadedLanguageDialog(
                downloadedList = uiState.models,
                expanded = sourceExpanded,
                onLanguageChanged = {
                    textViewModel.setSourceLanguage(it)
                },
                onDownloadClicked = {
                    textViewModel.getDownloadedLanguageModel(
                        models = uiState.models + it
                    )
                }
            )
        }
    }

    if (targetExpanded.value) {
        Dialog(onDismissRequest = { targetExpanded.value = false }) {
            DownloadedLanguageDialog(
                downloadedList = uiState.models,
                expanded = targetExpanded,
                onLanguageChanged = {
                    textViewModel.setTargetLanguage(it)
                },
                onDownloadClicked = {
                    textViewModel.getDownloadedLanguageModel(
                        models = uiState.models + it
                    )
                },
                isTargetLanguage = true
            )
        }
    }
}
