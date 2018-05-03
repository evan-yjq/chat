package com.evan.chat.domain.usecase;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import com.evan.chat.R;
import com.evan.chat.UseCase;
import com.evan.chat.data.source.Friend.FriendDataSource;
import com.evan.chat.data.source.Friend.FriendRepository;
import com.evan.chat.data.source.model.Friend;
import com.evan.chat.data.source.model.setHeadable;
import com.evan.chat.util.AppExecutors;
import com.evan.chat.util.PropertiesUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.BitmapCallback;
import okhttp3.Call;

import java.io.File;

import static com.evan.chat.util.Objects.checkNotNull;

/**
 * Created by IntelliJ IDEA
 * User: Evan
 * Date: 2018/4/22
 * Time: 22:21
 */
public class GetHead extends UseCase<GetHead.RequestValues, GetHead.ResponseValue> {

    private FriendRepository friendRepository;

    public GetHead(@NonNull FriendRepository friendRepository){
        this.friendRepository = checkNotNull(friendRepository,"friendRepository cannot be null!");
    }

    @Override
    protected void executeUseCase(RequestValues requestValues) {
        final setHeadable p = requestValues.getP();
        final File file = requestValues.getFile();
        friendRepository.getHead(file, p.getId(), new FriendDataSource.HeadCallback() {
            @Override
            public void onSuccess(Bitmap bitmap) {
                p.setHead(bitmap);
                getUseCaseCallback().onSuccess(new ResponseValue());
            }

            @Override
            public void onFail() {
                Bitmap bitmap=BitmapFactory.decodeStream(getClass().getResourceAsStream("/res/mipmap/logo.png"));
                p.setHead(bitmap);
                getUseCaseCallback().onSuccess(new ResponseValue());
            }
        });
    }

    public static final class RequestValues implements UseCase.RequestValues {
        private final setHeadable p;
        private final File file;

        public RequestValues(@NonNull setHeadable p, @NonNull File file) {
            this.p = checkNotNull(p);
            this.file = checkNotNull(file);
        }

        public File getFile() {
            return file;
        }

        public setHeadable getP() {
            return p;
        }
    }

    public static final class ResponseValue implements UseCase.ResponseValue {

    }
}
