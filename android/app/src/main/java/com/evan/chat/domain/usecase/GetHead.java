package com.evan.chat.domain.usecase;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import com.evan.chat.R;
import com.evan.chat.UseCase;
import com.evan.chat.data.source.model.setHeadable;
import com.evan.chat.util.AppExecutors;
import com.evan.chat.util.PropertiesUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.BitmapCallback;
import okhttp3.Call;

import static com.evan.chat.util.Objects.checkNotNull;

/**
 * Created by IntelliJ IDEA
 * User: Evan
 * Date: 2018/4/22
 * Time: 22:21
 */
public class GetHead extends UseCase<GetHead.RequestValues, GetHead.ResponseValue> {

    private AppExecutors appExecutors;

    public GetHead(){
        appExecutors = new AppExecutors();
    }

    @Override
    protected void executeUseCase(RequestValues requestValues) {
        final setHeadable p = requestValues.getP();
        appExecutors.networkIO().execute(new Runnable() {
            @Override
            public void run() {
                String url = PropertiesUtils.getInstance().getProperty("get_head",true) + p.getId() + "/head.png";
                OkHttpUtils.get().url(url)
                        .build().execute(new BitmapCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        getUseCaseCallback().onError();
                    }

                    @Override
                    public void onResponse(Bitmap response, int id) {
                        p.setHead(response);
                        getUseCaseCallback().onSuccess(new ResponseValue());
                    }
                });
            }
        });

    }

    public static final class RequestValues implements UseCase.RequestValues {
        private final setHeadable p;

        public RequestValues(@NonNull setHeadable p) {
            this.p = checkNotNull(p);
        }

        public setHeadable getP() {
            return p;
        }
    }

    public static final class ResponseValue implements UseCase.ResponseValue {

    }
}
