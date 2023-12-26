package com.example.translator.ui.screens.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.translator.R

@Composable
fun MainScreen(
    shape: Shape = RoundedCornerShape(8.dp),
    onCameraClick: () -> Unit,
    onTextClick: () -> Unit,
    navigateToLanguage: () -> Unit,
    navigateToRealTime: () -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(modifier = Modifier.padding(bottom = 32.dp)) {
            Text(
                text = "Choose translator",
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(bottom = 32.dp)
                    .fillMaxWidth()
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Column(
                    modifier = Modifier
                        .clip(shape)
                        .weight(1f)
                        .clickable {
                            onCameraClick()
                        }
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.camera_translator),
                        contentDescription = "Camera translator",
                        contentScale = ContentScale.Fit
                    )
                    Text(
                        text = "Camera \n Translator",
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = Color(219, 233, 244))
                            .padding(vertical = 4.dp)
                    )
                }

                Column(
                    modifier = Modifier
                        .clip(shape)
                        .weight(1f)
                        .clickable {
                            onTextClick()
                        }
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.text_translator),
                        contentDescription = "Camera translator",
                        contentScale = ContentScale.Fit
                    )
                    Text(
                        text = "Text \n Translator",
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = Color(219, 233, 244))
                            .padding(vertical = 4.dp)
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Column(
                    modifier = Modifier
                        .clip(shape)
                        .weight(1f)
                        .clickable {
                            navigateToRealTime()
                        }
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.realtime_translator),
                        contentDescription = "Realtime translator",
                        contentScale = ContentScale.Fit
                    )
                    Text(
                        text = "Realtime \n Translator",
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = Color(219, 233, 244))
                            .padding(vertical = 4.dp)
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(shape)
                        .weight(1f)
                ) {

                }
            }

            Text(
                text = "See downloaded language",
                color = Color(0xFF6581BF),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .padding(top = 20.dp)
                    .clickable {
                        navigateToLanguage()
                    },
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Start
            )
        }
    }
}