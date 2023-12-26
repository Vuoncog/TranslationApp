package com.example.translator.data

import androidx.annotation.DrawableRes
import com.example.translator.R
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.chinese.ChineseTextRecognizerOptions
import com.google.mlkit.vision.text.devanagari.DevanagariTextRecognizerOptions
import com.google.mlkit.vision.text.japanese.JapaneseTextRecognizerOptions
import com.google.mlkit.vision.text.korean.KoreanTextRecognizerOptions
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

enum class Character(
    @DrawableRes val icon: Int,
    val type: String,
    val textRecognizer: TextRecognizer
) {
    Latin(R.drawable.latin, "Latin", TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)),
    Japanese(R.drawable.kanji, "Japanese", TextRecognition.getClient(JapaneseTextRecognizerOptions.Builder().build())),
    Chinese(R.drawable.chinese_letter, "Chinese", TextRecognition.getClient(ChineseTextRecognizerOptions.Builder().build())),
    Devanagari(R.drawable.devanagari, "Devanagari", TextRecognition.getClient(DevanagariTextRecognizerOptions.Builder().build())),
    Korean(R.drawable.hangul, "Korean", TextRecognition.getClient(KoreanTextRecognizerOptions.Builder().build()));

    companion object {
        fun getFromTextRecognizer(textRecognizer: TextRecognizer): Character = Character.values()
            .find { it.textRecognizer == textRecognizer } ?: Latin
    }
}