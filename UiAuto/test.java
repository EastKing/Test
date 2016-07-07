package com.android.test;

import android.os.RemoteException;

import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.core.UiScrollable;
import com.android.uiautomator.core.UiSelector;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;

public class test extends UiAutomatorTestCase{
	public void testDemo() throws UiObjectNotFoundException, RemoteException{
//		getUiDevice().pressEnter();
		getUiDevice().pressHome();
		UiObject settingApp=new UiObject(new UiSelector().text("设置"));
		settingApp.click();
		//休眠三秒
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
			// TODO: handle exception
		}
		//进入语言和输入法设置界面
		
		UiScrollable settingItems=new UiScrollable(new UiSelector().scrollable(true));
		UiObject lanuageAndInputItem=settingItems.getChildByText(new UiSelector().text("语言和输入法"), "语言和输入法",true);
		lanuageAndInputItem.clickAndWaitForNewWindow();//点击窗口并等待窗口刷新
		
		UiObject setLan=new UiObject(new UiSelector().text("语言"));
		setLan.click();
	
		UiScrollable setLanuage=new UiScrollable(new UiSelector().scrollable(true));
		UiObject setLanItem=setLanuage.getChildByText(new UiSelector().text("English (United States)"), "English (United States)",true);
		setLanItem.clickAndWaitForNewWindow();
		
		
		//log输出
		System.out.println("setLanItem-->"+setLanItem.getPackageName());
		
		//由于无法识别中文，我们在这里选择坐标点击去选择"简体中文"选项
//		getUiDevice().click(290, 742);
		
		getUiDevice().pressHome();
	}
	public static void main(String[] args) {
//		UiAutomatorTool tool=new UiAutomatorTool("UiTest1", "com.android.test", "test", "1");
		new UiAutomatorTool("UiTest1", "com.android.test","test", "1");
	}

}
