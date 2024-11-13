package momin.tahir.kmp.newsapp.data.repository

import momin.tahir.kmp.newsapp.data.api.INewsApi
import momin.tahir.kmp.newsapp.domain.model.Article

class NewsRepositoryImp(private val newsApi: INewsApi,
    private val cachedData: ICacheData
):INewsRepository {

    // fetchAllNews 必须是一个函数，因为表示的是 newsApi.fetchAllNews()的结果，而不是将其作为一个方法传递。
    override suspend fun fetchAllNews()= newsApi.fetchAllNews()
    override suspend fun searchNews(query: String) = newsApi.searchNews(query)

    override suspend fun saveArticle(article: Article) {
        cachedData.saveArticle(article)
    }

    override suspend fun deleteArticle(article: Article) {
        cachedData.deleteArticle(article)
    }

    override suspend fun getSavedArticles()= cachedData.getSavedNewsList()

}