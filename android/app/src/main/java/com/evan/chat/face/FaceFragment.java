package com.evan.chat.face;

import android.Manifest;
import android.animation.ValueAnimator;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.*;
import android.widget.*;
import com.evan.chat.R;
import com.evan.chat.util.CameraUtil;

import java.io.File;
import java.io.IOException;
import java.util.Objects;


/**
 * Created by IntelliJ IDEA
 * User: Evan
 * Date: 2018/4/20
 * Time: 11:54
 */
public class FaceFragment extends Fragment implements FaceContratct.View , SurfaceHolder.Callback{

    public final static int TRAIN = 1;
    public final static int DIST = 2;

    private FaceContratct.Presenter presenter;

    private SurfaceView surfaceView;
    private Button button;
    private ProgressBar bar;

    private SurfaceHolder mHolder;

    private Camera mCamera;
    private int mCameraId = 1;
    private int screenWidth;

    private int type;

    public FaceFragment(int type){
        this.type = type;
    }

    public static FaceFragment newInstance(int type){
        return new FaceFragment(type);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.face_frag,container,false);
        surfaceView= root.findViewById(R.id.my_view);
        button = root.findViewById(R.id.button);
        if (type == TRAIN) button.setText("开始绑定");
        else button.setText("开始鉴定");
        bar = root.findViewById(R.id.bind_progress);

        mHolder = surfaceView.getHolder();
        mHolder.addCallback(this);
        initData();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.buttonOnClick(type);
            }
        });
        return root;
    }

    private int barNum;
    @Override
    public void setAnimation(final int mProgressBar, int millis) {
        ValueAnimator animator = ValueAnimator.ofInt(barNum, barNum+mProgressBar).setDuration(millis);
        barNum += mProgressBar;

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                bar.setProgress((int) valueAnimator.getAnimatedValue());
            }
        });
        animator.start();
    }

    @Override
    public void showProgress(boolean show) {
        button.setVisibility(show ? View.GONE : View.VISIBLE);
        bar.setVisibility(show ? View.VISIBLE : View.GONE);
        if (show)barNum = 0;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
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

    @Override
    public void showMessage(String msg){
        Toast toast = Toast.makeText(getContext(), msg, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    @Override
    public void captrue() {
        mCamera.takePicture(null, null, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                startPreview(camera,mHolder);
                final Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                // 创建并保存图片文件
                final File pictureFile = new File(getDir(), presenter.getUserId()+"-get.bmp");
                presenter.upload(bitmap, pictureFile);
            }
        });
    }

    // 获取文件夹
    private File getDir() {
        File filesDir = Objects.requireNonNull(getActivity()).getFilesDir();

        if (filesDir.exists()) {
            return filesDir;
        } else {
            filesDir.mkdirs();
            return filesDir;
        }
    }

    private void initData() {
        DisplayMetrics dm = Objects.requireNonNull(getActivity()).getResources().getDisplayMetrics();
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

    // 设置
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

        /*
         * 设置surfaceView的尺寸 因为camera默认是横屏，所以取得支持尺寸也都是横屏的尺寸
         * 我们在startPreview方法里面把它矫正了过来，但是这里我们设置设置surfaceView的尺寸的时候要注意 previewSize.height<previewSize.width
         * previewSize.width才是surfaceView的高度
         * 一般相机都是屏幕的宽度 这里设置为屏幕宽度 高度自适应 你也可以设置自己想要的大小
         */

        int picHeight = (screenWidth * pictrueSize.width) / pictrueSize.height;

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(screenWidth, picHeight);
        //这里当然可以设置拍照位置 比如居中 我这里就置顶了
        //params.gravity = Gravity.CENTER;
        surfaceView.setLayoutParams(params);
    }

    // 获取Camera实例
    private Camera getCamera(int id) {
        Camera camera = null;
        if(ActivityCompat.checkSelfPermission(Objects.requireNonNull(getContext()),
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()),
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }
        try {
            camera = Camera.open(id);
        } catch (Exception ignored) {
            getActivity().finish();
        }
        return camera;
    }

     // 释放相机资源
    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    // 预览相机
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
}
