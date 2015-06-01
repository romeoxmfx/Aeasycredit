
package com.aeasycredit.order.tool;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;

public class AeaCamera implements MediaScannerConnectionClient {
    private static AeaCamera instance;

    public static final int REQUEST_TAKE_PHOTO = 4001;
    public static final int REQUEST_PICK_PHOTO = REQUEST_TAKE_PHOTO + 1;

    public static final String baselocalTempImgDir = "aeasy";
    private String localTempImgFileName = ".jpg";

    private Context mContext;
    private boolean isInit = false;

    public String[] allFiles;
    private String SCAN_PATH;
    private static final String FILE_TYPE = "image/*";
    private MediaScannerConnection conn;
    private String openCameraTaskId;

    private File f;

    public static AeaCamera getInstance() {
        if (instance == null) {
            instance = new AeaCamera();
        }
        return instance;
    }

    private AeaCamera() {
    };

    public void initialize(Context context) {
        if (isInit) {
            return;
        }
        mContext = context;
        isInit = true;
    }

    public void openCamara(Context mContext, String taskId) {
        // 先验证手机是否有sdcard
        this.openCameraTaskId = taskId;
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            try {
                File baseDir = new File(Environment.getExternalStorageDirectory()
                        + "/" + baselocalTempImgDir);
                if (!baseDir.exists())
                    baseDir.mkdirs();

                File dir = new File(Environment.getExternalStorageDirectory()
                        + "/" + baselocalTempImgDir + "/" + taskId);
                if (!dir.exists())
                    dir.mkdirs();

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                Format format = new SimpleDateFormat("yyyyMMddhhmmss");
                String fileName = format.format(new Date()) + localTempImgFileName;
                f = new File(dir, fileName);
                Uri u = Uri.fromFile(f);
                intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, u);
                if (mContext instanceof Activity) {
                    ((Activity) mContext).startActivityForResult(intent,
                            REQUEST_TAKE_PHOTO);
                }
            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 将图片image压缩成大小为 size的图片（size表示图片大小，单位是KB）
     * 
     * @param image 图片资源
     * @param size 图片大小
     * @return Bitmap
     */
    private Bitmap compressImage(Bitmap image, int size) {
        Bitmap bitmap = null;
        ByteArrayInputStream isBm = null;
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            // 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
            image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            int options = 100;
            // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            while (baos.toByteArray().length / 1024 > size) {
                // 重置baos即清空baos
                baos.reset();
                // 每次都减少10
                options -= 12;
                // 这里压缩options%，把压缩后的数据存放到baos中
                image.compress(Bitmap.CompressFormat.JPEG, options, baos);

            }
            // 把压缩后的数据baos存放到ByteArrayInputStream中
            isBm = new ByteArrayInputStream(baos.toByteArray());
            // 把ByteArrayInputStream数据生成图片
            bitmap = BitmapFactory.decodeStream(isBm, null, null);
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            try {
                if(baos != null){
                    baos.close();
                }
                if(isBm != null){
                    isBm.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return bitmap;
    }

    public void choosePhoto(Context context, String taskId) {
        File folder = new File(Environment.getExternalStorageDirectory()
                + "/" + baselocalTempImgDir + "/" + taskId);
        allFiles = folder.list();
        if (allFiles != null && allFiles.length > 0) {
            SCAN_PATH = folder.getAbsolutePath() + "/" + allFiles[0];

            if (conn != null)
            {
                conn.disconnect();
            }
            conn = new MediaScannerConnection(context, this);
            conn.connect();
        }
    }

    @Override
    public void onMediaScannerConnected() {
        conn.scanFile(SCAN_PATH, FILE_TYPE);
    }

    @Override
    public void onScanCompleted(String path, Uri uri) {
        try {
            if (uri != null)
            {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(uri, FILE_TYPE);
                mContext.startActivity(intent);
            }
        } finally
        {
            conn.disconnect();
            conn = null;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (isInit == false) {
//            return;
//        }
        switch (requestCode) {
            case REQUEST_TAKE_PHOTO: {
                if (resultCode == Activity.RESULT_OK) {
                    // String filePath =
                    // Environment.getExternalStorageDirectory()
                    // + "/" + baselocalTempImgDir + "/" + openCameraTaskId;
                    // File f = new File(filePath);
                    // try {
                    // Uri u =
                    // Uri.parse(android.provider.MediaStore.Images.Media
                    // .insertImage(mContext.getContentResolver(),
                    // f.getAbsolutePath(), null, null));
                    // try {
                    // Bitmap bm = MediaStore.Images.Media.getBitmap(
                    // mContext.getContentResolver(), u);
                    // bm = rotateBitmap(bm, readPictureDegree(filePath));
                    // } catch (IOException e) {
                    // e.printStackTrace();
                    // }
                    // } catch (FileNotFoundException e) {
                    // e.printStackTrace();
                    // }
                    // 继续拍照
                    // openCamara(this.openCameraTaskId);
                    // Bitmap bm = (Bitmap) data.getExtras().get("data");
                    // jsCallBackPhotoBase64String(bm);
                    FileOutputStream fos = null;
                    Bitmap bitmap = null;
                    try {
                        if(f == null){
                            return;
                        }
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inSampleSize = 2;
                        bitmap = BitmapFactory.decodeFile(f.getPath(),
                                options);
                        // 压缩图片
                        bitmap = compressImage(bitmap,300);
     
                        if (bitmap != null) {
                            // 保存图片
                            fos = new FileOutputStream(f);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                            fos.flush();
                            bitmap.recycle();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally{
                        try {
                            if(fos != null){
                                fos.close();
                            }
                            if(bitmap != null){
                                bitmap.recycle();
                            }
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }
                    }
                    
                }
                break;
            }
            case REQUEST_PICK_PHOTO: {
                if (resultCode == Activity.RESULT_OK && null != data) {
                    Uri imageUri = data.getData();
                    String picturePath = null;
                    if (imageUri != null) {
                        if ("file".equalsIgnoreCase(imageUri.getScheme())) {
                            picturePath = imageUri.getPath();
                        } else {
                            String[] fileColumns = {
                                    MediaStore.Images.Media.DATA
                            };
                            Cursor c = mContext.getContentResolver().query(
                                    imageUri, fileColumns, null, null, null);
                            if (c != null && c.moveToFirst()) {
                                int columnIndex = c.getColumnIndex(fileColumns[0]);
                                picturePath = c.getString(columnIndex);
                                c.close();
                            }
                        }
                    }
                }
                break;
            }
        }
    }

    /**
     * 读取图片属性：旋转的角度
     * 
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return degree;
        }
        return degree;
    }

    /**
     * 旋转图片，使图片保持正确的方向。
     * 
     * @param bitmap 原始图片
     * @param degrees 原始图片的角度
     * @return Bitmap 旋转后的图片
     */
    public static Bitmap rotateBitmap(Bitmap bitmap, int degrees) {
        if (degrees == 0 || null == bitmap) {
            return bitmap;
        }
        Matrix matrix = new Matrix();
        matrix.setRotate(degrees, bitmap.getWidth() / 2, bitmap.getHeight() / 2);
        Bitmap bmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), matrix, true);
        if (null != bitmap) {
            bitmap.recycle();
        }
        return bmp;
    }
}
