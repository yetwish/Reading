package com.xidian.yetwish.reading.framework.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.graphics.Palette;

import com.xidian.yetwish.reading.R;
import com.xidian.yetwish.reading.framework.FWHelper;
import com.xidian.yetwish.reading.framework.service.ApiCallback;

import java.util.Map;
import java.util.Random;

/**
 * Created by Yetwish on 2016/5/9 0009.
 */
public class BitmapUtils {

    private BitmapUtils() {
        /* can not be instantiated */
        throw new UnsupportedOperationException("can not be instantiated");
    }

    private static final Random RANDOM = new Random();

    public static int getRandomAppBarBgImageRes() {
        switch (RANDOM.nextInt(4)) {
            default:
            case 0:
                return R.mipmap.bg1;
            case 1:
                return R.mipmap.bg2;
            case 2:
                return R.mipmap.bg3;
            case 3:
                return R.mipmap.bg4;
        }
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        //获取源图片的宽高
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSample = 1;
        if (height > reqHeight || width > reqWidth) {
            //计算出源宽高与目标宽高的比率
            final int heightRatio = Math.round(height * 1.0f / reqHeight);
            final int widthRatio = Math.round(width * 1.0f / reqWidth);
            inSample = heightRatio > widthRatio ? heightRatio : widthRatio;
        }
        return inSample;
    }


    public static Bitmap decodeSampleBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        // 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        //获取inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        //使用inSampleSize再次解析图片
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }


    public static Bitmap loadImage(Resources res, int reqWidth, int reqHeight){
        final int resId = getRandomAppBarBgImageRes();
        return decodeSampleBitmapFromResource(res,resId,reqWidth,reqHeight);
    }


    public static void loadMutedColorForBitmap(Bitmap bitmap, final ApiCallback<Integer> callback){
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                Palette.Swatch mutedSwatch = palette.getMutedSwatch();
                if(mutedSwatch!=null){
                    final int color = mutedSwatch.getRgb();
                    if(callback != null)
                        FWHelper.getInstance().getMainHandler().post(new Runnable() {
                            @Override
                            public void run() {
                                callback.onDataReceived(color);
                            }
                        });
                }
            }
        });
    }

}
