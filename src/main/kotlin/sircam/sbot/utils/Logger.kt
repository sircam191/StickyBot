package sircam.sbot.utils

import com.github.ajalt.mordant.AnsiColorCode
import com.github.ajalt.mordant.TermColors
import java.time.LocalTime

object Logger {
    init {
        with(TermColors()) {
            println(blue("╔═══════════════════════════╗"))
            println(blue("║    ${brightWhite("Developed by Atakku")}    ║"))
            println(blue("║ ${brightWhite("https://atakku.kuki.town/")} ║"))
            println(blue("╚═══════════════════════════╝"))
        }
    }

    fun info(log: String) = log(Level.INFO, log)
    fun warn(log: String) = log(Level.WARN, log)
    fun error(log: String) = log(Level.ERROR, log)

    private fun log(lvl: Level, log: String) = println("${LocalTime.now()} [${lvl(lvl.name)}] <${TermColors().magenta(Thread.currentThread().stackTrace[3].className)}> ${lvl(log)}")

    private enum class Level(val color: AnsiColorCode) {
        INFO(TermColors().white),
        WARN(TermColors().yellow),
        ERROR(TermColors().red);
        operator fun invoke(text: String): String = color(text)
    }

}