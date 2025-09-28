package com.leandromendes.vehicleequalizer

import com.leandromendes.vehicleequalizer.data.model.EqualizerProfile
import com.leandromendes.vehicleequalizer.data.repository.UserRepository
import com.leandromendes.vehicleequalizer.util.Constants
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class UserRepositoryTest {
    private lateinit var userRepository: UserRepository

    private val defaultProfile = EqualizerProfile()
    private val newProfile1 = EqualizerProfile(
        name = "Profile_1",
        bassEqValue = Constants.define.BASS_VALUE_DEFAULT,
        midEqValue = Constants.define.MIDDLE_VALUE_DEFAULT,
        hiEqValue = Constants.define.TREBLE_VALUE_DEFAULT,
        balanceEqValue = Constants.define.PAN_VALUE_DEFAULT,
        masterVolValue = Constants.define.VOLUME_VALUE_DEFAULT,
        isSelected = false
    )

    @Before
    fun setUp() {
        userRepository = UserRepository()
    }

    // --- Initialization Tests ---
    @Test
    fun init_repositoryStartsWithOneDefaultProfile() {
        // GIVEN: The repository was initialized in @Before.

        // WHEN & THEN: Check that the list is not empty and has the expected size (1)
        assertEquals(1, userRepository.allEqualizerProfiles.size)

        // Check if the first profile is the default
        assertEquals(defaultProfile.name, userRepository.getEqualizerProfile(0).name)
    }

    // --- Tests for addProfile ---
    @Test
    fun addProfile_addsNewProfileToListCorrectly() {
        // GIVEN: Repository with 1 default profile (from init)

        // WHEN: Add a new profile
        userRepository.addProfile(newProfile1)

        // THEN: The size of the list should be 2 and the new profile should be in the last position
        assertEquals(2, userRepository.allEqualizerProfiles.size)
        assertEquals(newProfile1.name, userRepository.getEqualizerProfile(1).name)
    }

    // --- Tests for removeProfile ---
    @Test
    fun removeProfile_removesProfileByValidIndex() {
        // GIVEN: Add a second profile
        userRepository.addProfile(newProfile1)
        assertEquals(2, userRepository.allEqualizerProfiles.size) // Verifica o GIVEN

        // WHEN: Remove the added profile (index 1)
        userRepository.removeProfile(1)

        // THEN: The size should return to 1
        assertEquals(1, userRepository.allEqualizerProfiles.size)

        // Check if the remaining profile is the default one
        assertEquals(defaultProfile.name, userRepository.getEqualizerProfile(0).name)
    }

    @Test
    fun removeProfile_doesNothingIfIndexIsInvalid() {
        // GIVEN: Repository with 1 default profile
        val initialSize = userRepository.allEqualizerProfiles.size

        // WHEN: Attempts to remove an invalid index (outside the upper limit)
        userRepository.removeProfile(99)
        // E: Attempts to remove an invalid (negative) index
        userRepository.removeProfile(-1)

        // THEN: The size of the list should not change
        assertEquals(initialSize, userRepository.allEqualizerProfiles.size)
    }

    // --- Testes para updateProfile ---
    @Test
    fun updateProfile_replacesProfileAtValidIndex() {
        // GIVEN: Repository with the default profile at index 0
        val newName = "Updated Profile"
        val updatedProfile = EqualizerProfile(name = newName)

        // WHEN: Update profile in index 0
        userRepository.updateProfile(0, updatedProfile)

        // THEN: The name of the profile in index 0 should be the new name
        assertEquals(newName, userRepository.getEqualizerProfile(0).name)
        // The size of the list should remain 1
        assertEquals(1, userRepository.allEqualizerProfiles.size)
    }

    @Test
    fun updateProfile_doesNothingIfIndexIsInvalid() {
        // GIVEN: Repository with 1 default profile
        val originalProfile = userRepository.getEqualizerProfile(0)
        val newName = "Invalid Test"
        val updatedProfile = EqualizerProfile(name = newName)

        // WHEN: Attempts to update an invalid index
        userRepository.updateProfile(99, updatedProfile)

        // THEN: The profile in index 0 should remain the original
        assertEquals(originalProfile.name, userRepository.getEqualizerProfile(0).name)
    }

    // --- Testes para getEqualizerProfile ---
    @Test
    fun getEqualizerProfile_returnsCorrectProfile() {
        // GIVEN: Adds the profile “Profile_1” to index 1
        userRepository.addProfile(newProfile1)

        // WHEN: Get the profile in index 1
        val retrievedProfile = userRepository.getEqualizerProfile(1)

        // THEN: The returned profile must have the name “Profile.”
        assertEquals(newProfile1.name, retrievedProfile.name)
    }
}