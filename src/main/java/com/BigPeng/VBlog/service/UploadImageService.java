package com.BigPeng.VBlog.service;

import com.BigPeng.VBlog.dao.UserDao;
import com.BigPeng.VBlog.model.User;
import com.BigPeng.VBlog.util.VBlogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class UploadImageService {

    @Autowired
    UserDao userDao;

    public Map<String,String> uploadImage(MultipartFile file,User user) throws IOException {
        Map<String ,String> map = new HashMap<>();
        if (file != null) {
            if (file.getName() != null || "".equals(file.getName())) {
                String[] name = file.getContentType().split("/");
                if ("BMP".equals(name[name.length - 1]) || "JPG".equals(name[name.length - 1])
                        || "JPEG".equals(name[name.length - 1]) || "bmp".equals(name[name.length - 1])
                        || "jpg".equals(name[name.length - 1]) || "jpeg".equals(name[name.length - 1])) {

                    String filename = file.getOriginalFilename();
                    String suffix = filename.substring(filename.indexOf("."));

                    String imageUrl = VBlogUtil.imageUrl(user)+suffix;
                    String winimgUrl = "E:\\image"+imageUrl;
                    File filemak = new File(winimgUrl);

                    if (!filemak.exists()) {
                        filemak.getParentFile().mkdirs();
                        filemak.createNewFile();
                    }
                    file.transferTo(filemak);

                    userDao.updateHeadUrl(imageUrl,user.getId());
                    map.put("url",imageUrl);
                    map.put("msg","上传成功");
                    return map;
                } else {
                    map.put("msg","格式不正确!");
                    return map;
                }
            } else {
                map.put("msg","请选择文件!");
                return map;
            }
        } else {
            map.put("msg","请选择文件!");
            return map;
        }
    }
}