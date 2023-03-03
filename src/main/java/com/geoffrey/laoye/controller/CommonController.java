package com.geoffrey.laoye.controller;

import com.geoffrey.laoye.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * 文件上传和下载
 * 本项目是直接上传到服务器，服务器再返回图片给客户端下载，比较浪费流量
 * 一般都是前端暂时直接调用本地图片展示一下，确定保存了才上传的（待优化）
 */

@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {

    @Value("${laoye.path}")
    private String basePath;

    /**
     * 文件上传
     *
     * @param fileSame
     * @return
     */
    @PostMapping("/upload")
    //@RequestPart（"参数名"）解决同时上传文件和传递参数问题,指定的参数名为页面上传文件标签里的name属性
    public R<String> upload(@RequestPart("file") MultipartFile fileSame) {
        //file是一个临时文件，需要转存到指定位置，否则本次请求完成后临时文件会删除

        //获得原始文件名，这样就能动态的给转存位置的新文件起名了
        String originalFilename = fileSame.getOriginalFilename();
        //文件后缀
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        //使用UUID重新生成文件名，防止文件名称重复而造成的文件覆盖情况
        //文件名加上后缀就是完整格式的文件名了
        String fileName = UUID.randomUUID().toString() + suffix;

        //创建一个目录对象
        File dir = new File(basePath);
        //判断该目录是否存在
        if (!dir.exists()) {
            //该目录不存在则创建出来
            dir.mkdir();
        }

        try {
            //将临时文件转存到指定位置
            fileSame.transferTo(new File(basePath + fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        log.info(fileSame.toString());
        //需要将该文件名保存到数据库里面
        return R.success(fileName);
    }

    /**
     * 文件下载
     *
     * @param name
     * @param response
     */
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response) {

        try {
            //输入流，通过输入流读取文件内容
            FileInputStream fileInputStream = new FileInputStream(new File(basePath + name));

            //输出流，通过输出流将文件写回浏览器，在浏览器展示图片了
            ServletOutputStream outputStream = response.getOutputStream();

            response.setContentType("image/jpeg");

            int len = 0;
            byte[] bytes = new byte[1024];
            while ((len = fileInputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, len);
                outputStream.flush();
            }

            //关闭资源  
            outputStream.close();
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
