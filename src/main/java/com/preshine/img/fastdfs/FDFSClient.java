//package com.preshine.img.fastdfs;
//
//import org.apache.commons.io.FilenameUtils;
//import org.apache.commons.io.IOUtils;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.csource.common.NameValuePair;
//import org.csource.fastdfs.*;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.Map;
//
//public class FDFSClient {
//
//    private static final Logger LOGGER = LogManager.getLogger(FDFSClient.class);
//
//
//    //    private static final String CONFIG_FILENAME = "classpath:fdfs-client.conf";
//    private static final String CONFIG_FILENAME = "fdfs-client.conf";
//
//    private static StorageClient1 storageClient1 = null;
//
//    // 初始化FastDFS Client
//    static {
//        try {
//            ClientGlobal.init(CONFIG_FILENAME);
//            TrackerClient trackerClient = new TrackerClient(ClientGlobal.g_tracker_group);
//            TrackerServer trackerServer = trackerClient.getConnection();
//            if (trackerServer == null) {
//                throw new IllegalStateException("getConnection return null");
//            }
//
//            StorageServer storageServer = trackerClient.getStoreStorage(trackerServer);
//            if (storageServer == null) {
//                throw new IllegalStateException("getStoreStorage return null");
//            }
//
//            storageClient1 = new StorageClient1(trackerServer,storageServer);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static void main(String[] args) {
////        testUpload();
////        testDownload();
//        testGetFileMetadata();
////        testDelete();
//    }
//
//    /**
//     * 上传文件
//     * @param file 文件对象
//     * @param fileName 文件名
//     * @return
//     */
//    public static String uploadFile(byte [] data, String fileName) throws Exception{
//        return storageClient1.upload_file1(data, FilenameUtils.getExtension(fileName), null);
//    }
//
//    /**
//     * 上传文件
//     * @param file 文件对象
//     * @param fileName 文件名
//     * @param metaList 文件元数据
//     * @return
//     */
//    public static String uploadFile(File file, String fileName, Map<String,String> metaList) {
//        try {
//            byte[] buff = IOUtils.toByteArray(new FileInputStream(file));
//            NameValuePair[] nameValuePairs = null;
//            if (metaList != null) {
//                nameValuePairs = new NameValuePair[metaList.size()];
//                int index = 0;
//                for (Iterator<Map.Entry<String,String>> iterator = metaList.entrySet().iterator(); iterator.hasNext();) {
//                    Map.Entry<String,String> entry = iterator.next();
//                    String name = entry.getKey();
//                    String value = entry.getValue();
//                    nameValuePairs[index++] = new NameValuePair(name,value);
//                }
//            }
//            return storageClient1.upload_file1(buff, FilenameUtils.getExtension(fileName),nameValuePairs);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    /**
//     * 获取文件元数据
//     * @param fileId 文件ID
//     * @return
//     */
//    public static Map<String,String> getFileMetadata(String fileId) {
//        try {
//            NameValuePair[] metaList = storageClient1.get_metadata1(fileId);
//            if (metaList != null) {
//                HashMap<String,String> map = new HashMap<>();
//                for (NameValuePair metaItem : metaList) {
//                    map.put(metaItem.getName(),metaItem.getValue());
//                }
//                return map;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    /**
//     * 删除文件
//     * @param fileId 文件ID
//     * @return 删除失败返回-1，否则返回0
//     */
//    public static int deleteFile(String fileId) {
//        try {
//            return storageClient1.delete_file1(fileId);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return -1;
//    }
//
//    /**
//     * 下载文件
//     * @param fileId 文件ID（上传文件成功后返回的ID）
//     * @param outFile 文件下载保存位置
//     * @return
//     */
//    public static int downloadFile(String fileId, File outFile) {
//        FileOutputStream fos = null;
//        try {
//            byte[] content = storageClient1.download_file1(fileId);
//            fos = new FileOutputStream(outFile);
//            IOUtils.write(content,fos);
//            return 0;
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            IOUtils.closeQuietly(fos);
//        }
//        return -1;
//    }
//
//    /**
//     * 文件上传测试
//     */
//    public static void testUpload() {
//        File file = new File("D:\\830C321F2C2674D265FEFA1A5C1B6CA6.jpg");
//        Map<String,String> metaList = new HashMap<String, String>();
//        metaList.put("width","1024");
//        metaList.put("height","768");
//        metaList.put("author","preshine");
//        metaList.put("date","20181009");
//        String fid = uploadFile(file, file.getName(), metaList);
//        System.out.println("upload local file " + file.getPath() + " ok, fileid=" + fid);
//    }
//
//    /**
//     * 文件下载测试
//     */
//    public static void testDownload() {
//        int r = downloadFile("group1/M00/00/00/wKgvgVu8U_aAQt6KAAAADb7JBHI141_big.txt", new File("E://test.txt"));
//        System.out.println(r == 0 ? "下载成功" : "下载失败");
//    }
//
//    /**
//     * 获取文件元数据测试
//     */
//    public static void testGetFileMetadata() {
//        Map<String,String> metaList = getFileMetadata("group1/M00/00/00/wKgvgVu8XHGAe1S3AAEt238QeVs024.jpg");
//        for (Iterator<Map.Entry<String,String>>  iterator = metaList.entrySet().iterator(); iterator.hasNext();) {
//            Map.Entry<String,String> entry = iterator.next();
//            String name = entry.getKey();
//            String value = entry.getValue();
//            System.out.println(name + " = " + value );
//        }
//    }
//
//    /**
//     * 文件删除测试
//     */
//    public static void testDelete() {
//        int r = deleteFile("group1/M00/00/00/wKgvgVu8XcyAJhpwAAEt238QeVs769.jpg");
//        System.out.println(r == 0 ? "删除成功" : "删除失败");
//    }
//
//}