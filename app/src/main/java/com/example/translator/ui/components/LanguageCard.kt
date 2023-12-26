package com.example.translator.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.translator.data.Language

@Composable
fun LanguageCard(
    language: Language,
    modifier: Modifier = Modifier,
    textColor: Color = Color.Black
) {
    val jpModifier = if (language == Language.Japanese) Modifier.border(
        width = 0.1.dp,
        color = Color.Black.copy(0.7f),
        shape = CircleShape
    ) else Modifier
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Image(
            painter = painterResource(id = language.icon), contentDescription = "Language icon",
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .then(jpModifier),
            contentScale = ContentScale.FillHeight
        )
        Text(
            text = language.name,
            style = MaterialTheme.typography.titleMedium,
            color = textColor,
            modifier = Modifier.weight(1f)
        )
    }
}