package me.nathanfallet.suitebde.plugins

import com.github.aymanizz.ktori18n.i18n
import freemarker.cache.ClassTemplateLoader
import io.ktor.server.application.*
import io.ktor.server.freemarker.*
import me.nathanfallet.ktorx.directives.TDirective

fun Application.configureTemplating() {
    install(FreeMarker) {
        templateLoader = ClassTemplateLoader(this::class.java.classLoader, "templates")
        setSharedVariable("t", TDirective(i18n))
    }
}
