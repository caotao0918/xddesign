package cn.zmdxd.xddesign.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class FileUtil {

    private FileUtil() {

    }

    public static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            return file.delete();
        }
        return false;

    }

    /**
     * @description: 文件保存方法
     * @param file:上传的文件
     * @param destination:文件保存路径
     * @throws IllegalStateException
     */
    public static String saveFile(MultipartFile file, String destination, String uploadPath) throws IllegalStateException {
        // 获取上传的文件名称，并结合存放路径，构建新的文件名称
        String filename = file.getOriginalFilename();
        File filepath = new File(destination, filename);

        // 判断路径是否存在，不存在则新创建一个
        if (!filepath.getParentFile().exists()) {
            boolean mkdirs = filepath.getParentFile().mkdirs();
        }

        // 将上传文件保存到目标文件目录
        String substring = IdUtils.getUUID().substring(0, 8);
        try {
            file.transferTo(new File(destination + File.separator + substring + filename));
        } catch (IOException e) {
            throw new RuntimeException("上传文件异常，请稍后重试");
        }
        return destination.substring(uploadPath.length()) + File.separator + substring + filename;
    }

    // 获取文件头信息
    public static FileType getFileType(InputStream is) throws IOException {
        byte[] src = new byte[28];
        is.read(src, 0, 28);
        StringBuilder stringBuilder = new StringBuilder("");
        for (byte b : src) {
            int v = b & 0xFF;
            String hv = Integer.toHexString(v).toUpperCase();
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        FileType[] fileTypes = FileType.values();
        for (FileType fileType : fileTypes) {
            if (stringBuilder.toString().startsWith(fileType.getValue())) {
                return fileType;
            }
        }
        return null;
    }

}