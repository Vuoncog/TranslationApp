package com.example.translator.data

import androidx.annotation.DrawableRes
import com.example.translator.R
import com.google.mlkit.nl.translate.TranslateLanguage

enum class Language(
    @DrawableRes val icon: Int,
    val tag: String
) {
    Afrikaans(R.drawable.afrikaans, TranslateLanguage.AFRIKAANS),
    Albanian(R.drawable.albanian, TranslateLanguage.ALBANIAN),
    Arabic((R.drawable.arabic),TranslateLanguage.ARABIC),
    Belarusian((R.drawable.belarusian),TranslateLanguage.BELARUSIAN),
    Bulgarian((R.drawable.bulgarian),TranslateLanguage.BULGARIAN),
    Bengali((R.drawable.bengali),TranslateLanguage.BENGALI),
    Catalan((R.drawable.catalan),TranslateLanguage.CATALAN),
    Chinese((R.drawable.chinese),TranslateLanguage.CHINESE),
    Croatian((R.drawable.croatian),TranslateLanguage.CROATIAN),
    Czech((R.drawable.czech),TranslateLanguage.CZECH),
    Danish((R.drawable.danish),TranslateLanguage.DANISH),
    Dutch((R.drawable.dutch),TranslateLanguage.DUTCH),
    English(R.drawable.england, TranslateLanguage.ENGLISH),
    Esperanto(R.drawable.esperanto, TranslateLanguage.ESPERANTO),
    Estonian(R.drawable.estonian, TranslateLanguage.ESTONIAN),
    Finnish(R.drawable.finnish, TranslateLanguage.FINNISH),
    French(R.drawable.french, TranslateLanguage.FRENCH),
    Galician(R.drawable.galician, TranslateLanguage.GALICIAN),
    Georgian(R.drawable.georgian, TranslateLanguage.GEORGIAN),
    German(R.drawable.german, TranslateLanguage.GERMAN),
    Greek(R.drawable.greek, TranslateLanguage.GREEK),
    Gujarati(R.drawable.gujarati, TranslateLanguage.GUJARATI),
    Haitian(R.drawable.haitian, TranslateLanguage.HAITIAN_CREOLE),
    Hebrew(R.drawable.hebrew, TranslateLanguage.HEBREW),
    Hindi(R.drawable.hindi, TranslateLanguage.HINDI),
    Hungarian(R.drawable.hungarian, TranslateLanguage.HUNGARIAN),
    Icelandic(R.drawable.icelandic, TranslateLanguage.ICELANDIC),
    Indonesian(R.drawable.indonesian, TranslateLanguage.INDONESIAN),
    Irish(R.drawable.irish, TranslateLanguage.IRISH),
    Italian(R.drawable.italian, TranslateLanguage.ITALIAN),
    Japanese(R.drawable.japan, TranslateLanguage.JAPANESE),
    Kannada(R.drawable.kannada, TranslateLanguage.KANNADA),
    Korean(R.drawable.korean, TranslateLanguage.KOREAN),
    Lithuanian(R.drawable.lithuanian, TranslateLanguage.LITHUANIAN),
    Latvian(R.drawable.latvian, TranslateLanguage.LATVIAN),
    Macedonian(R.drawable.macedonian, TranslateLanguage.MACEDONIAN),
    Marathi(R.drawable.marathi, TranslateLanguage.MARATHI),
    Malay(R.drawable.malay, TranslateLanguage.MALAY),
    Maltese(R.drawable.maltese, TranslateLanguage.MALTESE),
    Norwegian(R.drawable.norwegian, TranslateLanguage.NORWEGIAN),
    Persian(R.drawable.persian, TranslateLanguage.PERSIAN),
    Polish(R.drawable.polish, TranslateLanguage.POLISH),
    Portuguese(R.drawable.portuguese, TranslateLanguage.PORTUGUESE),
    Romanian(R.drawable.romanian, TranslateLanguage.ROMANIAN),
    Russian(R.drawable.russian, TranslateLanguage.RUSSIAN),
    Slovak(R.drawable.slovak, TranslateLanguage.SLOVAK),
    Slovenian(R.drawable.slovenian, TranslateLanguage.SLOVENIAN),
    Spanish(R.drawable.spanish, TranslateLanguage.SPANISH),
    Swedish(R.drawable.swedish, TranslateLanguage.SWEDISH),
    Swahili(R.drawable.swahili, TranslateLanguage.SWAHILI),
    Tagalog(R.drawable.tagalog, TranslateLanguage.TAGALOG),
    Tamil(R.drawable.tamil, TranslateLanguage.TAMIL),
    Telugu(R.drawable.telugu, TranslateLanguage.TELUGU),
    Thai(R.drawable.thai, TranslateLanguage.THAI),
    Turkish(R.drawable.turkish, TranslateLanguage.TURKISH),
    Ukrainian(R.drawable.ukrainian, TranslateLanguage.UKRAINIAN),
    Urdu(R.drawable.urdu, TranslateLanguage.URDU),
    Vietnamese(R.drawable.vietnam, TranslateLanguage.VIETNAMESE),
    Welsh(R.drawable.welsh, TranslateLanguage.WELSH);

    companion object {
        fun getFromTag(tag: String): Language = values().find { it.tag == tag } ?: English
    }
}