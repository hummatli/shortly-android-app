package com.mobline.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mobline.data.local.AppDatabase
import com.mobline.data.local.link.dao.LinkDao
import com.mobline.data.local.link.model.Link
import com.mobline.data.mapper.toDomain
import com.mobline.data.repository.LinkLocalRepositoryImpl
import com.mobline.domain.repository.LinkLocalRepository
import kotlinx.coroutines.runBlocking
import org.hamcrest.core.IsEqual.equalTo
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class AppDatabaseTest {
    private lateinit var dao: LinkDao
    private lateinit var db: AppDatabase
    private lateinit var repo: LinkLocalRepository

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, AppDatabase::class.java
        ).build()
        dao = db.linkDao()
        repo = LinkLocalRepositoryImpl(db)
    }

    @After
    @Throws(IOException::class)
    fun tearDown() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun writeLinkAndReadInList() = runBlocking {
        //given
        val link = Link(
            code = "HuIn8JJ",
            longLink = "https://www.youtube.com/",
            shortLink = "https://shrtco.de/HuIn8JJ"
        )

        //when
        dao.insert(link)
        val byCode = dao.findLinkByCode(code = "HuIn8JJ")

        //then
        assertThat(byCode.size, equalTo(1))
        assertThat(byCode[0].code, equalTo(link.code))
        assertThat(byCode[0].shortLink, equalTo(link.shortLink))
        assertThat(byCode[0].longLink, equalTo(link.longLink))
    }

    @Test
    @Throws(Exception::class)
    fun writeLinkAndReadInListWithRepo() = runBlocking {
        //given
        val link = Link(
            code = "HuIn8JJ",
            longLink = "https://www.youtube.com/",
            shortLink = "https://shrtco.de/HuIn8JJ"
        )

        //when
        repo.insertLink(link.toDomain())
        val byCode = dao.findLinkByCode(code = "HuIn8JJ")

        //then
        assertThat(byCode.size, equalTo(1))
        assertThat(byCode[0].code, equalTo(link.code))
        assertThat(byCode[0].shortLink, equalTo(link.shortLink))
        assertThat(byCode[0].longLink, equalTo(link.longLink))
    }

    @Test
    @Throws(Exception::class)
    fun writeLinkDeleteAndReadInList() = runBlocking {
        //given
        val link = Link(
            code = "HuIn8JJ",
            longLink = "https://www.youtube.com/",
            shortLink = "https://shrtco.de/HuIn8JJ"
        )

        //when
        dao.insert(link)
        dao.deleteByCode(link.code)

        val byCode = dao.findLinkByCode(code = "HuIn8JJ")

        //then
        assertThat(byCode.size, equalTo(0))
    }

    @Test
    @Throws(Exception::class)
    fun writeLinkDeleteAndReadInListWithRepo() = runBlocking {
        //given
        val link = Link(
            code = "HuIn8JJ",
            longLink = "https://www.youtube.com/",
            shortLink = "https://shrtco.de/HuIn8JJ"
        )

        //when
        val domainLink = link.toDomain()
        repo.insertLink(domainLink)
        repo.deleteLink(domainLink)

        val byCode = dao.findLinkByCode(code = "HuIn8JJ")

        //then
        assertThat(byCode.size, equalTo(0))
    }
}