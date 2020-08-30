package fr.amanin.sis.applications.webflux.sandbox

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import fr.amanin.sis.extensions.crs
import org.apache.sis.metadata.iso.extent.DefaultGeographicBoundingBox
import org.apache.sis.referencing.CRS
import org.opengis.metadata.extent.GeographicBoundingBox
import org.opengis.referencing.NoSuchAuthorityCodeException
import org.opengis.referencing.crs.CoordinateReferenceSystem
import org.springframework.http.MediaType
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.reactive.function.server.RouterFunctionDsl
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.badRequest
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.bodyToMono
import org.springframework.web.reactive.function.server.queryParamOrNull
import reactor.core.publisher.Mono
import java.lang.IllegalArgumentException

fun RouterFunctionDsl.addReferencingEndpoints() {
    GET("/crs") { describeCrs(it.queryParamOrNull("urn")) }
    POST("/crs") { it.bodyToMono<String>().flatMap(::describeCrs) }
    GET("/conversion") {
        conversion(
        it.queryParam("source").orElseThrow { IllegalArgumentException("Missing source CRS") },
        it.queryParam("target").orElseThrow { IllegalArgumentException("Missing target CRS") }
    ) }
}

fun describeCrs(crs : String?) : Mono<ServerResponse> {
    if (crs == null) {
        return badRequest().contentType(MediaType.TEXT_PLAIN).bodyValue("Missing parameter: URN")
    }
    return Mono.fromCallable { crs.crs() }
        .flatMap {
            ok()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(it)
        }
        .onErrorResume(NoSuchAuthorityCodeException::class.java) {
            badRequest()
                .contentType(MediaType.TEXT_PLAIN)
                .bodyValue("Invalid URN: $crs")
        }
}

fun conversion(from : String, to: String, roi: DoubleArray? = null) : Mono<ServerResponse> {
    val op = CRS.findOperation(from.crs(), to.crs(), roi.toGeoBBox())
    return ok()
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(op)
}

fun DoubleArray?.toGeoBBox() : GeographicBoundingBox? {
    if (this == null || isEmpty()) return null
    require(size == 4) { "Wrong number of ordinates. Expected 4, but $size were given"}
    return DefaultGeographicBoundingBox(this[0], this[2], this[1], this[3])
}

fun ObjectMapper.registerReferencing() {
    registerModule(
        SimpleModule("SIS referencing")
            .addSerializer(CRSWriter())
    )
}

class CRSWriter : StdSerializer<CoordinateReferenceSystem>(CoordinateReferenceSystem::class.java) {
    override fun serialize(value: CoordinateReferenceSystem?, gen: JsonGenerator?, provider: SerializerProvider?) {
        if (value != null) {
            gen?.writeString(value.toWKT())
        }
    }
}
