
package com.photoselector.controller;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore.Images.ImageColumns;
import android.provider.MediaStore.Images.Media;
import android.text.TextUtils;

import com.photoselector.model.AlbumModel;
import com.photoselector.model.PhotoModel;

public class AlbumController {

    private ContentResolver resolver;

    public AlbumController(Context context) {
        resolver = context.getContentResolver();
    }

    /** ��ȡ�����Ƭ�б� */
    public List<PhotoModel> getCurrent() {
        Cursor cursor = resolver.query(Media.EXTERNAL_CONTENT_URI, new String[] {
                ImageColumns.DATA,
                ImageColumns.DATE_ADDED, ImageColumns.SIZE
        }, null, null, ImageColumns.DATE_ADDED);
        if (cursor == null || !cursor.moveToNext())
            return new ArrayList<PhotoModel>();
        List<PhotoModel> photos = new ArrayList<PhotoModel>();
        cursor.moveToLast();
        do {
            if (cursor.getLong(cursor.getColumnIndex(ImageColumns.SIZE)) > 1024 * 10) {
                PhotoModel photoModel = new PhotoModel();
                photoModel.setOriginalPath(cursor.getString(cursor
                        .getColumnIndex(ImageColumns.DATA)));
                photos.add(photoModel);
            }
        } while (cursor.moveToPrevious());
        return photos;
    }

    /** ��ȡ��������б� */
    public List<AlbumModel> getAlbums() {
        List<AlbumModel> albums = new ArrayList<AlbumModel>();
        Map<String, AlbumModel> map = new HashMap<String, AlbumModel>();
        Cursor cursor = resolver.query(Media.EXTERNAL_CONTENT_URI, new String[] {
                ImageColumns.DATA,
                ImageColumns.BUCKET_DISPLAY_NAME, ImageColumns.SIZE
        }, null, null, null);
        if (cursor == null || !cursor.moveToNext())
            return new ArrayList<AlbumModel>();
        cursor.moveToLast();
        AlbumModel current = new AlbumModel("������Ƭ", 0, cursor.getString(cursor
                .getColumnIndex(ImageColumns.DATA)), true); // "�����Ƭ"���
        albums.add(current);
        do {
            if (cursor.getInt(cursor.getColumnIndex(ImageColumns.SIZE)) < 1024 * 10)
                continue;

            current.increaseCount();
            String name = cursor.getString(cursor.getColumnIndex(ImageColumns.BUCKET_DISPLAY_NAME));
            if (map.keySet().contains(name))
                map.get(name).increaseCount();
            else {
                AlbumModel album = new AlbumModel(name, 1, cursor.getString(cursor
                        .getColumnIndex(ImageColumns.DATA)));
                map.put(name, album);
                albums.add(album);
            }
        } while (cursor.moveToPrevious());
        return albums;
    }

    /** ��ȡ��Ӧ����µ���Ƭ */
    public List<PhotoModel> getAlbum(String name) {
        Cursor cursor = resolver.query(Media.EXTERNAL_CONTENT_URI, new String[] {
                ImageColumns.BUCKET_DISPLAY_NAME,
                ImageColumns.DATA, ImageColumns.DATE_ADDED, ImageColumns.SIZE
        }, "bucket_display_name = ?",
                new String[] {
                    name
                }, ImageColumns.DATE_ADDED);
        if (cursor == null || !cursor.moveToNext())
            return new ArrayList<PhotoModel>();
        List<PhotoModel> photos = new ArrayList<PhotoModel>();
        cursor.moveToLast();
        do {
            if (cursor.getLong(cursor.getColumnIndex(ImageColumns.SIZE)) > 1024 * 10) {
                PhotoModel photoModel = new PhotoModel();
                photoModel.setOriginalPath(cursor.getString(cursor
                        .getColumnIndex(ImageColumns.DATA)));
                photos.add(photoModel);
            }
        } while (cursor.moveToPrevious());
        return photos;
    }

    /** ��ȡ��Ӧ�ļ�������Ƭ */
    public List<PhotoModel> getSpecifyPhoto(String dir) {
        List<PhotoModel> photos = null;
        try {
            if (!TextUtils.isEmpty(dir)) {
                File file = new File(dir);
                if (file != null && file.isDirectory()) {
                    File[] files = file.listFiles(new FileFilter() {

                        @Override
                        public boolean accept(File pathname) {
                            if(isImage(getFileType(pathname.getName()))){
                                return true;
                            }
                            return false;
                        }
                    });
                    if(files != null && files.length > 0){
                        photos = new ArrayList<PhotoModel>();
                        for (File file2 : files) {
                            PhotoModel photoModel = new PhotoModel();
                            photoModel.setOriginalPath(file2.getAbsolutePath());
                            photos.add(photoModel);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return photos;
    }

    /**
     * ��ȡ�ļ���׺��
     * 
     * @param fileName
     * @return �ļ���׺��
     */
    public static String getFileType(String fileName) {
        if (fileName != null) {
            int typeIndex = fileName.lastIndexOf(".");
            if (typeIndex != -1) {
                String fileType = fileName.substring(typeIndex + 1)
                        .toLowerCase();
                return fileType;
            }
        }
        return "";
    }

    /**
     * ���ݺ�׺���ж��Ƿ���ͼƬ�ļ�
     * 
     * @param type
     * @return �Ƿ���ͼƬ���true or false
     */
    public static boolean isImage(String type) {
        if (type != null
                && (type.equals("jpg") || type.equals("gif")
                        || type.equals("png") || type.equals("jpeg")
                        || type.equals("bmp") || type.equals("wbmp")
                        || type.equals("ico") || type.equals("jpe"))) {
            return true;
        }
        return false;
    }
}
