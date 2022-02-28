import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection
import java.io.File

fun main() {
    val configFile = File("default.envconfig")

    if (!configFile.exists()) {
        println("配置文件default.envconfig不存在,请检查")
        println(configFile.absolutePath)
        System.`in`.read()
        return
    }

    val jsonAdapter = Moshi.Builder()
        .build()
        .adapter<List<EnvConfig>>(Types.newParameterizedType(List::class.java, EnvConfig::class.java))

    val json = configFile.readText()

    val configList = try {
        jsonAdapter.fromJson(json)
    } catch (e: Exception) {
        println("配置文件default.envconfig内容错误,请检查")
        System.`in`.read()
        return
    } ?: return

    val pathList = mutableListOf<String>()

    configList
        .filter { it.enable }
        .forEach { config ->
            Runtime.getRuntime().exec("setx /M \"${config.key}\" \"${config.value}\"")

            pathList.addAll(config.paths.map { "%${config.key}%\\$it" })

            println("已设置环境变量${config.key}为${config.value}")
        }

    val pathStr = pathList.joinToString(";", prefix = ";")

    Toolkit.getDefaultToolkit().systemClipboard.setContents(StringSelection(pathStr), null)

    println("所有环境变量已设置,path数据已复制到剪切板。按任意键退出")

    System.`in`.read()
}