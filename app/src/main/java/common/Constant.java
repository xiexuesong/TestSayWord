package common;

import android.os.Environment;

import java.io.File;


/**
 * Created by Administrator on 2017-12-29.
 */

public class Constant {
    public static final String TAG = "savion";
    //====================http response code========================
    public static final String API_RESPONSE_OK = "1";
    public static final String API_RESPONSE_ERROR = "0";
    public static final String API_RESPONSE_LOCAL_EXCEPTION = "-1";
    //====================http response code========================


    public static final String WXAPPID = "wx9c54d8071d0e71f9";
    public static final String WXSecret = "d069a1ad10dfd38efb5fee74c272dafd";
    public static final String QQAPPID = "1107929792";
    public static final String QQAPPKEY = "leLKU8QeRkixgn5t";
    public static final String UMENGAPPID = "5c19e084f1f556408c000318";
    //接口路径
    public static final String API_BASE_URL = "http://say-words.recorder.nineton.cn";
    public static final String API_SALT = "123456ABCDEFGHIJKL{(&#!,.&*)}MNOPQRSTUVWXYZ7890";
    public static final String API_AES_KEY= "362DA87FA3E89A95";
//
//    //base path
//    public static final String BASE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator+"MvpReader";
//    //cache path
//    public static final String CACHE_PATH = BASE_PATH+File.separator+"cache";
//    //img path
//    public static final String IMG_PATH = BASE_PATH+File.separator+"img";
//
//    public static final String INTENT_KEY = "intent_key";


    public static boolean Debug = true;
    public static final String PUBLIC_SD_FILE;
    public static final String QQ_APP_ID = "1107517387";
    public static final String VIVO_PUBLIC_SD_FILE;
    public static final String WX_APP_ID = "wx207dee304195470e";
    public static final String apiVersionV1 = "v1/";
    public static final String apiVersionV2 = "v2/";
    public static final String appServer = "https://api.xuanzhuanzimu.com/";
    public static final String assetsPath = "file:///android_asset/";
    public static final String cachePath;
    public static final String httpCachePath;
    public static final String downloadPath;
    public static final String filePath; //= App.instance.getFilesDir().getAbsolutePath();
    public static final String mp3OutPath;
    public static final String mp4OutPath;
    public static final String fontPath;
    public static final String bgPath;
    public static final String recordOutPath;
    public static boolean openException = false;
   // public static String packageName = App.instance.getPackageName();
    public static final String picOutPath;
    public static final long selFileEditTime = 180000;
    public static final long selFileMaxSize = 500;
    public static final long selFileMaxTime = 1800000;
    public static final long selFileMinTime = 3000;
    public static final String typefacePath;

    static {
        filePath = Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator+"SayWord";
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(Environment.getExternalStorageDirectory().getPath());
        stringBuilder.append("/DCIM/Camera");
        PUBLIC_SD_FILE = stringBuilder.toString();
        stringBuilder = new StringBuilder();
        stringBuilder.append(Environment.getExternalStorageDirectory().getPath());
        stringBuilder.append("/相机");
        VIVO_PUBLIC_SD_FILE = stringBuilder.toString();
        stringBuilder = new StringBuilder();
        stringBuilder.append(filePath);
        stringBuilder.append("/Cache");
        cachePath = stringBuilder.toString();
        stringBuilder = new StringBuilder();
        stringBuilder.append(cachePath);
        stringBuilder.append("/httpCache");
        httpCachePath = stringBuilder.toString();
        stringBuilder = new StringBuilder();
        stringBuilder.append(filePath);
        stringBuilder.append("/Download/");
        downloadPath = stringBuilder.toString();
        stringBuilder = new StringBuilder();
        stringBuilder.append(cachePath);
        stringBuilder.append("/Video");
        mp4OutPath = stringBuilder.toString();
        stringBuilder = new StringBuilder();
        stringBuilder.append(cachePath);
        stringBuilder.append("/music");
        mp3OutPath = stringBuilder.toString();
        stringBuilder = new StringBuilder();
        stringBuilder.append(cachePath);
        stringBuilder.append("/record");
        recordOutPath = stringBuilder.toString();
        stringBuilder = new StringBuilder();
        stringBuilder.append(cachePath);
        stringBuilder.append("/Pic");
        picOutPath = stringBuilder.toString();

        stringBuilder = new StringBuilder();
        stringBuilder.append(cachePath);
        stringBuilder.append("/font");
        fontPath = stringBuilder.toString();

        stringBuilder = new StringBuilder();
        stringBuilder.append(cachePath);
        stringBuilder.append("/bg");
        bgPath = stringBuilder.toString();

        stringBuilder = new StringBuilder();
        stringBuilder.append(cachePath);
        stringBuilder.append("/Typeface/");
        typefacePath = stringBuilder.toString();
    }



}
