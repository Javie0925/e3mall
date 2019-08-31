package cn.e3mall.fast;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.junit.Test;

import cn.e3mall.common.utils.FastDFSClient;

public class FastDfsTest {
	

	@Test
	public void testFast() throws FileNotFoundException, IOException, MyException{
		//创建一个配置文件，文件名任意，内容就是tracker服务器地址
		//使用全局对象加载配置文件
		ClientGlobal.init("F:/Java/MyeclipseWorkspace/e3-magager-web/src/main/resources/config/client.conf");
		//创建一个TrackerClient对象
		TrackerClient trackClient = new TrackerClient();
		//通过TrackerClient获得一个TrackerServer
		TrackerServer trackerServer = trackClient.getConnection();
		//创建一个StorageServer的应用，可以是null
		StorageServer storageServer = null;
		//创建一个StorageClient，参数需要TrackerService和StorageServer
		StorageClient storageClient = new StorageClient(trackerServer, storageServer); 
		//使用StorageClient上传文件
		String[] strings = storageClient.upload_file("C:/Users/lenovo/Desktop/axl.jpg", "jpg", null);
		for(String s:strings){
			System.out.println(s);
		}
	}
	@Test
	public void testFastDFSClient() throws Exception{
		
		FastDFSClient fastDFSClient = new FastDFSClient("F:/Java/MyeclipseWorkspace/e3-magager-web/src/main/resources/config/client.conf");
		String string = fastDFSClient.uploadFile("C:/Users/lenovo/Desktop/2.jpg", "jpg");
		System.out.println(string);
	}
}
