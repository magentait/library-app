package ru.hse.software.construction.service

import ru.hse.software.construction.model.User
import ru.hse.software.construction.repository.UserRepository

// Пример системы аутентификации пользователей
interface Authenticator {
    fun authenticate(username: String, password: String): Boolean
}

class SimpleAuthenticator(
    repository: UserRepository,
    private var users: MutableList<User>
) : Authenticator {

    init {
        users = repository.loadUsers()
    }

    override fun authenticate(username: String, password: String): Boolean{
        val user = users.find {it.username == username}

        return user?.password == password
    }
}

interface Registrar {
    fun isUsernameAvailable(username: String): Boolean
    fun isPasswordStrong(password: String): Boolean
    fun register(username: String, password: String)
}

class SimpleRegistrar(
    private val repository: UserRepository,
    private var users: MutableList<User>
) : Registrar {

    init {
        users = repository.loadUsers()
    }

    override fun isUsernameAvailable(username: String): Boolean {
        return !(users.any { it.username == username})
    }

    override fun isPasswordStrong(password: String) : Boolean {
        return password.length >= 8
    }

    override fun register(username: String, password: String) {
        val newAccount = User(username, password)
        users.add(newAccount)
        repository.saveUsers(users)
    }
}