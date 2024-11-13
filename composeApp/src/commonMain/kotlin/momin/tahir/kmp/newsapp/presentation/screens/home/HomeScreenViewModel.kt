package momin.tahir.kmp.newsapp.presentation.screens.home

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import momin.tahir.kmp.newsapp.domain.model.Article
import momin.tahir.kmp.newsapp.domain.usecase.DeleteNewsUseCase
import momin.tahir.kmp.newsapp.domain.usecase.GetAllNewsUseCase
import momin.tahir.kmp.newsapp.domain.usecase.GetSavedNewsUseCase
import momin.tahir.kmp.newsapp.domain.usecase.SaveNewsUseCase
import kotlin.coroutines.CoroutineContext

class HomeScreenViewModel(private val allNewsUseCase: GetAllNewsUseCase,
                          private val saveArticleUseCase: SaveNewsUseCase,
                          private val deleteArticleUseCase: DeleteNewsUseCase,
                          private val savedArticles: GetSavedNewsUseCase) : ScreenModel {

    private val job = SupervisorJob()
    private val coroutineContext: CoroutineContext = job + Dispatchers.IO
    private val viewModelScope = CoroutineScope(coroutineContext) // 启动协程用的

    // 状态处理
    val newsViewState = mutableStateOf<HomeScreenViewState>(HomeScreenViewState.Loading)
    // 存储的业务数据
    var savedNews = mutableStateListOf<Article>()

    // 初始化的时候就先从网络加载数据。 TODO 什么时候初始化呢？在哪里就开始初始化？
    init {
        // 利用协程在页面函数里异步加载网络数据，避免阻塞
        viewModelScope.launch {
            try {
                // 调用 API 从网络加载数据
                val news = allNewsUseCase.invoke()
                newsViewState.value = HomeScreenViewState.Success(news = news)
            } catch (e: Exception) {
                e.printStackTrace()
                newsViewState.value = HomeScreenViewState.Failure(e.message.toString())
            }
        }
    }

    fun getSavedArticles() {
        viewModelScope.launch {
            // 调用本地缓存获取缓存数据
            savedArticles.invoke().collect {
                savedNews.clear()
                savedNews.addAll(it)
            }
        }
    }

    fun saveArticle(article: Article) {
        viewModelScope.launch {
            saveArticleUseCase.invoke(article)
        }
    }

    fun removeArticle(article: Article) {
        viewModelScope.launch {
            deleteArticleUseCase.invoke(article)
        }
    }
}