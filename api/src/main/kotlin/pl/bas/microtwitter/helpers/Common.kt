package pl.bas.microtwitter.helpers

import pl.bas.microtwitter.exceptions.MalformedURLException
import java.net.URL

fun isHexColor(color: String) = Regex("^#[0-9a-f]{6}\$", RegexOption.IGNORE_CASE).matches(color)

fun buildValidUrl(potentialUrl: String?): String {
    if (potentialUrl === null) {
        throw MalformedURLException("Url cannot be null")
    }

    var url: String = potentialUrl

    if (!url.startsWith("http://") && !url.startsWith("https://")) {
        url = "https://" + url
    }

    val urlObject =try {
        URL(url)
    } catch (e: java.net.MalformedURLException) {
        throw MalformedURLException("Url $url is invalid")
    }

    return urlObject.toExternalForm()
}