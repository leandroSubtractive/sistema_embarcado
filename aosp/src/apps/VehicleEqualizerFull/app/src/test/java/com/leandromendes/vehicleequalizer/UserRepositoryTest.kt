package com.leandromendes.vehicleequalizer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.leandromendes.vehicleequalizer.data.dao.ProfileDao
import com.leandromendes.vehicleequalizer.data.model.EqualizerProfile
import com.leandromendes.vehicleequalizer.data.repository.UserRepository
import com.leandromendes.vehicleequalizer.util.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

// --- LiveData Utility for Tests ---

/**
 * LiveData extension that blocks the test execution until a value is emitted.
 */
fun <T> LiveData<T>.getOrAwaitValue(
    time: Long = 2,
    timeUnit: TimeUnit = TimeUnit.SECONDS,
    afterObserve: () -> Unit = {}
): T {
    var data: T? = null
    val latch = CountDownLatch(1)
    val observer = object : Observer<T> {
        override fun onChanged(value: T) {
            data = value
            latch.countDown()
            this@getOrAwaitValue.removeObserver(this)
        }
    }
    this.observeForever(observer)

    try {
        afterObserve.invoke()

        // Don't wait indefinitely if the LiveData is not set.
        if (!latch.await(time, timeUnit)) {
            throw TimeoutException("LiveData value was never set.")
        }

    } finally {
        this.removeObserver(observer)
    }

    @Suppress("UNCHECKED_CAST")
    return data as T
}

// --- Fake DAO (Simulates Room) ---

/**
 * Fake/Mock implementation of ProfileDao for use in unit tests.
 */
class FakeProfileDao : ProfileDao {

    // Simulates the database table
    private val data = mutableListOf<EqualizerProfile>()
    // The MutableLiveData that notifies observers. Initializes with the default profile.
    private val profilesLiveData = MutableLiveData<List<EqualizerProfile>>()
    private var nextId = 1

    init {
        // Initializes with the default profile
        val defaultProfile = EqualizerProfile(id = nextId++)
        data.add(defaultProfile)
        profilesLiveData.postValue(data.toList())
    }

    override fun getAllProfiles(): LiveData<List<EqualizerProfile>> {
        return profilesLiveData
    }

    override suspend fun insert(profile: EqualizerProfile): Long {
        // Copies the profile to ensure insertion uses a new ID
        val newProfile = profile.copy(id = nextId++)
        data.add(newProfile)
        profilesLiveData.postValue(data.toList())
        return newProfile.id.toLong()
    }

    override suspend fun update(profile: EqualizerProfile) {
        val index = data.indexOfFirst { it.id == profile.id }
        if (index != -1) {
            data[index] = profile
            profilesLiveData.postValue(data.toList())
        }
    }

    override suspend fun delete(profile: EqualizerProfile) {
        if (data.removeIf { it.id == profile.id }) {
            profilesLiveData.postValue(data.toList())
        }
    }

    // Implementation required if it exists in ProfileDao, even if not used in tests
    override suspend fun deleteById(profileId: Int) {
        if (data.removeIf { it.id == profileId }) {
            profilesLiveData.postValue(data.toList())
        }
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class UserRepositoryTest {

    // Rule for LiveData to function correctly in the test
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

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

    @Before
    fun setUp() {
        // Sets up the Main dispatcher for coroutines
        Dispatchers.setMain(testDispatcher)

        // Initializes the Fake DAO and the Repository with the DAO (now works)
        fakeProfileDao = FakeProfileDao()
        userRepository = UserRepository(fakeProfileDao)
    }

    @After
    fun tearDown() {
        // Resets the Main dispatcher
        Dispatchers.resetMain()
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

        val newName = "Updated Profile"
        // Creates a new object with the same ID as the original
        val updatedProfile = profileToUpdate.copy(name = newName)

        // WHEN: Updates the profile
        userRepository.updateProfile(updatedProfile)

        // THEN: The profile name should be the new name and the size should remain 1
        profiles = userRepository.allEqualizerProfiles.getOrAwaitValue()
        val updatedInList = profiles.first()

        assertEquals(newName, updatedInList.name)
        assertEquals(originalId, updatedInList.id)
        assertEquals(1, profiles.size)
    }
}