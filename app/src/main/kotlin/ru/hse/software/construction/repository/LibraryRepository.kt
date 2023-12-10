package ru.hse.software.construction.repository

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.hse.software.construction.model.Book
import java.io.File

class FileLibraryException(
    filePath: String,
    override val message: String? = null,
    cause: Throwable? = null
) : RuntimeException("Проблема с файлом $filePath\n$message", cause)

// Интерфейс для работы с книгами
interface LibraryApp {
    fun addBook(book: Book)
    fun removeBook(title: String)
    fun getAllBooks(): List<Book>
    fun findBookByTitle(title: String): Book?
}

class LibraryRepository(
    path: String,
    fileName: String
) {

    private val filePath = "$path/$fileName"
    private val file = File(filePath)

    fun loadBooks() : MutableList<Book> {
        try {
            return if (file.exists()) {
                val jsonString = file.readText()
                Json.decodeFromString(jsonString)
            } else {
                mutableListOf()
            }
        } catch (ex: Throwable) {
            throw FileLibraryException(filePath, "Нельзя загрузить библиотеку.", ex)
        }
    }

    fun saveBooks(books : List<Book>) {
        try {
            val jsonString = Json.encodeToString(books)
            file.writeText(jsonString)
        } catch (ex: Throwable) {
            throw FileLibraryException(filePath, "Нельзя сохранить библиотеку.", ex)
        }
    }
}

// Реализация библиотеки книг в файле JSON
class JsonLibraryApp(
    private val repository: LibraryRepository,
    private var books: MutableList<Book>
) : LibraryApp {

    init {
        books = repository.loadBooks()
    }

    override fun addBook(book: Book) {
        books.add(book)
        repository.saveBooks(books)
    }

    override fun removeBook(title: String) {
        books.removeIf { it.title == title }
        repository.saveBooks(books)
    }

    override fun getAllBooks(): List<Book> {
        return books.toList()
    }

    override fun findBookByTitle(title: String): Book? {
        return books.find { it.title == title }
    }
}