package com.spring.mail.api.controller;

import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.spring.mail.api.dto.MailRequest;
import com.spring.mail.api.service.MailSenderService;

@RestController
@PropertySource("classpath:application.properties")
public class MailSenderController {

	@Autowired(required = true)
	private MailSenderService service;
	@Value("${inlineImage}")
	String templateMailBodyImageVal;
	InputStreamSource imageSource = null;

	@RequestMapping("/sendHtmlEmail")
	public String send(@RequestBody MailRequest request) throws Exception {
		MultipartFile image = getImageContent();
		return service.sendEmail(request.getMailTo(), request.getSubject(), image, imageSource);
	}

	private MultipartFile getImageContent() throws Exception {
		InputStream imageIs = null;
		byte[] imageByteArray = null;
		MultipartFile multipartFile = null;
		imageIs = this.getClass().getClassLoader().getResourceAsStream("templates/" + templateMailBodyImageVal);
		imageByteArray = IOUtils.toByteArray(imageIs);
		multipartFile = new MockMultipartFile(imageIs.getClass().getName(), imageIs.getClass().getName(), "image/jpeg",
				imageByteArray);
		imageSource = new ByteArrayResource(imageByteArray);
		return multipartFile;
	}
}
