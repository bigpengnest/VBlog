package com.BigPeng.VBlog.controller;

import com.BigPeng.VBlog.model.Blog;
import com.BigPeng.VBlog.model.User;
import com.BigPeng.VBlog.service.BlogService;
import com.BigPeng.VBlog.service.UploadImageService;
import com.BigPeng.VBlog.service.UserService;
import com.BigPeng.VBlog.util.VBlogUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class thymeleafController {

    @Autowired
    BlogService blogService;

    @Autowired
    UploadImageService uploadImageService;

    @Autowired
    UserService userService;

    @RequestMapping("/test")
    public String test(Model model){
        User user =new User();
        User searchuser= new User();
        model.addAttribute("user",user);
        model.addAttribute("searchuser",searchuser);
        return "test";
    }

    @RequestMapping("/testPag")
    public String testpag(Model model,
            @RequestParam(required=true,defaultValue="1") Integer page){
        PageHelper.startPage(page, 3);
        List<Blog> blogList = blogService.getBlogList(6);
        PageInfo<Blog> p=new PageInfo<Blog>(blogList);
        model.addAttribute("page", p);
        model.addAttribute("blogList",blogList);
        System.out.println(blogList.size());
        return "testpag";
    }

    @RequestMapping(path = "/uploadImageTemp")
    @ResponseBody
    public String testimage (Model model,
                            @RequestParam(value = "file", required = true) MultipartFile file,
                            HttpServletRequest request) throws IOException {
        User user = userService.selectById(3);
        Map<String,String> map = uploadImageService.uploadImage(file,user);
        model.addAttribute("map",map);
        return "upimage";
    }

    @RequestMapping("/test/search")
    @ResponseBody
    public String search(Model model,
                         @RequestParam String username){
        Map<String,Object> map= new HashMap<>();
        User user = userService.selectByName(username);

        map.put("name",user.getName());
        map.put("id",user.getId());

/*        String username = request.getParameter("username");
        User searchuser = userService.selectByName(username);
        model.addAttribute("searchuser",searchuser);*/
        System.out.println(user.getName());
        return VBlogUtil.getJSONString(1,map);
    }
}
