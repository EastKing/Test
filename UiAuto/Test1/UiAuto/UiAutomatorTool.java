package com.android.test;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class UiAutomatorTool {
	private static final String StringBuffer = null;
	//工作空间目录
	private static String WORKSPACE_PATH;
	
	/**
	 * 指定自动测试的参数
	 * 
	 * @param jarName：生成jar的名字
	 * 
	 * @param testPackageclass:测试包名+类名
	 * 
	 * @param testFunction:测试方法名，空字符串代表测试所有方法
	 * 
	 * @param androidId:SDK id
	 *
	 */
	public UiAutomatorTool(String jarName,String testPackageclass,String testFunction,String androidId){
		System.out.println("************");
		System.out.println("--AutoTest Start--");
		System.out.println("************");
		
		//获取工作空间路径
		WORKSPACE_PATH=getWorkSpase();
		System.out.println("自动测试项目工作空间:\t\n"+getWorkSpase());
		
		//*******启动测试*********//
		//创建Build.xml文件
		creatBuildXml(jarName,androidId);
		//修改Build.xml文件
		modfileBuild();
		//使用Ant编译jar包
		antBuild();
		//push jar到手机
		pushJarToAndroid(WORKSPACE_PATH+"\\bin\\"+jarName+".jar");
		//测试方法，为空则测试全部方法
		if(androidId.equals("")){
			runTest(jarName,testPackageclass);
		}else{
			runTest(jarName,testPackageclass+"."+testFunction);
		}
		//*********启动测试**********//
		System.out.println("************");
		System.out.println("--AutoTest End---");
		System.out.println("************");
	}

	/**
	 *创建build.xml文件
	 */

	public void creatBuildXml(String jarName, String androidId) {
		// TODO Auto-generated method stub
		
		System.out.println("---创建build.xml开始----");
		execCmd("cmd /c android create uitest-project -n"+" "+jarName+" "+"-t"+" "+ androidId+" "+"-p"+" "+"\""+WORKSPACE_PATH+"\"");
		System.out.println("---创建Build.xml完成---");
	}
	/**
	 * 修改build.xml文件位于build type
	 */
	public void modfileBuild() {
		System.out.println("---修改build.xml文件---");
		StringBuffer stringBuffer=new StringBuffer();
		try{
			File file=new File("build.xml");
			if(file.isFile()&&file.exists()){
				InputStreamReader read=new InputStreamReader(new FileInputStream(file));
				BufferedReader bufferedReader=new BufferedReader(read);
				String lineTxt;
				while ((lineTxt=bufferedReader.readLine())!=null){
					if(lineTxt.matches(".*help.*")){
						lineTxt=lineTxt.replaceAll("help", "build");
					}
					stringBuffer = stringBuffer.append(lineTxt).append("\t\n");
				}
				read.close();
			}else{
				System.out.println("找不到build文件");
			}
			
		}catch(Exception e){
			System.out.println("读取build.xml文件出错");
			e.printStackTrace();			
		}
		//重新写回build.xml
		rewriteBuildxml("build.xml",new String(stringBuffer));
		System.out.println("----修改build.xml完成----");
		
	}
	
	/**
	 * 使用Ant编译jar包
	 */
	public void antBuild(){
		System.out.println("---编译build.xml开始---");
		execCmd("cmd /c ant -buildfile " + "\"" + WORKSPACE_PATH + "\"");
		System.out.println("---编译build.xml完成---");
		
	}
	
	/**
	 * adb push Jar包到Android手机
	 * @param localPath:localPath
	 */
	public void pushJarToAndroid(String localPath){
		System.out.println("---push jar 开始-----");
		localPath="\""+localPath+"\"";
		System.out.println("jar包路径："+localPath);
		String pushCmd = "adb push " + localPath + " /data/local/tmp/";
		System.out.println(pushCmd);
		execCmd(pushCmd);
		System.out.println("----push jar完成------");
	}
	
	/**
	 * 测试方法
	 * @param jarName:jar包名
	 */
	public void runTest(String jarName,String testName){
		System.out.println("---测试方法 开始---");
		String runCmd="adb shell uiautomator runtest";
		String testCmd = jarName + ".jar " + "-c " + testName;
		execCmd(runCmd+" "+testCmd);
		System.out.println("----测试方法  完成---");
		
	}
	
	/**
	 * 获取WorkSpace目录
	 * @return
	 */
	public String getWorkSpase() {
		File directory=new File("");
		return directory.getAbsolutePath();
	}
	/**
	 * 
	 */
	public void execCmd(String cmd){
		System.out.println("ExecCmd:"+cmd);
		try{
			Process p=Runtime.getRuntime().exec(cmd);
			//执行成功返回流
			InputStream input=p.getInputStream();
			BufferedReader reader=new BufferedReader(new InputStreamReader(input,"GBK"));
			String line;
			while ((line=reader.readLine())!=null){
				System.out.println(line);
				
			}
			//执行失败返回流
			InputStream errorInput=p.getErrorStream();
			BufferedReader errorReader=new BufferedReader(new InputStreamReader(errorInput,"GBK"));
			
			String eline;
			while ((eline=errorReader.readLine())!=null){
				System.out.println(eline);
			}
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	/**
	 * 重新写回Build.xml
	 * @param path
	 */
	public void rewriteBuildxml(String path,String content){
		File dirFile=new File(path);
		if(!dirFile.exists()){
			dirFile.mkdir();
		}
		try{
			BufferedWriter bwl=new BufferedWriter(new FileWriter(path));
			bwl.write(content);
			bwl.flush();
			bwl.close();
			
		}catch(IOException e){
			e.printStackTrace();
			
		}
	}
	
}
