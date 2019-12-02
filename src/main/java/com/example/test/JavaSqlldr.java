package com.example.test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class JavaSqlldr {

    public static final String ctlFileDir = "C:\\Users\\YunFengLiu\\Desktop\\RBMQ\\";
    public static final String logFileDir = "C:\\Users\\YunFengLiu\\Desktop\\RBMQ\\";

    public static void main(String[] args) {

        String username = "staging";
        String password = "staging";
        String Database = "helowin";
        String ctlFileName = "orc.ctl";
        String logFileName = "test_table.log";
        boolean isDBA = true;

        String command = buildCommand(
                            username, 
                            password, 
                            Database, 
                            isDBA,
                            JavaSqlldr.ctlFileDir, 
                            ctlFileName, 
                            JavaSqlldr.logFileDir, 
                            logFileName
                            );

        Executive(command);
    }

    /**
     * @param username
     * @param password
     * @param Database
     * @param isDBA
     * @param ctlFileDir
     * @param ctlFileName
     * @param logFileDir
     * @param logFileName
     * @return 
     *      the sql loader command
     */
    public static String buildCommand(
            String username, 
            String password, 
            String Database,
            boolean isDBA,
            String ctlFileDir, 
            String ctlFileName,
            String logFileDir, 
            String logFileName) {

        StringBuffer command = new StringBuffer();
        command.append("sqlldr ");
//        command.append(isDBA ? "'" : "");
        command.append(username);
        command.append("/");
        command.append(password);
        command.append("@9.111.139.176:1521/");
        command.append(Database);
//        command.append(isDBA ? " as sysdba'" : "");
        command.append(" control=" + ctlFileDir + ctlFileName);
        command.append(" log=" + logFileDir + logFileName);

        System.out.println("Command : " + command.toString());
        //command : sqlldr 'SYS/123@ORCL as sysdba' control=C:\test.ctl log=C:\test_table.log
        return command.toString();

    }


    /**
     * To call DOS command
     */
    public static void Executive(String command) {

        InputStream ins = null;
        command = "sqlldr userid = staging/staging@//9.111.139.176:1521/helowin   control = C:/Users/YunFengLiu/Desktop/RBMQ/orc.ctl   bad=C:/Users/YunFengLiu/Desktop/RBMQ/bad.bad   log = C:/Users/YunFengLiu/Desktop/RBMQ/log.log";

        String[] cmd = new String[] { "cmd.exe", "/C", command }; // 命令
        try {
            Process process = Runtime.getRuntime().exec(cmd);
            ins = process.getInputStream(); // 获取执行cmd命令后的信息

            BufferedReader reader = new BufferedReader(new InputStreamReader(ins));
            String line = null;
            while ((line = reader.readLine()) != null) {
                String msg = new String(line.getBytes("ISO-8859-1"), "UTF-8");
                System.out.println(msg); // 输出
            }
            int exitValue = process.waitFor();

            System.out.println("Returned value was：" + exitValue);

            if(exitValue == 0) {
                System.out.println("The records were loaded successfully");
            }else {
                System.out.println("The records were not loaded successfully");
            }

            process.getOutputStream().close(); // 关闭
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}