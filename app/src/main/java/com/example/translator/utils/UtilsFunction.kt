package com.example.translator.utils

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.translator.data.Language
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.common.model.RemoteModelManager
import com.google.mlkit.nl.languageid.LanguageIdentification
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.TranslateRemoteModel
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions

fun setLanguage(tag: String): Language {
    return Language.getFromTag(tag)
}

fun downloadModel(
    languageTag: String,
    context: Context,
    onSuccess: (String) -> Unit
) {
    val modelManager = RemoteModelManager.getInstance()
    val model = TranslateRemoteModel.Builder(languageTag).build()
    val conditions = DownloadConditions.Builder()
        .requireWifi()
        .build()
    modelManager.download(model, conditions)
        .addOnSuccessListener {
            Toast.makeText(
                context,
                "Download ${setLanguage(languageTag).name} successfully",
                Toast.LENGTH_SHORT
            ).show()
            onSuccess(languageTag)
        }
        .addOnFailureListener {
            Toast.makeText(
                context,
                "Download ${setLanguage(languageTag).name} fail",
                Toast.LENGTH_SHORT
            ).show()
        }
}

fun convertToLanguageTag(language: Language): String {
    return language.tag
}

fun languageIdentifier(
    text: String,
    onSuccess: (String) -> Unit
) {
    val languageIdentifier = LanguageIdentification.getClient()
    languageIdentifier.identifyPossibleLanguages(text)
        .addOnSuccessListener { identifiedLanguages ->
            for (identifiedLanguage in identifiedLanguages) {
                val language = identifiedLanguage.languageTag
                val tag = TranslateLanguage.fromLanguageTag(language)
                if (tag != null) {
                    onSuccess(tag)
                }
                val confidence = identifiedLanguage.confidence
            }
        }
        .addOnFailureListener {

        }
}

fun textTranslator(
    sourceLanguageTag: String,
    targetLanguageTag: String,
    text: String,
    onSuccess: (String) -> Unit
) {
    val options = TranslatorOptions.Builder()
        .setSourceLanguage(sourceLanguageTag)
        .setTargetLanguage(targetLanguageTag)
        .build()
    val translator = Translation.getClient(options)
    translator.translate(text).addOnSuccessListener {
        onSuccess(it)
    }
}

fun getDownloadedAllModel(
    onSuccess: (List<String>) -> Unit
) {
    val modelManager = RemoteModelManager.getInstance()
    val modelList = mutableListOf<String>()
    modelManager.getDownloadedModels(TranslateRemoteModel::class.java)
        .addOnSuccessListener { models ->
            models.forEach { model ->
                modelList += model.language
            }
            onSuccess(modelList.toList())
        }
        .addOnFailureListener {
            // Error.
        }
}

fun removeModel(
    languageTag: String,
    context: Context,
    onSuccess: (String) -> Unit
) {
    val modelManager = RemoteModelManager.getInstance()
    val model = TranslateRemoteModel.Builder(languageTag).build()
    modelManager.deleteDownloadedModel(model)
        .addOnSuccessListener {
            Toast.makeText(
                context,
                "Deleted ${setLanguage(languageTag).name}",
                Toast.LENGTH_SHORT
            ).show()
            onSuccess(languageTag)
        }
}

fun boxInfo(rotate: Int, textBlock: android.graphics.Rect): Pair<Int, Int> {
    return when (rotate) {
        90, 270 -> {
            val width = textBlock.width()
            val height = textBlock.height()
            Pair(width, height)
        }

        else -> {
            val width = textBlock.height()
            val height = textBlock.width()
            Pair(width, height)
        }
    }
}
fun offsetInfo(
    rotate: Int,
    textBlock: android.graphics.Rect,
    width: Int, height: Int
): Pair<Int, Int> {
    return when (rotate) {
        90 -> {
            val offsetX = textBlock.left
            val offsetY = textBlock.bottom
            Pair(offsetX, offsetY)
        }
        180 -> {
            val offsetX = textBlock.top
            val offsetY = height - textBlock.right
            Pair(offsetX, offsetY)
        }
        270 -> {
            val offsetX = width - textBlock.right
            val offsetY = height - textBlock.bottom
            Pair(offsetX, offsetY)
        }
        else -> {
            val offsetX = width - textBlock.top
            val offsetY = textBlock.left
            Pair(offsetX, offsetY)
        }
    }
}
fun textRotate(rotate: Float): Float{
    return when (rotate){
        180f -> 270f
        0f -> 90f
        else -> rotate - 90
    }
}


