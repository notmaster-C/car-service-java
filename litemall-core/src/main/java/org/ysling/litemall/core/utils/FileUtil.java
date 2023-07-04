package org.ysling.litemall.core.utils;


import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.io.FileUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * 文件下载工具
 */
public class FileUtil {

    /**
     * 通过网络地址获取文件InputStream
     * @param path 网络文件地址
     */
    public static InputStream returnBitMap(String path) {
        URL url;
        InputStream is;
        try {
            url = new URL(path);
        } catch (MalformedURLException e) {
            throw new RuntimeException("文件获取异常");
        }
        try {
            //利用HttpURLConnection对象,我们可以从网络中获取网页数据.
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.connect();
            is = conn.getInputStream();    //得到网络返回的输入流
        } catch (IOException e) {
            throw new RuntimeException("文件获取异常");
        }
        return is;
    }

    /**
     * 通过网络url取文件，并保存
     * @param url  网络地址
     */
    public static File uploadFile(String url) {
        String fileName = url.substring(url.lastIndexOf('/')+1);
        File file = new File(getBasePath("storage/tmp/") + fileName);
        if (file.exists()){
            return file;
        }
        try {
            URL pathUrl = new URL(url);
            DataInputStream dataInputStream = new DataInputStream(pathUrl.openStream());
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            while (dataInputStream.read(buffer) > 0) {
                fileOutputStream.write(buffer);//将buffer中的字节写入文件中区
            }
            dataInputStream.close();//关闭输入流
            fileOutputStream.close();//关闭输出流

            return file;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取项目同级路径
     * @param child 项目同级文件名
     */
    public static String getBasePath(String child) {
        return System.getProperty("user.dir") + child;
    }


    /**
     * file 类型转为 MultipartFile
     * @param file 文件
     */
    public static MultipartFile fileToMultipartFile(File file) {
        FileItem fileItem = createFileItem(file);
        return new CommonsMultipartFile(fileItem);
    }

    private static FileItem createFileItem(File file) {
        FileItemFactory factory = new DiskFileItemFactory(16, null);
        FileItem item = factory.createItem("textField", "text/plain", true, file.getName());
        int bytesRead;
        byte[] buffer = new byte[8192];
        try {
            FileInputStream fis = new FileInputStream(file);
            OutputStream os = item.getOutputStream();
            while ((bytesRead = fis.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return item;
    }

    /**
     * 将MultipartFile转化为File类的方法
     */
    public static File MultipartFileToFile(MultipartFile multipartFile){
        File toFile = null;
        try {
            InputStream ins = multipartFile.getInputStream();
            toFile = new File(FileUtils.getTempDirectory(), System.currentTimeMillis() + ".tmp");
            inputStreamToFile(ins, toFile);
            ins.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return toFile;
    }

    private static void inputStreamToFile(InputStream ins, File file) {
        try {
            OutputStream os = new FileOutputStream(file);
            int bytesRead;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
