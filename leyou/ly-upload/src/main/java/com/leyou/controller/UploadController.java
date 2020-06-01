package com.leyou.controller;

import org.apache.commons.lang.StringUtils;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("upload")
public class UploadController {

    public static final List<String> FILS_TYPE = Arrays.asList("jpg", "png");

    //配置文件中的值,获取
    @Value("${user.httpImageYuMing}")
    private String httpImage;

    /**
     * 图片上传验证
     *
     * @param file
     * @return
     */
    @RequestMapping("image")
    public String uploadImage(@RequestParam("file") MultipartFile file) throws IOException {
        /**
         * 1: 验证图片的格式
         * 2: 验证图片的内容
         * 3: 验证图片的大小
         * */
        try {
            //获取上传图片的名字
            String filename = file.getOriginalFilename();
            //获取后缀 +1是为了不要后面的"."
            String type01 = filename.substring(filename.lastIndexOf(".") + 1);
            //也可以使用阿帕奇的工具类
            String fileTpey = StringUtils.substringAfterLast(filename, ".");

            System.out.println("type01:" + type01);
            System.out.println("fileType:" + fileTpey);

            //验证文件格式,通过截取字符串的方式获取
            if (!FILS_TYPE.contains(fileTpey)) {
                return null;
            }

            // 验证图片的内容
            BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
            if (bufferedImage == null) {
                return null;
            }

            String filePath = System.currentTimeMillis() + filename;
            //图片上传
            // file.transferTo(new File("d:/upload/"+filePath));

            //加载客户端配置文件,配置文件中指明了tracker服务器的地址
            ClientGlobal.init("fastdfs.conf");
            //验证配置文件是否加载成功
            System.out.println(ClientGlobal.configInfo());

            //创建TranckerClient对象,客户端对象
            TrackerClient trackerClient = new TrackerClient();

            //获取到调度对象,也就是与Tracker服务器取得联系
            TrackerServer trackerServer = trackerClient.getConnection();

            //创建存储客户端对象
            StorageClient storageClient = new StorageClient(trackerServer, null);

           /* NameValuePair[] params=new NameValuePair[1];
            NameValuePair p =new NameValuePair();
            p.setName("创建时间");p.setValue("333");
            params[0] = p;
            String[] upload_file =storageClient.upload_file("d:/test.jpg","jpg",params);*/

            String[] upload_appender_file = storageClient.upload_appender_file(file.getBytes(), fileTpey, null);

            for (String string : upload_appender_file) {
                System.out.println(string);
                //group/M0/00/XXXXX.JPG
            }

            return httpImage + upload_appender_file[0] + "/" + upload_appender_file[1];


        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
