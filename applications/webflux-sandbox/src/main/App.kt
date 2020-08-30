package fr.amanin.sis.applications.webflux.sandbox

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.fu.kofu.reactiveWebApplication
import org.springframework.fu.kofu.webflux.webFlux
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.ok
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.Duration

val app = reactiveWebApplication {
    beans {
        bean { jacksonObjectMapper().registerReferencing() }
    }
    webFlux {
        codecs { string() ; jackson() }
        router {
            GET("/health", ::health)
            GET("/hello") { hello() }
            GET("/hello/{name}") {
                hello(it.pathVariable("name"))
            }
            addReferencingEndpoints()
        }
    }
}
fun main(args: Array<String>) {
    app.run()
}

fun hello(name : String = "World") = ok()
    .contentType(MediaType.TEXT_PLAIN)
    .body(
        Flux.concat(
            Mono.delay(Duration.ofSeconds(1)).map { "Hello, ..." },
            Mono.delay(Duration.ofSeconds(2)).map { name }
        ), String::class.java
    )

fun health(request: ServerRequest) : Mono<ServerResponse> = TODO("Check coordinate factory has been loaded properly")