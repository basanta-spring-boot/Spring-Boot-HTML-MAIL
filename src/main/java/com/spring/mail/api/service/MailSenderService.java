package com.spring.mail.api.service;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.spring.mail.api.dto.MailBodyContent;

@Component
public class MailSenderService {
	@Autowired
	private JavaMailSender sender;
	@Autowired
	private TemplateEngine templateEngine;

	public String sendEmail(String to, String subject, MultipartFile image, InputStreamSource imageSource)
			throws Exception {
		String templateName = "email/myTemplate";
		Context context = new Context();
		context.setVariable("Content", create());
		// add for image
		context.setVariable("imageResourceName", image.getName());
		String body = templateEngine.process(templateName, context);

		MimeMessage mail = sender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mail, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
				StandardCharsets.UTF_8.name());

		// added for attachment
		helper.addAttachment("pulpitrock.jpg", new ClassPathResource("templates/pulpitrock.jpg"));

		helper.setTo(to);
		helper.setSubject(subject);
		helper.setText(body, true);
		// ad
		helper.addInline(image.getName(), imageSource, image.getContentType());
		helper.setFrom("basant1993.dev@gmail.com");
		sender.send(mail);
		return "mail send successfully";
	}

	public MailBodyContent create() {
		MailBodyContent content = new MailBodyContent();
		content.setUsername("Basant Hota");
		List<String> technology = new ArrayList<>();
		technology.add("Spring-Boot");
		technology.add("Thymeleaf");
		technology.add("Template Engine");
		content.setTechnology(technology);
		content.setMessage("This is a sample Test mail where the body is purely HTML ");
		content.setTechnology(technology);
		return content;
	}

}
