package com.example.test;
 
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
 
public class testTimer {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// 写控制文件.ctl
		long startTime=System.currentTimeMillis();
		String fileRoute = "E:\\";// 文件地址路径
		
		String fileName = "CRM_TRATION_EXP_S09_20181025.dat";// 数据文件名
		String tableName = "tration_temp4ctl";// 表名
		String fieldName = "C_TANO,C_CUSTNO,C_CUSTTYPE,C_AGENCYNO,C_FUNDACCO,C_TRADEACCO,C_NETNO,C_RATIONNO,F_BALANCE,C_RATIONTERM,L_DELAY,L_ALLOWFAIL,L_RATIONDATE,F_AGIO,C_RATIONSTATUS,C_FUNDCODE,C_SHARETYPE,L_TOTALTIMES,F_TOTALBALANCE,F_TOTALSHARES,D_FIRSTDATE,D_LASTDATE,D_PROTOCOLENDDATE,D_CLOSEDATE,L_SUCTIMES,L_MAXSUCTIMES,D_CDATE,D_OPENDATE,C_SARATIONNO";
		String colAttr="VARCHAR2,VARCHAR2,CHAR,CHAR,VARCHAR2,VARCHAR2,VARCHAR2,VARCHAR2,NUMBER,VARCHAR2,NUMBER,NUMBER,NUMBER,NUMBER,VARCHAR2,VARCHAR2,CHAR,NUMBER,NUMBER,NUMBER,DATE,DATE,DATE,DATE,NUMBER,NUMBER,DATE,DATE,VARCHAR2";		
	
		String logfileName = tableName+".log";
		String ctlfileName = tableName+".ctl";// 控制文件名
		String cols=processCol(fieldName,colAttr);
 
		stlFileWriter(fileRoute, fileName, tableName, cols, ctlfileName);
		// 要执行的DOS命令
		String user = "test";
		String psw = "test";
		String Database = "//10.50.101.101:1521/uat";// IP要指向数据库服务器的地址
		
		Executive(user, psw, Database, fileRoute, ctlfileName, logfileName);
		
		long costTime=System.currentTimeMillis()-startTime;
		System.out.println(tableName+"内存："+(Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory())/1024L+"kb");
		System.out.println(tableName+"内存："+(Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory())/1024L/1024L+"Mb");
		System.out.println(tableName+"时间："+costTime/1000f+"秒");
		System.out.println(tableName+"时间："+costTime/1000/60/60f+"小时");
	}
	
	private static String processCol(String fieldName,String colAttr){
		String[] fields=fieldName.split(",");
		String[] attrs=colAttr.split(",");
		
		StringBuffer sb = new StringBuffer("(");
		for (int i=0;i<attrs.length;i++) {
			sb.append(fields[i]);
			if(attrs[i].equalsIgnoreCase("DATE")){
				sb.append(" DATE\"yyyy-MM-dd\"");
			}
			sb.append(",");
		}
 
		if (sb.charAt(sb.length() - 1) == ',') {
			sb.deleteCharAt(sb.length() - 1);
		}
		sb.append(")");
		
		return sb.toString();
	}
	
    
    /**
     * * 写控制文件.ctl
     * @param fileRoute 数据文件地址路径
     * @param fileName 数据文件名
     * @param tableName 表名
     * @param fieldName 要写入表的字段
     * @param ctlfileName 控制文件名
     * 
     * String strctl = "OPTIONS (skip=0)" +   // 0是从第一行开始  1是 从第二行
        " LOAD DATA INFILE '"+fileRoute+""+fileName+"'" +
        " APPEND INTO TABLE "+tableName+"" + //覆盖写入
        " FIELDS TERMINATED BY ',' " +  //--数据中每行记录用","分隔 ,TERMINATED用于控制字段的分隔符，可以为多个字符。
        " OPTIONALLY  ENCLOSED BY \"'\"" +  //源文件有引号 ''，这里去掉    ''''"
        " TRAILING NULLCOLS "+fieldName+"";//--表的字段没有对应的值时允许为空  源数据没有对应，写入null
     */
	public static void stlFileWriter(String fileRoute, String fileName,String tableName, String fieldName, String ctlfileName) {
		FileWriter fw = null;
		String strctl = "OPTIONS (skip=0)" + " LOAD DATA INFILE '" + fileRoute
				+ "" + fileName + "'" + "truncate  INTO TABLE " + tableName + ""
				+ " FIELDS TERMINATED BY ''" +
				// " FIELDS TERMINATED BY '0x0'" +
				// " OPTIONALLY  ENCLOSED BY \"'\"" +
				" TRAILING NULLCOLS " + fieldName + "";
		try {
			//File file = new File(fileRoute + "" + ctlfileName);
			//file.deleteOnExit();
			fw = new FileWriter(fileRoute + "" + ctlfileName);
			fw.write(strctl);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fw.flush();
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
    
    /**
     * 调用系统DOS命令
     * @param user 
     * @param psw
     * @param Database
     * @param fileRoute 文件路径
     * @param ctlfileName 控制文件名
     * @param logfileName 日志文件名
     */
    public static void Executive(String user,String psw,String Database,String fileRoute,String ctlfileName,String logfileName)
    {
        InputStream ins = null;
        //要执行的DOS命令  --数据库  用户名  密码  user/password@database
        String dos="sqlldr "+user+"/"+psw+"@"+Database+" control="+fileRoute+""+ctlfileName+" log="+fileRoute+""+logfileName;
        System.out.println(dos); // 输出
        String[] cmd = new String[]{ "cmd.exe", "/C", dos }; // 命令
        int lineCount=0;
        try
        {
            Process process = Runtime.getRuntime().exec(cmd);
            ins = process.getInputStream(); // 获取执行cmd命令后的信息
            
            BufferedReader reader = new BufferedReader(new InputStreamReader(ins));
            String line = null;
            while ((line = reader.readLine()) != null){
            	lineCount++;
            }
            System.out.println("行数："+lineCount);
            
            int exitValue = process.waitFor();
            if(exitValue==0){
                System.out.println("------返回值：" + exitValue+"\n数据导入成功-----");
            }else{
                System.out.println("------返回值：" + exitValue+"\n数据导入失败-----");
            }
            process.getOutputStream().close(); // 关闭
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}