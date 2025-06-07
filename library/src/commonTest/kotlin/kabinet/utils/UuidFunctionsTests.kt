@file:OptIn(ExperimentalUuidApi::class)

package kabinet.utils

import kotlin.test.*
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class UuidFunctionsTests {
    
    @Test
    fun testRandomUuidString() {
        // Test that randomUuidString() returns a non-empty string
        val uuidString = randomUuidString()
        assertNotNull(uuidString)
        assertTrue(uuidString.isNotEmpty())
        
        // Test that it contains a hyphen (format should be "base62-base62")
        assertTrue(uuidString.contains("-"))
        
        // Test that two random UUIDs are different
        val anotherUuidString = randomUuidString()
        assertNotEquals(uuidString, anotherUuidString)
    }
    
    @Test
    fun testUuidToLongPair() {
        // Create a known UUID
        val uuid = Uuid.fromString("00000000-0000-0000-0000-000000000000")
        val pair = uuid.toLongPair()
        
        // Test conversion to long pair
        assertEquals(0L, pair.first)
        assertEquals(0L, pair.second)
        
        // Test with a non-zero UUID
        val nonZeroUuid = Uuid.fromString("12345678-1234-5678-1234-567812345678")
        val nonZeroPair = nonZeroUuid.toLongPair()
        assertNotEquals(0L, nonZeroPair.first)
        assertNotEquals(0L, nonZeroPair.second)
    }
    
    @Test
    fun testLongPairToUuid() {
        // Test conversion from long pair to UUID
        val pair = 0L to 0L
        val uuid = pair.toUuid()
        
        // Test that the UUID is as expected
        assertEquals("00000000-0000-0000-0000-000000000000", uuid.toString())
        
        // Test with non-zero values
        val nonZeroPair = 1234567890L to 9876543210L
        val nonZeroUuid = nonZeroPair.toUuid()
        
        // Test that converting back gives the same pair
        assertEquals(nonZeroPair, nonZeroUuid.toLongPair())
    }
    
    @Test
    fun testUuidToStringId() {
        // Test conversion to string ID
        val uuid = Uuid.fromString("00000000-0000-0000-0000-000000000000")
        val stringId = uuid.toStringId()
        
        // Test that the string ID has the expected format (base62-base62)
        assertTrue(stringId.contains("-"))
        val parts = stringId.split("-")
        assertEquals(2, parts.size)
        
        // Test that converting back works
        val reconstructedUuid = Uuid.fromStringId(stringId)
        assertEquals(uuid, reconstructedUuid)
    }
    
    @Test
    fun testUuidFromStringId() {
        // Test conversion from string ID to UUID
        val originalUuid = Uuid.random()
        val stringId = originalUuid.toStringId()
        val reconstructedUuid = Uuid.fromStringId(stringId)
        
        // Test that the reconstructed UUID matches the original
        assertEquals(originalUuid, reconstructedUuid)
        
        // Test with a known string ID
        val knownPair = 0L to 0L
        val knownUuid = knownPair.toUuid()
        val knownStringId = "0-0"  // Base62 representation of 0-0
        assertEquals(knownUuid, Uuid.fromStringId(knownStringId))
    }
    
    @Test
    fun testRoundTripConversion() {
        // Test full round-trip conversion: UUID -> LongPair -> UUID
        val originalUuid = Uuid.random()
        val longPair = originalUuid.toLongPair()
        val reconstructedUuid = longPair.toUuid()
        assertEquals(originalUuid, reconstructedUuid)
        
        // Test full round-trip conversion: UUID -> StringId -> UUID
        val stringId = originalUuid.toStringId()
        val reconstructedFromStringId = Uuid.fromStringId(stringId)
        assertEquals(originalUuid, reconstructedFromStringId)
    }
}