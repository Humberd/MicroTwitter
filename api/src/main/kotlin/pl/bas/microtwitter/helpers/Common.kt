package pl.bas.microtwitter.helpers

fun isHexColor(color: String) = Regex("^#[0-9a-f]{6}\$", RegexOption.IGNORE_CASE).matches(color)
