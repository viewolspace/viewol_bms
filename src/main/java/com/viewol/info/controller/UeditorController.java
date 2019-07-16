package com.viewol.info.controller;

import com.viewol.info.util.Uploader;
import com.youguu.core.util.PropertiesUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Controller
@RequestMapping("/ued")
public class UeditorController {


    @ResponseBody
    @RequestMapping("imageUp")
    public Object imageUp(MultipartFile upfile, HttpServletRequest request) {

        Uploader up = new Uploader(request);
        up.setSavePath("upload");
        String[] fileType = {".gif", ".png", ".jpg", ".jpeg", ".bmp"};
        up.setAllowFiles(fileType);
        up.setMaxSize(10000); //单位KB
        try {
            up.upload(upfile);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String callback = request.getParameter("callback");

        String result = "{\"name\":\"" + up.getFileName() + "\", \"originalName\": \"" + up.getOriginalName() + "\", \"size\": " + up.getSize() + ", \"state\": \"" + up.getState() + "\", \"type\": \"" + up.getType() + "\", \"url\": \"" + up.getUrl() + "\"}";


        result = result.replaceAll("\\\\", "\\\\");

        if (callback == null) {
//            String uploadHttpUrl = "http://localhost:8088/upload" + File.separator + filename;
            return resultMap(up);
//            return result;
        } else {
            return "<script>" + callback + "(" + result + ")</script>";
        }
    }

    private Map<String, Object> resultMap(Uploader uploader) {
        Map<String, Object> resMap = new HashMap<String, Object>();
        resMap.put("state", uploader.getState());  //"SUCCESS" 表示成功
        resMap.put("name", uploader.getFileName());
        resMap.put("title", uploader.getTitle());
        resMap.put("original", uploader.getOriginalName());
        resMap.put("type", uploader.getType());
        resMap.put("size", uploader.getSize());

        try {
            Properties properties = PropertiesUtil.getProperties("properties/config.properties");
            String imageUrl = properties.getProperty("imageUrl");
            resMap.put("url", imageUrl + File.separator + uploader.getUrl());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resMap;
    }
}