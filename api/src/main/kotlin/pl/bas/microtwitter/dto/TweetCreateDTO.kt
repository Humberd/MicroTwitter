package pl.bas.microtwitter.dto

data class TweetCreateDTO(
        var content: String,
        var inReplyToTweetId: Long? = null
)