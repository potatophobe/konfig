package ru.potatophobe.konfig

/**
 * @param name environment variable name
 *
 * @return environment variable value or null
 * */
fun env(name: String): String? {
    return System.getenv(name)
}
