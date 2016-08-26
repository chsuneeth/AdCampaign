package com.comcast.project.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.comcast.project.exception.DuplicateKeyException;
import com.comcast.project.exception.NoActiveCampaignException;
import com.comcast.project.utill.CommonUtils;
import com.comcast.project.vo.Campaign;

/**
 * 
 * @author SUNEETH
 *
 */
@RestController
public class AdCampaignController {

	private static final Logger LOGGER = Logger.getLogger(AdCampaignController.class);

	static Map<String, Campaign> storeObject = null;

	/* This will register the data by capturing the POST data */
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public String registerAdd(@RequestBody Campaign campaign,HttpServletRequest request) throws DuplicateKeyException {
		
		String status = null;
		LOGGER.debug("Inside register add method");
		LOGGER.debug(campaign.toString());
		
		//Now add end time to the campaign object
		campaign.setTimeStamp(CommonUtils.getEndTime(Integer.parseInt(campaign.getDuration())));
		
		if (storeObject == null)
			storeObject = new HashMap<String, Campaign>();
		
		if (storeObject.containsKey(campaign.getPartnerId())) {
			
			//now compare end time
			Campaign oldCampaign = storeObject.get(campaign.getPartnerId());
			
			if (CommonUtils.compareDate(oldCampaign.getTimeStamp()))  {
				
				status = "failed to store Object";
				LOGGER.debug("I can't store the object");
				
				throw new DuplicateKeyException("Already one active Ad campaign is playing for this partner id");
				
			} else {
				
				//System.out.println("In Else block");
				status = "updated store Object";
				storeObject.put(campaign.getPartnerId(), campaign);
			}
		}else{
			
			//System.out.println("In Else block");
			storeObject.put(campaign.getPartnerId(), campaign);
			status = "Added store Object";
		}
		return status;
	}

	/* This will register the data by capturing the POST data */
	@RequestMapping(value = "/get", method = RequestMethod.GET, produces = { "application/json" })
	@ResponseBody
	public Campaign getCampaign(@RequestParam(value = "partnerId", required = true) String partnerId) throws Exception {
		
		LOGGER.debug("Inside getCampaign method");
		
		Campaign campaign = null;
		if (storeObject != null) {
			campaign = storeObject.get(partnerId);
			
			if (!CommonUtils.compareDate(campaign.getTimeStamp())) {
				throw new NoActiveCampaignException("No Active Campaign Exist");
			} 
		}
		return campaign;
		
	}
}
