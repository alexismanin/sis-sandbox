package fr.amanin.sis.extensions

import org.apache.sis.geometry.GeneralEnvelope
import org.apache.sis.geometry.ImmutableEnvelope
import org.apache.sis.referencing.CommonCRS
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class EnvelopesTest {

    @Test fun `build Envelope`() {
        val expected = ImmutableEnvelope(doubleArrayOf(2.0, 3.0), doubleArrayOf(3.0, 4.0), CommonCRS.defaultGeographic())
        assertEquals(expected, envelope(2.0, 3.0, 3.0, 4.0, crs = CommonCRS.defaultGeographic()))
    }

    @Test fun `intersection extension`() {
        val env1 = envelope(2.0, 3.0, 3.0, 4.0)
        val env2 = envelope(2.5, 3.7, 2.9, 4.2)

        assertEquals(
            GeneralEnvelope(doubleArrayOf(2.5, 3.7), doubleArrayOf(2.9, 4.0)),
            env1 intersect env2)
    }
}