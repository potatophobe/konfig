package ru.potatophobe.konfig

/**
 * @return environment variable value or null
 * */
fun env(name: String): String? {
    return System.getenv(name)
}
