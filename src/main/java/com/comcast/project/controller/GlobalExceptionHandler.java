package com.comcast.project.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.comcast.project.exception.DuplicateKeyException;
import com.comcast.project.exception.NoActiveCampaignException;
import com.comcast.project.vo.Error;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(DuplicateKeyException.class)
	public @ResponseBody Error returnErrorMessage(DuplicateKeyException exception){
		return new Error("DuplicateKeyException", exception.getMessage());
	}
	
	@ExceptionHandler(NoActiveCampaignException.class)
	public @ResponseBody Error returnNoCampaignErrorMessage(NoActiveCampaignException exception){
		return new Error("NoActiveCampaignException", exception.getMessage());
	}
}
