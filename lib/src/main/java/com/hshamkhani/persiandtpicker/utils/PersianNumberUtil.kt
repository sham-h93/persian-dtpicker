package com.hshamkhani.persiandtpicker.utils

import java.util.Locale


internal object PersianNumberUtils {

    fun Int.padZeroToStartWithPersianDigits(): String {
        val number =  if (this < 10) {
            this.toString().padStart(2, '0')
        } else {
            this.toString()
        }
        return number.formatToHindiIfLanguageIsFa()
    }

    fun String.formatToHindiIfLanguageIsFa(): String {
        return if (Locale.getDefault().language == "fa") {
            this.toHindiDigits()
        } else {
            this
        }
    }

    fun String.toHindiDigits(): String {
        val chars = this.toCharArray().map { char ->
            when (char) {
                '0' -> '۰'
                '1' ->  '۱'
                '2' ->  '۲'
                '3' ->  '۳'
                '4' ->  '۴'
                '5' ->  '۵'
                '6' ->  '۶'
                '7' ->  '۷'
                '8' ->  '۸'
                '9' ->  '۹'
                else -> '۰'
            }
        }.joinToString(separator = "")
        return chars
    }

    fun String.toArabicDigits(): Int {
        val chars = this.toCharArray().map { char ->
            when (char) {
                '۱' -> 1
                '۲' -> 2
                '۳' -> 3
                '۴' -> 4
                '۵' -> 5
                '۶' -> 6
                '۷' -> 7
                '۸' -> 8
                '۹' -> 9
                '۰' -> 0
                else -> char
            }
        }.joinToString(separator = "")
        return chars.toInt()
    }

}
