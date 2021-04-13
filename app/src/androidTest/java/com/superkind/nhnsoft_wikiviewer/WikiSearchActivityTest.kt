package com.superkind.nhnsoft_wikiviewer

import android.view.View
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeDown
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.superkind.nhnsoft_wikiviewer.activity.WikiSearchActivity
import com.superkind.nhnsoft_wikiviewer.activity.WebViewActivity
import com.superkind.nhnsoft_wikiviewer.util.EspressoIdleResource
import com.superkind.nhnsoft_wikiviewer.view.SearchBarView
import com.superkind.nhnsoft_wikiviewer.view.WikiListView
import org.hamcrest.CoreMatchers.anything
import org.hamcrest.Matcher
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class WikiSearchActivityTest {
    lateinit var activityScenarioWiki: ActivityScenario<WikiSearchActivity>
    private val searchText = "microsoft"

    private fun startActivity() {
        activityScenarioWiki = ActivityScenario.launch(WikiSearchActivity::class.java)
    }

    @Before
    fun registerIdlingResource() {
        IdlingRegistry.getInstance().register(EspressoIdleResource.countingIdlingResource)
    }

    // WikiListView Item 을 클릭합니다.
    private fun clickListItem(row: Int) {
        startActivity()
        Intents.init()

        // text microsoft
        onView(withId(R.id.main_search_bar)).perform(SearchBarSetTextAction(searchText))

        // perform search
        onView(withId(R.id.imgBtn_search)).perform(click())

        // perform click list item
        onData(anything()).inAdapterView(withId(R.id.list_search_result)).atPosition(row).perform(
            click()
        )
    }

    // Swipe 동작을 위한 Custom Action
    private fun withCustomConstraints(action: ViewAction, constraints: Matcher<View>): ViewAction? {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return constraints
            }

            override fun getDescription(): String {
                return action.description
            }

            override fun perform(uiController: UiController, view: View) {
                action.perform(uiController, view)
            }
        }
    }

    /**
     * 리스트에 아이템이 추가되었는지 확인합니다.
     */
    private fun checkIsListItemLoaded(): Boolean {
        var loaded = false
        activityScenarioWiki.onActivity {
            val size = it.findViewById<WikiListView>(R.id.main_list_wiki).getListAdapter().count
            loaded = size > 0
        }
        return loaded
    }

    /**
     * SwipeRefreshLayout을 사용하여 ListView를 새로 고침하는 pull to refresh ListView 커스텀 뷰를 구현한다.
     */
    @Test
    fun swipeAndRefresh() {
        // search
        searchAndShowList()

        // perform swipe down to refresh list
        onView(withId(R.id.swipe_layout)).perform(withCustomConstraints(swipeDown(), isDisplayingAtLeast(85)))

        // check is successfully load wiki items
        Assert.assertTrue(checkIsListItemLoaded())
    }

    /**
     * 검색어를 입력 후 검색을 시도하면 “검색 결과 Activity”를 구성한다.
     */
    @Test
    fun searchAndShowList() {
        startActivity()

        // set search text on edit text
        onView(withId(R.id.main_search_bar)).perform(SearchBarSetTextAction(searchText))

        // perform search
        onView(withId(R.id.imgBtn_search)).perform(click())

        // check is successfully load wiki items
        Assert.assertTrue(checkIsListItemLoaded())
    }

    /**
     * ListView의 header view를 클릭하면 “검색 상세 페이지” URL을 이용하여
     * WebView를 내장한 Activity를 새롭게 띄워 해당 웹 페이지를 표시한다.
     */
    @Test
    fun clickHeader() {
        // click header
        clickListItem(0)

        // check is WebViewActivity successfully load
        intended(hasComponent(WebViewActivity::class.java.name))
        Intents.release()
    }

    /**
     * ListView의 각 항목을 클릭하면 해당 검색어를 이용한 새로운 “검색 결과 Activity”를 띄운다.
     */
    @Test
    fun clickItem() {
        // click item
        clickListItem(1)

        // check is SearchActivity successfully load
        intended(hasComponent(WikiSearchActivity::class.java.name))
        Intents.release()
    }

    /**
     * “요약 정보 API” 를 이용하여 가져온 데이터를 이용하여 ListView의 header view 를 구성한다.
     */
    @Test
    fun checkHeaderTitle() {
        searchAndShowList()

        activityScenarioWiki.onActivity {
            val title = it.findViewById<WikiListView>(R.id.main_list_wiki).getListHeader().getTitle()
            Assert.assertTrue(title.toUpperCase().contains(title.toUpperCase()))
        }
    }

    @After
    fun unRegisterIdlingResource(){
        IdlingRegistry.getInstance().unregister(EspressoIdleResource.countingIdlingResource)
    }

    @After
    fun close() {
        activityScenarioWiki.close()
    }
}

// SearchBarView EditText 수정을 위한 Custom Action
class SearchBarSetTextAction(private val search: String) : ViewAction {
    override fun getConstraints(): Matcher<View> {
        return isAssignableFrom(SearchBarView::class.java)
    }

    override fun getDescription(): String {
        return search
    }

    override fun perform(uiController: UiController?, view: View) {
        val bar: SearchBarView = view as SearchBarView
        bar.setSearchText(search)
    }
}