package app.grandeyardera.com.grandeyardera.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by 41988 on 2017/8/23.
 */

public class NetUtil {
    private URL url = null;
    private String path = null;
    private InputStream is = null;
    private OutputStream out = null;
    private File file =null;
    public String upInfo(String path,String info,String request,String encode){   //request接收方命名的变量
        this.path = path;
        try {
            url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            conn.setUseCaches(false);
            if(info!=null) {
                out = conn.getOutputStream();
                byte[] bytes = new String(request + info).getBytes(encode);
                out.write(bytes);
                out.flush();
                out.close();
            }
            if(conn.getResponseCode()==200){
                is = conn.getInputStream();
                System.out.println("连接成功");
            }else {
                return null;
            }
            byte[] b = getBytesByInputStream(is);
            is.close();
            return new String(b,encode);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String upPic(String path, Bitmap bitmap, String encode){
        try {
            url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            conn.setUseCaches(true);
            //out = new ByteArrayOutputStream();
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);
            bitmap.recycle();
            byte[] bytes = output.toByteArray();
            out = conn.getOutputStream();
            out.write(bytes);
            out.flush();
            out.close();
            if(conn.getResponseCode()==200){
                is = conn.getInputStream();
                System.out.println("连接成功");
            }
            byte[] b = getBytesByInputStream(is);
            is.close();
            return new String(b,encode);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }return null;

    }
    public Bitmap getPic(String path,String info,String request,String encode){
        try {
            url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            conn.setUseCaches(true);
            if(conn.getResponseCode()==200){
                System.out.println("连接成功");
                is = conn.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                is.close();
                return bitmap;
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }return null;
    }

    private static byte[] getBytesByInputStream(InputStream is){
        byte[] bytes = null;
        BufferedInputStream bis = new BufferedInputStream(is);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BufferedOutputStream bos = new BufferedOutputStream(baos);
        byte[] buffer = new byte[1024 * 8];
        int length = 0;
        try {
            while ((length = bis.read(buffer)) > 0) {
                bos.write(buffer, 0, length);
            }
            bos.flush();
            bytes = baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                bis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return bytes;

    }

}
