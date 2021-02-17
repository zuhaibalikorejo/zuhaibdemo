package com.zuhaib.nytimes

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import android.provider.MediaStore
import com.zuhaib.nytimes.model.*
import com.zuhaib.nytimes.viewmodel.ListViewModel
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.disposables.Disposable
import io.reactivex.internal.schedulers.ExecutorScheduler
import io.reactivex.plugins.RxJavaPlugins

import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import java.util.concurrent.Callable
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit

class ListViewModelTest {

    @get:Rule
    var rule = InstantTaskExecutorRule()
    @Mock
    lateinit var mostViewedService: MostViewedService

    @InjectMocks
    var listViewModel = ListViewModel()
    lateinit var testSingle: Single<MostViewed>
    @Before
    fun setup(){
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun getCountriesSuccess() {


        var mediaMeetaDataList = arrayListOf(MediaMetadata("https://static01.nyt.com/images/2020/07/05/business/02Schoolparent-illo/02Schoolparent-illo-thumbStandard.jpg"))

        val mediaList: MutableList<media> = arrayListOf(media(mediaMeetaDataList))

        val result = Results("New York Times","Russia Secretly Offered Afghan Militants Bounties to Kill U.S. Troops, Intelligence Says","new theme","20/3/2",mediaList)
        val resultList = arrayListOf(result)
       val country = MostViewed("countryName","capital",resultList)
      // val countriesList = arrayListOf(country)

       testSingle = Single.just(country)

      `when`(mostViewedService.getMostView()).thenReturn(testSingle)

      listViewModel.refresh()


      Assert.assertEquals(false,listViewModel.countryLoadError.value)

      Assert.assertEquals(false,listViewModel.loading.value)

    }

    @Test
    fun getCountriesFail(){

        testSingle = Single.error(Throwable())

        `when`(mostViewedService.getMostView()).thenReturn(testSingle)

        listViewModel.refresh()

        Assert.assertEquals(true,listViewModel.countryLoadError.value)

        Assert.assertEquals(false,listViewModel.loading.value)

    }

    @Before
    fun setUpRxSchedulers(){
        val immidiate = object: Scheduler() {
            override fun scheduleDirect(run: Runnable?, delay: Long, unit: TimeUnit?): Disposable {
                return super.scheduleDirect(run, 0, unit)
            }

            override fun createWorker(): Worker {
                return ExecutorScheduler.ExecutorWorker(Executor { it.run() })
            }
        }

        RxJavaPlugins.setInitIoSchedulerHandler{scheduler: Callable<Scheduler>? -> immidiate }
        RxJavaPlugins.setInitComputationSchedulerHandler { scheduler: Callable<Scheduler>? -> immidiate }
        RxJavaPlugins.setInitNewThreadSchedulerHandler { scheduler: Callable<Scheduler>? ->  immidiate}
        RxJavaPlugins.setInitSingleSchedulerHandler { scheduler: Callable<Scheduler>? -> immidiate }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { scheduler: Callable<Scheduler>? -> immidiate }

    }

}