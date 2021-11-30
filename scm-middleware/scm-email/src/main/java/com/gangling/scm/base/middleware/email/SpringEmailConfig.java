package com.gangling.scm.base.middleware.email;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class SpringEmailConfig {
    @Value("${mail.smtp}")
    private String mailHost;
    @Value("${mail.smtp.port}")
    private int port;
    @Value("${mail.username}")
    private String userName;
    @Value("${mail.password}")
    private String password;

    /**
     * 配置邮件发送器
     *
     * @return
     */
    @Bean
    public MailSender emailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(mailHost);//指定用来发送Email的邮件服务器主机名
        mailSender.setPort(port);//默认端口，标准的SMTP端口
        mailSender.setUsername(userName);//用户名
        mailSender.setPassword(password);//密码
        mailSender.setDefaultEncoding("UTF-8");

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        props.put("mail.smtp.port", port);
        props.put("mail.smtp.socketFactory.port", port);
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        // 设置超时时间，避免阻塞线程
        props.put("mail.smtp.connectiontimeout", 5000);
        props.put("mail.smtp.timeout", 3000);
        props.put("mail.smtp.writetimeout", 5000);

        //        props.put("mail.debug", "true");
        return mailSender;
    }
}
