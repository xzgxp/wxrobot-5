package com.xll.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xll.bean.WXData;
import com.xll.constant.Constants;
import com.xll.service.MessageService;
import com.xll.util.MyWebClient;
import com.xll.util.PicBootstrap;


/**
 *@author xialonglei
 *@date  2016/10/02 
 */
@Controller
@RequestMapping("/wx")
public class WXController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(WXController.class);
	
	@Resource
	private MessageService messageServiceImpl;
	
	@Resource
	private MyWebClient myWebClient;
	
	@RequestMapping("/getImage")
	public String getImage(HttpSession session){
		new Thread(new PicBootstrap()).start();
		try {
			myWebClient.visitUrl(Constants.LOGIN_URL);
			Thread.sleep(1000);
		} catch (Exception e) {
			LOGGER.error("" , e);
		}
		session.setAttribute("qrUrl", WXData.getSingleton().getQrUrl());
		return "qr";
	}
	
	@RequestMapping("/sendMessage")
	@ResponseBody
	public String sendMessage(){
		if(WXData.getSingleton() != null && WXData.getSingleton().getContactors() != null && WXData.getSingleton().getContactors().get("One、Life") != null){
			List<String> sendUsernames = new ArrayList<String>();
			sendUsernames.add(WXData.getSingleton().getContactors().get("One、Life").getUserName());
			try{
				messageServiceImpl.sendTextMessage(sendUsernames , "??");
			}catch(Exception e){
				LOGGER.error("" , e);
				return Constants.SEND_MSG_FAILURE;
			}
			
			return Constants.SEND_MSG_SUCCESS;
		}else{
			return Constants.LOGIN_HINT;
		}
		
	}
	
	@RequestMapping("/sendMultiMessage")
	@ResponseBody
	public String sendMultiMessage(@RequestParam String path){
		if(WXData.getSingleton() != null && WXData.getSingleton().getContactors() != null && WXData.getSingleton().getContactors().get("One、Life") != null){
			List<String> sendUsernames = new ArrayList<String>();
			sendUsernames.add(WXData.getSingleton().getContactors().get("One、Life").getUserName());
			try{
				messageServiceImpl.sendMultiMessage(sendUsernames , path);
			}catch(Exception e){
				LOGGER.error("" , e);
				return Constants.SEND_MSG_FAILURE;
			}
			
			return Constants.SEND_MSG_SUCCESS;
		}else{
			return Constants.LOGIN_HINT;
		}
		
	}
	
}
