package com.mirkowu.mvm.ui.mvvm

import androidx.lifecycle.LiveData
import com.mirkowu.lib_base.mediator.BaseMediator
import com.mirkowu.lib_base.util.RxLife
import com.mirkowu.lib_base.util.RxScheduler
import com.mirkowu.lib_base.view.IBaseView
import com.mirkowu.lib_network.ErrorBean
import com.mirkowu.lib_network.ErrorType
import com.mirkowu.lib_network.state.ResponseData
import com.mirkowu.lib_network.state.ResponseLiveData
import com.mirkowu.lib_network.util.asResponseLiveData
import com.mirkowu.lib_network.util.subscribe
import com.mirkowu.lib_util.LogUtil
import com.mirkowu.lib_util.livedata.SingleLiveData
import com.mirkowu.lib_util.utilcode.util.NetworkUtils
import com.mirkowu.mvm.BizModel
import com.mirkowu.mvm.bean.GankBaseBean
import com.mirkowu.mvm.bean.GankImageBean
import com.mirkowu.mvm.bean.RandomImageBean
import com.mirkowu.mvm.network.ImageClient
import com.mirkowu.mvm.network.RxObserver
import io.reactivex.rxjava3.core.Observable

open class MVVMMediator : BaseMediator<IBaseView?, BizModel?>() {
    @JvmField
    val mImageData = ResponseLiveData<List<RandomImageBean>>()
    var mLiveData = SingleLiveData<Any>()
    var mError = SingleLiveData<Throwable>()

    @JvmField
    var mRequestImageListData = SingleLiveData<ResponseData<List<GankImageBean>>>()

    @JvmField
    var mImageError = SingleLiveData<ErrorBean>()

    var mPingResult = ResponseLiveData<Boolean>()


    fun loadImage(page: Int, pageSize: Int) {
        mModel.loadImage(page, pageSize)
            .doOnDispose { LogUtil.d("RxJava 被解绑") }
            .to(RxLife.bindLifecycle(mView))
            .subscribe(object : RxObserver<GankBaseBean<List<GankImageBean>>>() {
                override fun onSuccess(data: GankBaseBean<List<GankImageBean>>) {
                    if (data.isSuccess) {
                        mRequestImageListData.setValue(ResponseData.success(data.data))
                    }
                }

                override fun onFailure(bean: ErrorBean) {
                    mRequestImageListData.setValue(ResponseData.error(bean))
                }
            })
        loadImage2()
    }

    fun loadImage2() {
        //todo  这里不用model层也是可以的，直接把api 放到mediator中
        //mModel.loadImage2()
        ImageClient.getImageApi()
            .getRandomImage()
            .compose(RxScheduler.ioToMain())
            .doOnDispose { LogUtil.d("RxJava 被解绑") }
            .to(RxLife.bindLifecycle(mView))
            .subscribe(object : RxObserver<List<RandomImageBean>>() {
                override fun onSuccess(data: List<RandomImageBean>) {
                    mImageData.setValue(ResponseData.success(data))
                }


                override fun onFailure(bean: ErrorBean) {
                    mImageData.setValue(ResponseData.error(bean))
                }
            })
    }

    //                        mError.setValue(e);
    val data: Unit
        get() {
            mModel.loadData()
                .doOnDispose { LogUtil.d("RxJava 被解绑") }
                .to(RxLife.bindLifecycle(mView))
                .subscribe(object : RxObserver<Any>() {
                    override fun onSuccess(data: Any) {
                        mLiveData.setValue(data)
                    }

                    override fun onFailure(bean: ErrorBean) {


//                        mError.setValue(e);
                    }
                })
        }

    fun getPing() {
        Observable.create<Boolean> {
            val result = NetworkUtils.isAvailableByPing("baidu.com")
            it.onNext(result)
            it.onComplete()
        }
            .compose(RxScheduler.ioToMain())
            .to(RxLife.bindLifecycle(mView))
//            .subscribe(
//                onSuccess = {
//                    mPingResult.value = ResponseData.success(it)
//                },
//                onFailure = {
//                    mPingResult.value = ResponseData.error(it)
//                })
            .subscribe(object : RxObserver<Boolean>() {
                override fun onSuccess(data: Boolean) {
                    mPingResult.value = ResponseData.success(data)
                }

                override fun onFailure(bean: ErrorBean) {
                    mPingResult.value = ResponseData.error(bean)
                }
            })
    }

    fun getPing2LiveData(): ResponseLiveData<Boolean> {
        return Observable.create<Boolean> {
            val result = NetworkUtils.isAvailableByPing("baidu.com")
            it.onNext(result)
            it.onComplete()
        }
            .compose(RxScheduler.ioToMain())
            .to(RxLife.bindLifecycle(mView))
            .asResponseLiveData()
    }
}