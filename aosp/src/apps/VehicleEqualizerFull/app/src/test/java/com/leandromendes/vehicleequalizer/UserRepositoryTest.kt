package com.leandromendes.vehicleequalizer

import androidx.arch.core.executor.ArchTaskExecutor
import androidx.arch.core.executor.TaskExecutor
import com.leandromendes.vehicleequalizer.data.model.EqualizerProfile
import com.leandromendes.vehicleequalizer.data.repository.UserRepository
import com.leandromendes.vehicleequalizer.stubs.FakeProfileDao
import com.leandromendes.vehicleequalizer.stubs.getOrAwaitValue
import com.leandromendes.vehicleequalizer.util.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class UserRepositoryTest {

    private lateinit var userRepository: UserRepository
    private lateinit var fakeProfileDao: FakeProfileDao
    private val testDispatcher = StandardTestDispatcher()

    private val defaultProfileName = Constants.Define.PROFILE_DEFAULT_NAME
    private val newProfile1 = EqualizerProfile(
        name = "Profile_1",
        band0 = 0,
        band1 = 0,
        band2 = 0,
        band3 = 0,
        band4 = 0,
        masterVolValue = 0,
        isSelected = false
    )

    @BeforeEach
    fun setUp() {
        // Sets up the Main dispatcher for coroutines
        Dispatchers.setMain(testDispatcher)

        // Force LiveData to run inline, without relying on Looper
        ArchTaskExecutor.getInstance().setDelegate(object : TaskExecutor() {
            override fun executeOnDiskIO(runnable: Runnable) = runnable.run()
            override fun postToMainThread(runnable: Runnable) = runnable.run()
            override fun isMainThread(): Boolean = true
        })

        fakeProfileDao = FakeProfileDao()
        userRepository = UserRepository(fakeProfileDao)
    }

    @AfterEach
    fun tearDown() {
        // Resets the Main dispatcher
        Dispatchers.resetMain()
        ArchTaskExecutor.getInstance().setDelegate(null)
    }

    // --- Initialization Tests ---
    @Test
    fun init_repositoryStartsWithOneDefaultProfile() = runTest {
        // WHEN: Gets the list of profiles from LiveData
        val profiles = userRepository.allEqualizerProfiles.getOrAwaitValue()

        // THEN: The list size should be 1 and the name should be the default
        assertEquals(1, profiles.size)
        assertEquals(defaultProfileName, profiles.first().name)
    }

    // --- Tests for addProfile ---
    @Test
    fun addProfile_addsNewProfileToListCorrectly() = runTest {
        // WHEN: Adds a new profile
        userRepository.addProfile(newProfile1)

        // THEN: The list size (LiveData) should be 2 and the new profile should be at the end
        val profiles = userRepository.allEqualizerProfiles.getOrAwaitValue()
        assertEquals(2, profiles.size)
        assertEquals(newProfile1.name, profiles.last().name)
    }

    // --- Tests for removeProfile ---
    @Test
    fun removeProfile_removesProfileByObject() = runTest {
        // GIVEN: Adds a second profile and saves the object for removal
        userRepository.addProfile(newProfile1)
        var profiles = userRepository.allEqualizerProfiles.getOrAwaitValue()
        val profileToRemove = profiles.last()
        assertEquals(2, profiles.size)

        // WHEN: Removes the profile by object
        userRepository.removeProfile(profileToRemove)

        // THEN: The size should return to 1
        profiles = userRepository.allEqualizerProfiles.getOrAwaitValue()
        assertEquals(1, profiles.size)
        assertEquals(defaultProfileName, profiles.first().name)
    }

    // --- Updated Tests for updateProfile ---
    @Test
    fun updateProfile_replacesProfileAtValidIndex() = runTest {
        // GIVEN: Gets the default object and its ID.
        var profiles = userRepository.allEqualizerProfiles.getOrAwaitValue()
        val profileToUpdate = profiles.first()
        val originalId = profileToUpdate.id

        // Creates a new object with the same ID as the original
        val updatedProfile = profileToUpdate.copy(name = "Updated Profile")
        // WHEN: Updates the profile
        userRepository.updateProfile(updatedProfile)

        // THEN: The profile name should be the new name and the size should remain 1
        profiles = userRepository.allEqualizerProfiles.getOrAwaitValue()
        val updatedInList = profiles.first()
        assertEquals("Updated Profile", updatedInList.name)
        assertEquals(originalId, updatedInList.id)
        assertEquals(1, profiles.size)
    }
}
