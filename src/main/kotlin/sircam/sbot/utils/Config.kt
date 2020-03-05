package sircam.sbot.utils

import org.apache.commons.io.FileUtils
import org.json.JSONObject
import java.io.File
import kotlin.system.exitProcess

object Config {
    private val file = File("config.json")
    private val config: JSONObject

    init {
        if (file.exists()) {
            config = JSONObject(FileUtils.readFileToString(file, "UTF-8"))
            Logger.info("Loaded configuration")
        } else {
            Logger.error("${file.name} doesn't exist")
            exitProcess(1)
        }
    }

    val token = config.getString("token")
    val prefix = config.getString("prefix")
    val channel = config.getString("channel")
}