package ru.hse.software.construction.controller

import java.io.File

class LibraryController {
    fun isFilePathValid (
        path: String,
        fileName: String
    ) : Boolean {

        val filePath = "$path/$fileName"
        val file = File(filePath)

        try {
            if (!file.exists()) {
                // Пытаемся создать файл, если он не существует
                val isCreated = file.createNewFile()
                if (!isCreated) {
                    println("Ошибка при создании файла.")
                    return false
                }
                file.writeText("[]")
            }
        } catch (ex: Throwable) {
            println("Ошибка при создании файла: ${ex.message}")
            return false
        }

        return true
    }
}