package com.example.translator.utils

import com.google.mlkit.nl.translate.TranslateLanguage

object Constant {
    val supportedLanguages = TranslateLanguage.getAllLanguages().toList()
//        listOf(
//        TranslateLanguage.CHINESE,
//        TranslateLanguage.ENGLISH,
//        TranslateLanguage.KOREAN,
//        TranslateLanguage.JAPANESE,
//        TranslateLanguage.VIETNAMESE,
//    )
}