package com.leandromendes.vehicleequalizer.stubs

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.leandromendes.vehicleequalizer.data.dao.ProfileDao
import com.leandromendes.vehicleequalizer.data.model.EqualizerProfile
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
