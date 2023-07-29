package br.com.joaovq.settings_presentation.factory

class SocialMedia(
    val url: String,
)

object SocialMediaFactory {
    private const val GITHUB_URL = "https://github.com/joaovq"
    private const val LINKEDIN_URL = "https://www.linkedin.com/in/joaovitorqueiroz/"
    val github = SocialMedia(
        GITHUB_URL,
    )
    val linkedin = SocialMedia(
        LINKEDIN_URL,
    )
}
