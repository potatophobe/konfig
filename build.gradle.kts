plugins {
    id("io.codearte.nexus-staging")
}

nexusStaging {
    val ossrhUsername: String by project
    val ossrhPassword: String by project

    packageGroup = "ru.potatophobe"
    serverUrl = "https://s01.oss.sonatype.org/service/local/"
    username = ossrhUsername
    password = ossrhPassword
}
