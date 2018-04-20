package com.zqw.fileoperation.functions;

/**
 * Created by 51376 on 2018/4/19.
 */

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class MyCompress {


    //filePathes列表最后一个为压缩文件路径
    public static boolean execCompress(List<String> filePathes) {
        String zipFilePath = filePathes.get(filePathes.size()-1);
        filePathes.remove(filePathes.size()-1);
        ZipOutputStream zipOutputStream = null;
        BufferedOutputStream bufferedOutputStream = null;
        try {
            zipOutputStream = new ZipOutputStream(new FileOutputStream(zipFilePath));
            bufferedOutputStream = new BufferedOutputStream(zipOutputStream, 1048576);
            for (String sourceFilePath : filePathes) {
                System.out.println("压缩中...");
                File file = new File(sourceFilePath);
                compress(zipOutputStream, bufferedOutputStream, file, "");
                System.out.println("压缩完成!");
            }
            return true;
        } catch (IOException e) {
            File file = new File(zipFilePath);
            if (file.exists()) file.delete();
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (bufferedOutputStream != null)
                    bufferedOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean execDecompress(String sourceZipFileName, String DestFilePath) {
        ZipInputStream zipInputStream = null;
        try {
            zipInputStream = new ZipInputStream(new BufferedInputStream(new FileInputStream(sourceZipFileName)));
            ZipEntry entry;
            while ((entry = zipInputStream.getNextEntry()) != null) {
                BufferedOutputStream bufferedOutputStream = null;
                try {
                    System.out.println("提取" + entry.getName() + entry.getName().lastIndexOf("/"));
                    int lastIndex = entry.getName().lastIndexOf("/");
                    String preFixPath = "/";
                    if (lastIndex != -1) {
                        preFixPath = entry.getName().substring(0, entry.getName().lastIndexOf("/") + 1);
                        File file = new File(DestFilePath + preFixPath);
                        if (!file.exists()) new File(DestFilePath + preFixPath).mkdirs();
                    }
                    if (entry.isDirectory()) {
                        continue;
                    }
                    bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(DestFilePath + entry.getName()), 1048576);
                    int count;
                    byte[] bytes = new byte[1048576];
                    while ((count = zipInputStream.read(bytes, 0, 1048576)) != -1) {
                        bufferedOutputStream.write(bytes, 0, count);
                    }
                    bufferedOutputStream.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                } finally {
                    if (bufferedOutputStream != null)
                        bufferedOutputStream.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                zipInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    private static void compress(ZipOutputStream out, BufferedOutputStream bufferedOutputStream, File sourceFile, String base) throws IOException {
        if (sourceFile.isDirectory()) {
            File[] files = sourceFile.listFiles();
            if (files.length == 0) {
                out.putNextEntry(new ZipEntry(base + sourceFile.getName() + "/"));
            } else {
                for (File file : files) {
                    compress(out, bufferedOutputStream, file, base + sourceFile.getName() + "/");
                }
            }
        } else {
            BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(sourceFile));
            out.putNextEntry(new ZipEntry(base + sourceFile.getName()));
            int tag;
            while ((tag = bufferedInputStream.read()) != -1) {
                //  out.write(tag);
                bufferedOutputStream.write(tag);
                //flush之后才能真正写入磁盘
                bufferedOutputStream.flush();
            }
            bufferedInputStream.close();
        }
    }

    public static void test() {

        File file = new File("newfolder1");
        //  String src = file.getAbsolutePath();
        String src1 = "C:\\Users\\51376\\IdeaProjects\\CSPTest\\tt1.pdf";
        String src2 = "C:\\Users\\51376\\IdeaProjects\\CSPTest\\pic1.jpg";
        String src3 = "C:\\Users\\51376\\IdeaProjects\\CSPTest\\pic2.jpg";
        String src4 = "C:\\Users\\51376\\IdeaProjects\\CSPTest\\newfolder1";
        String src5 = "C:\\Users\\51376\\IdeaProjects\\CSPTest\\emptyfolder";
        List<String> list = new LinkedList<>();
        list.add(src1);
        list.add(src2);
        list.add(src3);
        list.add(src4);
        //String src = "C:\\Users\\51376\\IdeaProjects\\CSPTest\\tt1.pdf";
        String srcZip = "C:\\Users\\51376\\IdeaProjects\\CSPTest\\Mutil4.zip";
        String destPath = "C:/Users/51376/IdeaProjects/CSPTest/mypotplayer/";
        String srcPath = "C:/Users/51376/IdeaProjects/CSPTest/PotPlayer";
        String destPath1 = "C:/Users/51376/IdeaProjects/CSPTest/Clion.zip";
//       // String dest = "second1.zip";
        //  execCompress(list, dest);
        //execCompress(srcPath, destPath1);
        execDecompress(destPath1, destPath);


    }

    public static void main(String[] args) throws Exception {
        test();
    }

    public static void test1() {
        File file = new File("C:/Users/51376/IdeaProjects/CSPTest/mypotplayer");
        System.out.println(file.exists());
    }

}
