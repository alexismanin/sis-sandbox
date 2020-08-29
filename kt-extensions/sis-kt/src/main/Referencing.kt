package fr.amanin.sis.extensions

import org.apache.sis.referencing.CRS
import org.opengis.metadata.extent.GeographicBoundingBox
import org.opengis.referencing.NoSuchAuthorityCodeException
import org.opengis.referencing.crs.CoordinateReferenceSystem
import org.opengis.util.FactoryException

fun String.crs() : CoordinateReferenceSystem {
    return try {
        CRS.forCode(this)
    } catch (e : NoSuchAuthorityCodeException) {
        try {
            CRS.fromWKT(this)
        } catch (bis : FactoryException) {
            e.addSuppressed(bis)
            throw e
        }
    }
}

infix fun Pair<CoordinateReferenceSystem, CoordinateReferenceSystem>.conversion(within: GeographicBoundingBox) = CRS.findOperation(first, second, within)
