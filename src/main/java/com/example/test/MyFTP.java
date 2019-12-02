package com.example.test;
 
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
 
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
/**
 * 
 * @author zhou_dong
 * 
 *	服务器上传文件 2014-03-07 17:03
 */
public class MyFTP {
 
	public static void main(String[] args) {
		
		File imagefile = new File("C:/Users/YunFengLiu/Desktop/RBMQ/cc.txt");
		String imagefileFileName = "苹果.jpg";
		//创建ftp客户端
		FTPClient ftpClient = new FTPClient();
		ftpClient.setControlEncoding("GBK");
		String hostname = "9.111.139.176";
		int port = 21;
		String username = "root";
		String password = "Password";
		try {
			//链接ftp服务器
			ftpClient.connect(hostname, port);
			//登录ftp
			ftpClient.login(username, password);
			int  reply = ftpClient.getReplyCode();  
			System.out.println(reply);
			//如果reply返回230就算成功了，如果返回530密码用户名错误或当前用户无权限下面有详细的解释。
			if (!FTPReply.isPositiveCompletion(reply)) {  
	            	ftpClient.disconnect();  
	                return ;  
	            }  
		        ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
				
	            ftpClient.makeDirectory("path");//在root目录下创建文件夹
	            String remoteFileName = System.currentTimeMillis()+"_"+imagefileFileName;
	            InputStream input = new FileInputStream(imagefile); 
	            ftpClient.storeFile(remoteFileName, input);//文件你若是不指定就会上传到root目录下
	            input.close();  
	            ftpClient.logout();  
			
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally 
		{  
            if (ftpClient.isConnected())
            {  
                try 
                {  
                	ftpClient.disconnect();  
                } catch (IOException ioe) 
                {  
					ioe.printStackTrace();
                }  
            } 
	
		}
	}
		
}