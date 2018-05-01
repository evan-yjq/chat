package com.evan.chat.face;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.support.annotation.NonNull;
import com.evan.chat.data.source.model.User;
import com.evan.chat.util.AppExecutors;
import com.evan.chat.util.PropertiesUtils;
import com.evan.chat.util.UploadUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import okhttp3.Call;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

import static com.evan.chat.face.FaceFragment.TRAIN;
import static com.evan.chat.util.Objects.checkNotNull;
import static com.evan.chat.PublicData.user;

/**
 * Created by IntelliJ IDEA
 * User: Evan
 * Date: 2018/4/20
 * Time: 11:53
 */
public class FacePresenter implements FaceContratct.Presenter{

    private final FaceContratct.View view;
    private final long userId;
    private final boolean isResult;
    private final int type;
    private AppExecutors appExecutors;
    private int uploadSuccessNum;

    FacePresenter(@NonNull FaceContratct.View view, boolean isResult, int type){
        this.view = checkNotNull(view,"view cannot be null!");
        this.isResult = isResult;
        this.type = type;
        this.userId = user.getId();
        view.setPresenter(this);
    }

    @Override
    public void start() {
        appExecutors = new AppExecutors();
    }

    @Override
    public long getUserId() {
        return userId;
    }


    private volatile boolean stop = false;

    @Override
    public void buttonOnClick() {
        if (view.isActive()) {
            view.showProgress(true);
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    uploadSuccessNum = 0;
                    int uploadNum;
                    if (type == TRAIN) uploadNum = 8;
                    else uploadNum = 1;
                    final int bar = 100/(uploadNum+1);
                    for (int j = 0; j < uploadNum; j++) {
                        if (view.isActive()) {
                            if (stop)break;
                            if (type == TRAIN)
                                view.captrue(j+"");
                            else view.captrue("get");
                            appExecutors.mainThread().execute(new Runnable() {
                                @Override
                                public void run() {
                                    view.setAnimation(bar, 2500);
                                }
                            });
                        }
                        Thread.sleep(1500);
                    }
                    if (!stop) {
                        while (uploadSuccessNum != uploadNum) {
                            Thread.sleep(1000);
                        }
                        doIt();
                        if (view.isActive()) {
                            appExecutors.mainThread().execute(new Runnable() {
                                @Override
                                public void run() {
                                    view.setAnimation(bar, 2500);
                                }
                            });
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    if (view.isActive()) {
                        view.showMessage("因不明操作中断");
                        if (isResult) view.showResult(false);
                        view.showProgress(false);
                    }
                }
            }
        }).start();
    }

    private void doIt(){
        appExecutors.networkIO().execute(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils.post()
                        .url(PropertiesUtils.getInstance().getProperty("face_dist", true))
                        .addParams("userId",getUserId()+"")
                        .addParams("type",type+"").build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int i) {
                                if (view.isActive()) {
                                    view.showMessage("人脸不够清晰");
                                    if (isResult) view.showResult(false);
                                    view.showProgress(false);
                                }
                            }

                            @Override
                            public void onResponse(String s, int i) {
                                if (view.isActive()) {
                                    if (type == TRAIN) {
                                        if ("ok".equals(s)) {
                                            view.showMessage("绑定完成");
                                            user.setIs_bind_face(true);
                                        }
                                    } else {
                                        String[]strs = s.split("-");
                                        if (!"".equals(strs[0])) {
                                            String msg = "--鉴定成功--";
                                            if (strs.length==2)
                                                msg = msg + "\n" + strs[1];
                                            view.showMessage(msg);
                                            if (isResult) view.showResult(true);
                                        } else {
                                            view.showMessage("不是本人");
                                            if (isResult) view.showResult(false);
                                        }
                                    }
                                    view.showProgress(false);
                                }
                            }
                        });
            }
        });
    }

    @Override
    public void upload(final Bitmap bitmap, final File pictureFile) {
        appExecutors.networkIO().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Matrix matrix = new Matrix();
                    matrix.setScale(0.4f, 0.4f);
//                    将得到的照片进行270°旋转，使其竖直
                    matrix.preRotate(270);
                    Bitmap bit = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                    FileOutputStream fos = new FileOutputStream(pictureFile);
                    saveBMPpicture(fos, bit);
                    fos.close();
                    String str = UploadUtil.uploadFile(pictureFile, PropertiesUtils.getInstance().getProperty("face_upload",true));
                    if (str.equals("ok"))uploadSuccessNum++;
                } catch (Exception error) {
                    stop = true;
                    if (view.isActive()) {
                        view.showMessage("拍照失败");
                        if (isResult) view.showResult(false);
                        view.showProgress(false);
                    }
                    error.printStackTrace();
                }
            }
        });

    }

    private static void saveBMPpicture(FileOutputStream fos, Bitmap bm) {
        int w = bm.getWidth();
        int h = bm.getHeight();
        int[] pixels = new int[w * h];
        bm.getPixels(pixels, 0, w, 0, 0, w, h);

        byte[] rgb = addBMP_RGB_888(pixels, w, h);
        byte[] header = addBMPImageHeader(rgb.length);
        byte[] infos = addBMPImageInfosHeader(w, h);


        byte[] buffer = new byte[54 + rgb.length];
        System.arraycopy(header, 0, buffer, 0, header.length);
        System.arraycopy(infos, 0, buffer, 14, infos.length);
        System.arraycopy(rgb, 0, buffer, 54, rgb.length);
        try {
            fos.write(buffer);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static byte[] addBMP_RGB_888(int[] b, int w, int h) {
        int len = b.length;
//        System.out.println(b.length);
        byte[] buffer = new byte[w * h * 4];
        int offset = 0;
        for (int i = len - 1; i >= w; i -= w) {
            int start = i - w + 1;
            for(int j = start; j <= i; j++) {
                buffer[offset] = (byte)(b[j]);
                buffer[offset + 1] = (byte)(b[j] >> 8);
                buffer[offset + 2] = (byte)(b[j] >> 16);
                buffer[offset + 3] = (byte)(b[j] >> 24);
                offset += 4;
            }
        }
        return buffer;
    }

    //BMP文件头
    private static byte[] addBMPImageHeader(int size) {
        byte[] buffer = new byte[14];
        //magic number 'BM'
        buffer[0] = 0x42;
        buffer[1] = 0x4D;
        //记录大小
        buffer[2] = (byte) (size);
        buffer[3] = (byte) (size >> 8);
        buffer[4] = (byte) (size >> 16);
        buffer[5] = (byte) (size >> 24);
        buffer[6] = 0x00;
        buffer[7] = 0x00;
        buffer[8] = 0x00;
        buffer[9] = 0x00;
        buffer[10] = 0x36;
        buffer[11] = 0x00;
        buffer[12] = 0x00;
        buffer[13] = 0x00;
        return buffer;
    }

    //BMP文件信息头
    private static byte[] addBMPImageInfosHeader(int w, int h) {
        byte[] buffer = new byte[40];
        //这个是固定的 BMP 信息头要40个字节
        buffer[0] = 0x28;
        buffer[1] = 0x00;
        buffer[2] = 0x00;
        buffer[3] = 0x00;
        //宽度 地位放在序号前的位置 高位放在序号后的位置
        buffer[4] = (byte) (w);
        buffer[5] = (byte) (w >> 8);
        buffer[6] = (byte) (w >> 16);
        buffer[7] = (byte) (w >> 24);
        //长度 同上
        buffer[8] = (byte) (h);
        buffer[9] = (byte) (h >> 8);
        buffer[10] = (byte) (h >> 16);
        buffer[11] = (byte) (h >> 24);
        //总是被设置为1
        buffer[12] = 0x01;
        buffer[13] = 0x00;
        //比特数 像素 32位保存一个比特 这个不同的方式(ARGB 32位 RGB24位不同的!!!!)
        buffer[14] = 0x20;
        buffer[15] = 0x00;
        //0-不压缩 1-8bit位图
        //2-4bit位图 3-16/32位图
        //4 jpeg 5 png
        buffer[16] = 0x00;
        buffer[17] = 0x00;
        buffer[18] = 0x00;
        buffer[19] = 0x00;
        //说明图像大小
        buffer[20] = 0x00;
        buffer[21] = 0x00;
        buffer[22] = 0x00;
        buffer[23] = 0x00;
        //水平分辨率
        buffer[24] = 0x00;
        buffer[25] = 0x00;
        buffer[26] = 0x00;
        buffer[27] = 0x00;
        //垂直分辨率
        buffer[28] = 0x00;
        buffer[29] = 0x00;
        buffer[30] = 0x00;
        buffer[31] = 0x00;
        //0 使用所有的调色板项
        buffer[32] = 0x00;
        buffer[33] = 0x00;
        buffer[34] = 0x00;
        buffer[35] = 0x00;
        //不开颜色索引
        buffer[36] = 0x00;
        buffer[37] = 0x00;
        buffer[38] = 0x00;
        buffer[39] = 0x00;
        return buffer;
    }
}
