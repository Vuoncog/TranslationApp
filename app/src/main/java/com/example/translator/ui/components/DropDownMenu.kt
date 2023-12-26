package com.example.translator.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.translator.data.Character

@Composable
fun DropDownMenu(
    modifier: Modifier = Modifier,
    setCharacter: (Character) -> Unit,
    characterType: Character
) {
    val characterList = listOf(
        Character.Latin,
        Character.Chinese,
        Character.Devanagari,
        Character.Japanese,
        Character.Korean
    )

    var expanded by remember {
        mutableStateOf(false)
    }

    Box(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .clickable {
                    expanded = !expanded
                }
                .align(Alignment.CenterEnd),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            Text(text = "Character: ")
            Text(text = characterType.type)
        }
    }

    if (expanded) {
        Dialog(onDismissRequest = { expanded = !expanded }) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.White)
                    .padding(16.dp)
            ) {
                characterList.forEach { character ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier
                            .clickable {
                                expanded = !expanded
                                setCharacter(character)
                            }
                            .padding(8.dp)
                    ) {
                        Image(
                            painter = painterResource(id = character.icon),
                            contentDescription = character.type,
                            modifier = Modifier.size(24.dp)
                        )
                        Text(text = character.type)
                    }
                }

            }
        }
    }
}