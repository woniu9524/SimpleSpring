package com.woniu.spring.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @Author: zhangcheng
 * @CreateTime: 2022-09-04  23:26
 * @Description: 文件处理工具类
 */
public class FileUtil {
    public static List<File> getAllFile(File dirFile){
        // 如果文件夹不存在或着不是文件夹，则返回 null
        if(Objects.isNull(dirFile) || !dirFile.exists() || dirFile.isFile())
            return null;

        File[] childrenFiles =  dirFile.listFiles();
        if(Objects.isNull(childrenFiles) || childrenFiles.length == 0)
            return null;

        List<File> files = new ArrayList<>();
        for(File childFile : childrenFiles) {

            // 如果时文件，直接添加到结果集合
            if(childFile.isFile()) {
                files.add(childFile);
            }else {
                // 如果是文件夹，则将其内部文件添加进结果集合
                List<File> cFiles =  getAllFile(childFile);
                if(Objects.isNull(cFiles) || cFiles.isEmpty()) continue;
                files.addAll(cFiles);
            }

        }
        return files;
    }
}
