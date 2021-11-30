package com.gangling.scm.base.middleware.email.param;

import com.gangling.scm.base.common.exception.ArgumentException;
import com.gangling.scm.base.common.exception.ServerException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.util.CollectionUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.List;

@Slf4j
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class EmailParam {
    private String from;
    private String replyTo;
    private String[] to;
    private String[] cc;
    private String[] bcc;
    private Date sentDate;
    private String subject;
    private String text;
    private boolean isRich;
    private List<EmailAttachment> attachmentList;

    public void validate() {
        if (ArrayUtils.isEmpty(to)) {
            throw new ArgumentException("收件人不能为空");
        }
    }

    private void copyToMessageHelper(MimeMessageHelper helper) throws MessagingException {
        helper.setFrom(from);
        helper.setTo(to);
        if (StringUtils.isNotBlank(replyTo)) {
            helper.setReplyTo(replyTo);
        }
        if (ArrayUtils.isNotEmpty(cc)) {
            helper.setCc(cc);
        }
        if (ArrayUtils.isNotEmpty(bcc)) {
            helper.setBcc(bcc);
        }
        if (sentDate != null) {
            helper.setSentDate(sentDate);
        }
        helper.setSubject(subject);
    }

    public void copyAttachmentMessage(MimeMessage message) {
        try{
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            copyToMessageHelper(helper);
            helper.setText(text, isRich);
            if (!CollectionUtils.isEmpty(attachmentList)) {
                for (EmailAttachment emailAttachment : attachmentList) {
                    if (emailAttachment.getByteArrayResource() != null) {
                        helper.addAttachment(emailAttachment.getFileName(), emailAttachment.getByteArrayResource());
                    } else {
                        helper.addAttachment(emailAttachment.getFileName(), emailAttachment.getFile());
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ServerException(e.getMessage());
        }
    }

    public void copyRichMessage(MimeMessage message) {
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            copyToMessageHelper(helper);
            helper.setText(text, true); //第二个参数表明这是一个HTML
    //        helper.setText("<html><body><img src='cid:testLogo'>"
    //                + "<h4>Hello World!!!</h4>"
    //                + "</body></html>", true);
                //src='cid:testLogo'表明在消息中会有一部分是图片并以testLogo来进行标识
    //        ClassPathResource image = new ClassPathResource("logo.jpg");
    //        System.out.println(image.exists());
    //        helper.addInline("testLogo", image);//添加内联图片，第一个参数表明内联图片的标识符，第二个参数是图片的资源引用
            if (!CollectionUtils.isEmpty(attachmentList)) {
                for (EmailAttachment emailAttachment : attachmentList) {
                    if (emailAttachment.getByteArrayResource() != null) {
                        helper.addInline(emailAttachment.getInlineParamName(), emailAttachment.getByteArrayResource(), emailAttachment.getContentType());
                    } else {
                        helper.addInline(emailAttachment.getInlineParamName(), emailAttachment.getFile());
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ServerException(e.getMessage());
        }
    }

    public SimpleMailMessage convertToSimpleMessage() {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(from);
        mailMessage.setTo(to);
        if (StringUtils.isNotBlank(replyTo)) {
            mailMessage.setReplyTo(replyTo);
        }
        if (ArrayUtils.isNotEmpty(cc)) {
            mailMessage.setCc(cc);
        }
        if (ArrayUtils.isNotEmpty(bcc)) {
            mailMessage.setBcc(bcc);
        }
        if (sentDate != null) {
            mailMessage.setSentDate(sentDate);
        }
        mailMessage.setSubject(subject);
        mailMessage.setText(text);
        return mailMessage;
    }
}
