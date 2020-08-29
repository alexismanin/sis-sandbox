package fr.amanin.sis.extensions

import org.apache.sis.geometry.GeneralEnvelope
import org.apache.sis.geometry.ImmutableEnvelope
import org.opengis.geometry.Envelope
import org.opengis.referencing.crs.CoordinateReferenceSystem

fun envelope(vararg ordinates : Double, crs : CoordinateReferenceSystem? = null) : Envelope {
    require(ordinates.size % 2 == 0) {
        "Ordinates must contain an even number of values: lower corner then upper corner (Ex: minX, minY, maxX, maxY)"
    }

    val middle = ordinates.size / 2
    return ImmutableEnvelope(
        ordinates.sliceArray(0 until middle),
        ordinates.sliceArray(middle until ordinates.size),
        crs
    )
}

infix fun Envelope.intersect(other : Envelope) : Envelope {
    return GeneralEnvelope(this).apply { intersect(other) }
}

