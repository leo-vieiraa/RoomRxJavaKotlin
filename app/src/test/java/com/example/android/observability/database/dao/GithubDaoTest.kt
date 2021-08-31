package com.example.android.observability.database.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.android.observability.database.AppDatabase
import com.example.android.observability.model.GithubModel
import com.example.android.observability.model.GithubOwner
import org.junit.After
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class GithubDaoTest {

    private lateinit var database: AppDatabase
    private lateinit var dao: GithubDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = database.getGithubDao()

    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun testing_insert_github_model_should_returns_true() {
        val githubModelToInsert = GithubModel(
            id = 1L,
            name = "Kotlin",
            description = null,
            fullName = "",
            forks = 1L,
            language = "",
            stars = 0L,
            url = "",
            owner = GithubOwner(id = 1L, avatar = "", username = "")
        )

        dao.insert(arrayListOf(githubModelToInsert))

        val results = dao.getAll()
        assertThat(results).contains(githubModelToInsert)
    }

    @Test
    fun testing_total_by_lang_should_returns_true() {
        val githubModelToInsert1 = GithubModel(
            id = 1L,
            name = "",
            description = null,
            fullName = "",
            forks = 1L,
            language = "Kotlin",
            stars = 0L,
            url = "",
            owner = GithubOwner(id = 1L, avatar = "", username = "")
        )
        val githubModelToInsert2 = GithubModel(
            id = 2L,
            name = "",
            description = null,
            fullName = "",
            forks = 1L,
            language = "Kotlin",
            stars = 0L,
            url = "",
            owner = GithubOwner(id = 1L, avatar = "", username = "")
        )
        val githubModelToInsert3 = GithubModel(
            id = 3L,
            name = "",
            description = null,
            fullName = "",
            forks = 1L,
            language = "Java",
            stars = 0L,
            url = "",
            owner = GithubOwner(id = 1L, avatar = "", username = "")
        )

        dao.insert(arrayListOf(githubModelToInsert1, githubModelToInsert2, githubModelToInsert3))

        val result = dao.getTotalByLanguage("Kotlin")
        assertThat(result).isEqualTo(2)
    }


}