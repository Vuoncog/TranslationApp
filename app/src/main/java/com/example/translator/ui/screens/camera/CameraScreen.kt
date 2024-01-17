package com.example.translator.ui.screens.camera

import android.content.Context
import android.graphics.Bitmap
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.translator.R
import com.example.translator.navigation.Screen
import com.example.translator.ui.components.DropDownMenu
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognizer


@Composable
fun CameraScreen(
    context: Context,
    navController: NavHostController,
    cameraViewModel: CameraViewModel
) {
    val recognizer by cameraViewModel.character.collectAsState()
    val bitmap by cameraViewModel.bitmap.collectAsState()
    val uiState by cameraViewModel.cameraUiState.collectAsState()
    val contract = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = {
            if (it != null) {
                val bitmapConverter = MediaStore.Images.Media.getBitmap(context.contentResolver, it)
                cameraViewModel.assignPhoto(bitmapConverter)
            }
        })
    val image = ImageRequest.Builder(context)
        .data(bitmap)
        .error(R.drawable.placeholder)
        .placeholder(R.drawable.placeholder)
        .crossfade(true)
        .build()

    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
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
            DropDownMenu(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 16.dp),
                setCharacter = {
                    cameraViewModel.assignCharacter(it)
                },
                characterType = uiState.char
            )
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier
                .weight(1f)
                .padding(top = 28.dp),
        ) {
            AsyncImage(
                model = image, contentDescription = "Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 64.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedButton(
                onClick = {
                    contract.launch(
                        PickVisualMediaRequest(
                            ActivityResultContracts.PickVisualMedia.ImageOnly
                        )
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 64.dp),
                shape = RoundedCornerShape(4.dp)
            ) {
                Text(text = "Upload from gallery")
            }

            Text(
                text = "OR",
                color = Color.Gray,
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            OutlinedButton(
                onClick = {
                    navController.navigate(Screen.Camera.addArgument("capture"))
                },
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 64.dp),
                shape = RoundedCornerShape(4.dp)
            ) {
                Text(text = "Capture photos")
            }
        }
        Button(
            onClick = {
                imageProcessing(
                    bitmap = bitmap,
                    recognizer = recognizer,
                    cameraViewModel = cameraViewModel,
                    context = context,
                    navController = navController
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 32.dp,
                    vertical = 16.dp
                ),
            shape = RoundedCornerShape(4.dp)
        ) {
            Text(text = "Translate")
        }
    }
}

fun imageProcessing(
    bitmap: Bitmap?,
    recognizer: TextRecognizer,
    cameraViewModel: CameraViewModel,
    context: Context,
    navController: NavHostController
) {
    if (bitmap != null) {
        val inputImage = InputImage.fromBitmap(bitmap, 0)
        val result = recognizer.process(inputImage)
            .addOnSuccessListener { visionText ->
                cameraViewModel.recognizeText(visionText.text)
                cameraViewModel.setSourceLanguage()
            }
            .addOnFailureListener { _ ->
                Toast.makeText(
                    context,
                    "Can't recognize this image",
                    Toast.LENGTH_LONG
                ).show()
            }
        navController.navigate(Screen.Camera.addArgument("translator"))
    } else {
        Toast.makeText(
            context,
            "Please add image",
            Toast.LENGTH_LONG
        ).show()
    }
}

