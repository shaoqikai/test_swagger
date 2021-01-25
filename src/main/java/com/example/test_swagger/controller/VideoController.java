package com.example.test_swagger.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.apache.poi.util.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.util.StringUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author shaoqk
 * @create 2021-01-20 17:26
 */
@Api(value = "VideoController", tags = {"视频流转换模板"})
@RequestMapping("/iodemo")
@Controller
public class VideoController {

    /**
     * 通过访问服务器上的视频，以流的形式返回页面
     *
     * @param request
     * @param response
     * @param
     */
    @GetMapping("/getVio")
    @ResponseBody
    public void getVio(@ApiParam(hidden = true) HttpServletRequest request, @ApiParam(hidden = true) HttpServletResponse response) {
        // 内网的IP和路径
//        String url = "http://10.12.107.99:8090/file/1,08cd295534eb";
        String url = "http://10.10.77.230:8090/file/6,028d14dced39";
        if (StringUtils.isEmpty(url)) {
            throw new ArithmeticException("请输入解析的url");
        }
        try {

            URL readUrl = new URL(url);
            // 创建连接对象
            URLConnection urlConnection = readUrl.openConnection();
            // 设置超时时间
            urlConnection.setConnectTimeout(1000);
            urlConnection.setReadTimeout(5000);
            //发起连接
            urlConnection.connect();

            // 获取流
            InputStream inputStream = urlConnection.getInputStream();
            // 判断格式
            String fprmat = url.substring(url.lastIndexOf(".") + 1);
//            response.addHeader("Content-Type", "video/mp4;charset=UTF-8");
            response.addHeader("Content-Type", "audio/mpeg;charset=UTF-8");
            if ("mp4".equals(fprmat)) {
                // 设置返回的文件类型
                response.addHeader("Content-Type", "video/mp4;charset=UTF-8");
            } else if ("mp3".equals(fprmat)) {
                response.addHeader("Content-Type", "audio/mpeg;charset=UTF-8");
            }

            IOUtils.copy(inputStream, response.getOutputStream());
            response.flushBuffer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @GetMapping ("/test")
    @ResponseBody
    public void test(HttpServletRequest request, HttpServletResponse response) throws Exception {

        File file = new File("http://10.10.77.230:8090/file/4,0294af605722");
        ServletOutputStream out = null;
        try {
            FileInputStream instream = new FileInputStream(file);
            byte[] b = new byte[1024];
            int length = 0;
            BufferedInputStream buf = new BufferedInputStream(instream);
            out = response.getOutputStream();
            BufferedOutputStream bot = new BufferedOutputStream(out);
            while ((length = buf.read(b)) != -1) {
                bot.write(b, 0, b.length);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    @GetMapping ("/show")
    public String show() {
        return "show";
    }


    @PostMapping("/download")
    public String downloadAmachment(String downloadUrl, String realFileName, HttpServletRequest request,
                                    HttpServletResponse response) {
        response.setContentType("video/mp4;charset=UTF-8");
        try {
            request.setCharacterEncoding("UTF-8");

            BufferedInputStream bis = null;
            BufferedOutputStream bos = null;
            //此处使用的配置文件里面取出的文件服务器地址,拼凑成完整的文件服务器上的文件路径
            //写demo时，可以直接写成http://xxx/xx/xx.txt.这种形式
            String downLoadPath = "http://10.12.107.99:8090/file/1,08cd295534eb";
            response.setContentType("application/ogg");
            response.reset();//清除response中的缓存
            //根据网络文件地址创建URL
            URL url = new URL(downLoadPath);
            //获取此路径的连接
            URLConnection conn = url.openConnection();

            Long fileLength = conn.getContentLengthLong();//获取文件大小
            //设置reponse响应头，真实文件名重命名，就是在这里设置，设置编码
            response.setHeader("Content-disposition",
                    "attachment; filename=" + new String(realFileName.getBytes("utf-8"), "ISO8859-1"));
            response.setHeader("Content-Length", String.valueOf(fileLength));

            bis = new BufferedInputStream(conn.getInputStream());//构造读取流
            bos = new BufferedOutputStream(response.getOutputStream());//构造输出流
            byte[] buff = new byte[1024];
            int bytesRead;
            //每次读取缓存大小的流，写到输出流
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }
            response.flushBuffer();//将所有的读取的流返回给客户端
            bis.close();
            bos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
