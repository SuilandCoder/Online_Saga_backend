package com.liber.sun.utils;

import java.io.*;

/**
 * Created by sunlingzhi on 2017/10/31.
 */
public class ProcessUtils {

    public static boolean callCmd(String locationCmd) throws IOException {
        String strcmd = "cmd /c start  " + locationCmd;
        Runtime rt = Runtime.getRuntime();
        Process ps = null;
        try {
            ps = rt.exec(strcmd);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        try {
            ps.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int i = ps.exitValue();
        if (i == 0) {
            System.out.println("cmd starts runing.");

        } else {

            System.out.println("cmd starts error.");
            return false;
        }
        ps.destroy();
        ps = null;

        // 批处理执行完后，根据cmd.exe进程名称
        new ProcessUtils().killProcess();
        return true;
    }

    public static String StartProcess(String cmd) throws IOException {
        String strcmd = cmd;
        Runtime rt = Runtime.getRuntime();
        Process ps = null;
        String rdltStr = "";
        try {
            ps = rt.exec(strcmd);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        try {

            InputStream is = ps.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "UTF-8");
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
                rdltStr += line + "\n";
            }

            ps.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int i = ps.exitValue();
        if (i == 0) {
            // System.out.println("cmd starts runing.");

        } else {

            System.out.println("cmd starts error.");
            return "";
        }
        ps.destroy();
        ps = null;

        // 批处理执行完后，根据cmd.exe进程名称
        new ProcessUtils().killProcess();
        return rdltStr;
    }

    public void killProcess() {
        Runtime rt = Runtime.getRuntime();
        try {
            rt.exec("cmd.exe /C start wmic process where name='cmd.exe' call terminate");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean ProcessHasEnd(String ProcessName) throws IOException {
        int tasklist1 = -1;
        try {
            String cmd1 = "cmd.exe /c  tasklist";
            Process p = Runtime.getRuntime().exec(cmd1);
            StringBuffer out = new StringBuffer();
            byte[] b = new byte[1024];
            for (int n; (n = p.getInputStream().read(b)) != -1;) {
                out.append(new String(b, 0, n));
            }
            tasklist1 = out.toString().indexOf(ProcessName);// 检查进程
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        if (tasklist1 == -1) {
            return false;
        } else {
            System.out.println("model is running，please wait...");
            return true;
        }
    }


    /**
     *
     * @param modelPath
     *             model文件夹所在的路径
     * @param modelName
     *             可执行文件文件名（*.exe）+Parameters
     * @return
     */
    public static boolean runModel(String modelPath, String modelName) {

        if (modelPath == null || modelPath.equals("") || modelName == null
                || modelName.equals("")) {
            System.out.println("modelPath or modelExEName can not be null");
            return false;
        }
        try {

            ProcessBuilder builder = new ProcessBuilder("cmd", "/c", " "
                    + modelName);
            builder.directory(new File(modelPath));
            Process process;
            process = builder.start();
            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "UTF-8");
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
            process.waitFor();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return true;
    }

    /**
     *
     * @param modelPath
     *            model文件夹所在的路径
     * @param modelName
     *            可执行文件文件名（*.exe）
     * @param parameter
     *            模型运行时需要输入的参数,若无则输入null or ""
     * @return
     */
    public static boolean runModel(String modelPath, String modelName,
                                   String parameter) {
        if (modelPath == null || modelPath.equals("") || modelName == null
                || modelName.equals("")) {
            System.out.println("modelPath or modelExEName can not be null");
            return false;
        }
        if (parameter == null || parameter.equals("")) {
            if (runModel(modelPath, modelName)) {
                return true;
            } else {
                System.out.println("Process run error.");
                return false;
            }

        }
        try {
            ProcessBuilder builder = new ProcessBuilder("cmd", "/c", " "
                    + modelName);
            builder.directory(new File(modelPath));
            Process process;
            process = builder.start();
            // 输入参数
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                    process.getOutputStream()));
            bw.write(parameter);
            bw.newLine();
            bw.flush();

            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "GB2312");
            BufferedReader br = new BufferedReader(isr);
            String line;
            String lastLine = "";
            while ((line = br.readLine()) != null) {

                System.out.println(line);
                if (lastLine.equals(line)) {
                    bw.newLine();
                    bw.flush();
                }
                lastLine = line;
            }
            process.waitFor();
            bw.close();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return true;
    }
}
