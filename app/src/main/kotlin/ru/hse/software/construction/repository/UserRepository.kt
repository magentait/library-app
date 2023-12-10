package ru.hse.software.construction.repository

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.hse.software.construction.model.User
import java.io.File

class FileUserException(
    filePath: String,
    override val message: String? = null,
    cause: Throwable? = null
) : RuntimeException("Проблема с файлом $filePath\n$message", cause)

class UserRepository(
    path: String,
    fileName: String
) {

    private val filePath = "$path/$fileName"
    private val file = File(filePath)

    fun loadUsers() : MutableList<User> {
        try {
            return if (file.exists()) {
                val jsonString = file.readText()
                Json.decodeFromString(jsonString)
            } else {
                mutableListOf()
            }
        } catch (ex: Throwable) {
            throw FileUserException(filePath, "Нельзя загрузить пользователей", ex)
        }
    }

    fun saveUsers(users: List<User>) {
        try {
            val jsonString = Json.encodeToString(users)
            file.writeText(jsonString)
        } catch (ex: Throwable) {
            throw FileUserException(filePath, "Нельзя сохранить пользователей", ex)
        }
    }
}