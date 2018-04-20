package com.evan.chat.face;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.*;
import android.widget.*;
import com.evan.chat.R;
import com.evan.chat.util.AppExecutors;
import com.evan.chat.util.CameraUtil;
import com.evan.chat.util.PropertiesUtils;
import com.evan.chat.util.UploadUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import okhttp3.Call;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.content.ContentValues.TAG;

/**
 * Created by IntelliJ IDEA
 * User: Evan
 * Date: 2018/4/20
 * Time: 11:54
 */
public class FaceRegisterFragment extends Fragment implements FaceContratct.View , SurfaceHolder.Callback{

    public final static int TRAIN = 1;
    public final static int DIST = 2;

    private FaceContratct.Presenter presenter;

    private SurfaceView surfaceView;
    private RelativeLayout all;
    private Button startBind;
    private ProgressBar bar;

    private SurfaceHolder mHolder;

    private Camera mCamera;
    private int mCameraId = 1;
    //屏幕宽高
    private int screenWidth;

    private int i = 1;

    public FaceRegisterFragment(){

    }

    public static FaceRegisterFragment getInstance(){
        return new FaceRegisterFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.face_frag,container,false);
        surfaceView= root.findViewById(R.id.my_view);
        all = root.findViewById(R.id.all);
        startBind = root.findViewById(R.id.bind_button);
        bar = root.findViewById(R.id.bind_progress);

        mHolder = surfaceView.getHolder();
        mHolder.addCallback(this);
        initData();

        startBind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgress();
                bind();
            }
        });
        return root;
    }

    @SuppressLint("HandlerLeak")
    private Handler bind = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    captrue();
                    setAnimation(bar, 10, 2500);
                    break;
                case 2:
                    train();
                    setAnimation(bar,20,5000);
            }
        }
    };

    private void bind(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (int j = 0; j < 8; j++) {
                        bind.sendEmptyMessageDelayed(1,0);
                        Thread.sleep(1500);
                    }
                    while(uploadSuccessNum != 8) {
                        Thread.sleep(500);
                    }
                    bind.sendEmptyMessageDelayed(2, 0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private int uploadSuccessNum = 0;
    private AppExecutors appExecutors = new AppExecutors();
    private void train(){
        appExecutors.networkIO().execute(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils.post()
                        .url(PropertiesUtils.getInstance().getProperty("face_dist", true))
                        .addParams("userId",presenter.getUserId()+"")
                        .addParams("type",TRAIN+"").build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int i) {
                                showMessage("人脸不够清晰");
                            }

                            @Override
                            public void onResponse(String s, int i) {
                                System.out.println(s);
                                if ("ok".equals(s)) {
                                    showMessage("绑定完成");
                                }
                            }
                        });
            }
        });
    }

    private int barNum = 0;
    private void setAnimation(final ProgressBar view, final int mProgressBar, int millis) {
        ValueAnimator animator = ValueAnimator.ofInt(barNum, barNum+mProgressBar).setDuration(millis);
        barNum += mProgressBar;

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                view.setProgress((int) valueAnimator.getAnimatedValue());
            }
        });
        animator.start();
    }

    private void showProgress() {
        startBind.setVisibility(View.GONE);
        bar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mCamera == null) {
            mCamera = getCamera(mCameraId);
            if (mHolder != null) {
                startPreview(mCamera, mHolder);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        releaseCamera();
    }

    private void showMessage(String msg){
        Snackbar.make(all,msg, Snackbar.LENGTH_LONG).show();
    }

    private void captrue() {
        mCamera.takePicture(null, null, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                startPreview(camera,mHolder);
                final Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                // 创建并保存图片文件
                final File pictureFile = new File(getDir(), presenter.getUserId()+"-"+i++ + ".bmp");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Matrix matrix = new Matrix();
                            matrix.setScale(0.4f, 0.4f);
//                         将得到的照片进行270°旋转，使其竖直
                            matrix.preRotate(270);
                            Bitmap bit = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                            FileOutputStream fos = new FileOutputStream(pictureFile);
                            saveBMPpicture(fos, bit);
                            fos.close();
                            String str = UploadUtil.uploadFile(pictureFile, "http://115.28.216.244:3000/user/face_upload");
                            if (str.equals("ok"))uploadSuccessNum++;
                        } catch (Exception error) {
                            showMessage("拍照失败");
                            Log.i(TAG, "保存照片失败" + error.toString());
                            error.printStackTrace();
                        }
                    }
                }).start();
            }
        });
    }

    // 获取文件夹
    private File getDir() {
        File filesDir = getActivity().getFilesDir();

        if (filesDir.exists()) {
            return filesDir;
        } else {
            filesDir.mkdirs();
            return filesDir;
        }
    }

    private void initData() {
        DisplayMetrics dm = getActivity().getResources().getDisplayMetrics();
        screenWidth = dm.widthPixels;
    }

    @Override
    public void setPresenter(FaceContratct.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    /**
     * 设置
     */
    private void setupCamera(Camera camera) {
        Camera.Parameters parameters = camera.getParameters();

        if (parameters.getSupportedFocusModes().contains(
                Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        }

        //这里第三个参数为最小尺寸 getPropPreviewSize方法会对从最小尺寸开始升序排列 取出所有支持尺寸的最小尺寸
        Camera.Size previewSize = CameraUtil.getInstance().getPropSizeForHeight(parameters.getSupportedPreviewSizes(), 800);
        parameters.setPreviewSize(previewSize.width, previewSize.height);

        Camera.Size pictrueSize = CameraUtil.getInstance().getPropSizeForHeight(parameters.getSupportedPictureSizes(), 800);
        parameters.setPictureSize(pictrueSize.width, pictrueSize.height);

        camera.setParameters(parameters);

        /**
         * 设置surfaceView的尺寸 因为camera默认是横屏，所以取得支持尺寸也都是横屏的尺寸
         * 我们在startPreview方法里面把它矫正了过来，但是这里我们设置设置surfaceView的尺寸的时候要注意 previewSize.height<previewSize.width
         * previewSize.width才是surfaceView的高度
         * 一般相机都是屏幕的宽度 这里设置为屏幕宽度 高度自适应 你也可以设置自己想要的大小
         *
         */

        int picHeight = (screenWidth * pictrueSize.width) / pictrueSize.height;

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(screenWidth, (screenWidth * pictrueSize.width) / pictrueSize.height);
        //这里当然可以设置拍照位置 比如居中 我这里就置顶了
        //params.gravity = Gravity.CENTER;
        surfaceView.setLayoutParams(params);
    }

    /**
     * 获取Camera实例
     *
     * @return
     */
    private Camera getCamera(int id) {
        Camera camera = null;
        try {
            camera = Camera.open(id);
        } catch (Exception e) {

        }
        return camera;
    }

    /**
     * 释放相机资源
     */
    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    /**
     * 预览相机
     */
    private void startPreview(Camera camera, SurfaceHolder holder) {
        try {
            setupCamera(camera);
            camera.setPreviewDisplay(holder);
            //亲测的一个方法 基本覆盖所有手机 将预览矫正
            CameraUtil.getInstance().setCameraDisplayOrientation(getActivity(), mCameraId, camera);
//            camera.setDisplayOrientation(90);
            camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        startPreview(mCamera, holder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        mCamera.stopPreview();
        startPreview(mCamera, holder);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        releaseCamera();
    }

    public static void saveBMPpicture(FileOutputStream fos, Bitmap bm) {
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

    public static byte[] addBMP_RGB_888(int[] b, int w, int h) {
        int len = b.length;
        System.out.println(b.length);
        byte[] buffer = new byte[w * h * 4];
        int offset = 0;
        for (int i = len - 1; i >= w; i -= w) {
            int end = i, start = i - w + 1;
            for(int j = start; j <= end; j++) {
                buffer[offset] = (byte)(b[j] >> 0);
                buffer[offset + 1] = (byte)(b[j] >> 8);
                buffer[offset + 2] = (byte)(b[j] >> 16);
                buffer[offset + 3] = (byte)(b[j] >> 24);
                offset += 4;
            }
        }
        return buffer;
    }

    //BMP文件头
    public static byte[] addBMPImageHeader(int size) {
        byte[] buffer = new byte[14];
        //magic number 'BM'
        buffer[0] = 0x42;
        buffer[1] = 0x4D;
        //记录大小
        buffer[2] = (byte) (size >> 0);
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
    public static byte[] addBMPImageInfosHeader(int w, int h) {
        byte[] buffer = new byte[40];
        //这个是固定的 BMP 信息头要40个字节
        buffer[0] = 0x28;
        buffer[1] = 0x00;
        buffer[2] = 0x00;
        buffer[3] = 0x00;
        //宽度 地位放在序号前的位置 高位放在序号后的位置
        buffer[4] = (byte) (w >> 0);
        buffer[5] = (byte) (w >> 8);
        buffer[6] = (byte) (w >> 16);
        buffer[7] = (byte) (w >> 24);
        //长度 同上
        buffer[8] = (byte) (h >> 0);
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
