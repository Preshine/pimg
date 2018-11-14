//package com.preshine.img.controller;
//
//import com.preshine.img.fastdfs.FDFSClient;
//import org.apache.commons.io.FilenameUtils;
//import org.apache.commons.io.IOUtils;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.*;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.UUID;
//
//
//@Controller
//@RequestMapping("/pimg/")
//public class FileController {
//
//    @Value("${imageLocation}")
//    private String imageLocation;
//
//    //项目内部暂时不使用 SLF4J来做日志适配， 因为启动是Jetty嵌入式启动的系统，
//    // jetty有默认的对slf4j的日志实现 ，得覆盖他的实现，否则自己的项目不按照配置的log4j2.xml的执行，日志打印格式会按照jetty的来，
//    //但是spring mybatis等框架会按照log4j2.xml的执行，暂时还没解决，这里项目就直接用Log4j2的类来实例化，没用slf4j的做门面了。
////    private static final Logger logger = LoggerFactory.getLogger(FileController.class);
//    private static final Logger LOGGER = LogManager.getLogger(FileController.class);
//
//    @RequestMapping(value = "/uploadImg", method = RequestMethod.POST)
//    @ResponseBody
//    public Map<String,Object> uploadImg(
//                                        @RequestParam(value = "file") MultipartFile file, HttpServletRequest request) {
//        Map<String,Object> map = new HashMap<>();
//        try {
//
//        String fileId = FDFSClient.uploadFile(file.getBytes(), file.getOriginalFilename());
//        map.put("url", "/pimg/resource/" + fileId);
//        map.put("type", FilenameUtils.getExtension(file.getOriginalFilename()));
//        map.put("title", "/pimg/resource/" + fileId);
//        map.put("original", "/pimg/resource/" + fileId);
//        map.put("size", file.getSize());
//        map.put("state", "SUCCESS");
//        return map;
//        } catch (Exception e) {
//            LOGGER.error(e);
//        }
//        //如果上传失败就上传到本地
//        return uploadLocal(file, request);
//    }
//
//    @RequestMapping(value = "/")
//    @ResponseBody
//    public Map<String,Object> uploadImg(HttpServletRequest request) {
//        Map<String, Object> map = new HashMap<>();
//        map.put("name", "preshine");
//        map.put("age", 23);
//        LOGGER.error(map.toString());
//        return map;
//    }
//
//    @RequestMapping(value = "/toImg")
//    public void toImg(HttpServletResponse response) throws Exception {
//        LOGGER.info("toImg");
//        response.sendRedirect("/views/uploadImg.html");
//    }
//
//    /**
//     * 返回图片二进制流
//     * @param uid 图片资源id
//     * @param response
//     */
//    @RequestMapping(value = "/resource/{uid:.+}")
//    public void getResource( @PathVariable(value="uid") String uid, HttpServletResponse response) {
//        if (uid == null || ("").equals(uid)) {
//            return;
//        }
//        InputStream is = null;
//        OutputStream os = null;
//        try {
//            if (uid.indexOf(".") >= 0) {
//                String imageType = uid.split("\\.")[1];
//                response.setContentType("image/" + imageType);
//            }
//            is = new FileInputStream(new File(imageLocation + File.separator + uid));
//            os = response.getOutputStream();
//            IOUtils.copy(is, os);
//            os.flush();
//        } catch (Exception e) {
//            LOGGER.error(e.toString());
//        } finally {
//            IOUtils.closeQuietly(is);
//            IOUtils.closeQuietly(os);
//        }
//    }
//
////    public static byte[] readInputStream(InputStream is) throws Exception{
////        ByteArrayOutputStream os = new ByteArrayOutputStream();
////        byte[] buffer = new byte[2048];
////        int len = 0;
////        while( (len = is.read(buffer)) != -1 ){
////            os.write(buffer, 0, len);
////        }
////        is.close();
////        return os.toByteArray();
////    }
//
//    public Map<String,Object> uploadLocal(MultipartFile multipartFile, HttpServletRequest request) {
//        Map<String,Object> map = new HashMap<>();
//        try {
//            String type = FilenameUtils.getExtension(multipartFile.getOriginalFilename());
//            String fileName = UUID.randomUUID() + "." + type;
//            String realPath = imageLocation;
//            //检测是否存在上传的目录，存在方法就什么都不做直接返回，不存在就创建
//            new File(realPath).mkdirs();
//            File destFile = new File(realPath + File.separator + fileName);
//            multipartFile.transferTo(destFile);
//            map.put("url", "/pimg/resource/" + fileName);
//            map.put("type", type);
//            map.put("title", "/pimg/resource/" + fileName);
//            map.put("original", "/pimg/resource/" + fileName);
//            map.put("size", multipartFile.getSize());
//            map.put("state", "SUCCESS");
//            return map;
//        } catch (IOException e) {
//            throw new RuntimeException(e.getMessage(), e);
//        }
//    }
//
//
//}
