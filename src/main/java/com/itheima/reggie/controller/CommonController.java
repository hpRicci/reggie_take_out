package com.itheima.reggie.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.util.UUID;

/**
 * @author 陈万三
 * @create 2023-03-21 14:20
 */
@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {

    @Value("${reggie.path}")
    private String basePath;

    @PostMapping("/upload")
    public R<String> upload(MultipartFile file){
        //file是一个临时文件, 需要转存到指定位置, 否则本次请求完成后临时文件会删除
        log.info(file.toString());

        //获取原始文件名
        String originalFilename = file.getOriginalFilename();
        String substring = originalFilename.substring(originalFilename.lastIndexOf("."));//获取文件后缀(包括 . )

        //使用UUID重新生成文件名, 防止文件名称重复造成文件覆盖
        String fileName = UUID.randomUUID().toString() + substring;

        //新建一个目录
        File dir = new File(basePath);
        //判断目录是否存在
        if (!dir.exists()){
            dir.mkdirs(); //不存在则创建目录;
        }

        try {
            file.transferTo(new File(basePath + fileName));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        log.info("图片上传成功");
        return R.success(fileName);
    }

    @GetMapping("/download")
    public void download(String name, HttpServletResponse response){
        try {
            //输入流
            FileInputStream fileInputStream = new FileInputStream(new File(basePath + name));

            //输出流
            ServletOutputStream outputStream = response.getOutputStream();

            response.setContentType("image/jpeg");

            int len;
            byte[] bytes = new byte[1024];
            while ((len = fileInputStream.read(bytes)) != -1){
                outputStream.write(bytes, 0, len);
                outputStream.flush();
            }

            //关闭输入输出流
            outputStream.close();
            fileInputStream.close();

            log.info("图片下载成功");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
