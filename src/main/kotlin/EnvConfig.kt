import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class EnvConfig(
    @Json(name = "key") val key: String,
    @Json(name = "value") val value: String,
    @Json(name = "paths") val paths: List<String>,
    @Json(name = "enable") val enable: Boolean
)