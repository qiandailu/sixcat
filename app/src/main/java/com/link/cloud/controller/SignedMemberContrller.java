package com.link.cloud.controller;

import android.content.Context;

import com.link.cloud.User;
import com.link.cloud.network.ApiFactory;
import com.link.cloud.network.response.ApiResponse;
import com.link.cloud.network.subscribe.ProgressSubscriber;

import java.net.ConnectException;
import java.util.concurrent.TimeoutException;

/**
 * 作者：qianlu on 2018/11/28 15:25
 * 邮箱：zar.l@qq.com
 */
public class SignedMemberContrller {

    private Context context;

    private SignedListener listener;


    public interface SignedListener {

        void signedMemberSuccess();

        void signedMemberFail(String message);

        void newWorkFail();
    }

    public SignedMemberContrller(SignedListener listener, Context context) {
        this.listener = listener;
        this.context = context;
    }


    public void signedMember(String uid, String fromType) {

        ApiFactory.signedMember(User.get().getDeviceId(), uid, fromType).subscribe(new ProgressSubscriber<ApiResponse>(context) {
            @Override
            public void onNext(ApiResponse memberdataResponseApiResponse) {
                if (listener != null) {
                    listener.signedMemberSuccess();
                }
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);
                if (throwable instanceof ConnectException || throwable instanceof TimeoutException) {
                    listener.newWorkFail();
                } else {
                    listener.signedMemberFail(throwable.getMessage());
                }

            }
        });
    }

}