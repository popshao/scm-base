package com.gangling.scm.base.middleware.email;

import com.gangling.scm.base.middleware.email.param.EmailParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;

@Slf4j
@Component
public class SpringEmailUtil {
    @Value("${mail.from}")
    private String from;

    @Resource
    private JavaMailSender emailSender;

    /**
     * 发送普通邮件
     *
     * @param emailParam
     */
    public void sendSimpleEmail(EmailParam emailParam) {
        emailParam.validate();
        emailParam.setFrom(from);
        SimpleMailMessage message = emailParam.convertToSimpleMessage();
        emailSender.send(message);
    }

    /**
     * 发送富文本邮件，支持内嵌附件
     *
     * @param emailParam
     */
    public void sendRichEmail(EmailParam emailParam) {
        emailParam.validate();
        emailParam.setFrom(from);
        MimeMessage message = emailSender.createMimeMessage();
        emailParam.copyRichMessage(message);
        emailSender.send(message);
    }

    /**
     * 发送带附件邮件
     *
     * @param emailParam
     */
    public void sendAttachmentEmail(EmailParam emailParam) {
        emailParam.validate();
        emailParam.setFrom(from);
        MimeMessage message = emailSender.createMimeMessage();
        emailParam.copyAttachmentMessage(message);
        emailSender.send(message);
    }
}
