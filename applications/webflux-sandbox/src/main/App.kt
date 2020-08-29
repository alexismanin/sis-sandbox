package fr.amanin.sis.applications.webflux.sandbox

import org.opengis.referencing.operation.CoordinateOperation
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.support.beans
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.router
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.Duration
import java.util.stream.Collectors
import kotlin.time.seconds
import kotlin.time.toJavaDuration

@SpringBootApplication
class App

fun main(args: Array<String>) {
    runApplication<App>(*args) {
        addInitializers(
            beans {
                bean {
                    router {
                        GET("/health") {
                            TODO("Check coordinate factory")
                        }

                        GET("/hello") { hello() }
                        GET("/hello/{name}") {
                            hello(it.pathVariable("name"))
                        }
                    }
                }
            }
        )
    }
}

fun hello(name : String = "World") = ok()
    .contentType(MediaType.TEXT_PLAIN)
    .body(
        Flux.concat(
            Mono.delay(Duration.ofSeconds(1)).map { "Hello, ..." },
            Mono.delay(Duration.ofSeconds(2)).map { name }
        ), String::class.java
    )

fun conversion(from : String, to: String, vararg bbox : Double) : Mono<ServerResponse> {
    TODO()
}