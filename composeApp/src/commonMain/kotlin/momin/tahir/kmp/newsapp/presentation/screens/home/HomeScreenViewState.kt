package momin.tahir.kmp.newsapp.presentation.screens.home

import momin.tahir.kmp.newsapp.domain.model.News

// 封装了下结果，方便处理成功与失败。与加载。一共是三种状态
sealed interface HomeScreenViewState {
    data object Loading : HomeScreenViewState // 单例 是一个不可变的、唯一的单例对象，表示特定的 "加载" 状态。
    data class Success(
        val news: News,
    ) : HomeScreenViewState

    data class Failure(val error: String) : HomeScreenViewState
}