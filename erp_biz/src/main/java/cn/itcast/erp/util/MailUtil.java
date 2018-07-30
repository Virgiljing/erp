package cn.itcast.erp.util;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component("mailUtil")
public class MailUtil {

    @Autowired
    private JavaMailSender sender;
    @Value("${mail.username}")
    private String from;

    public void sendMail(String title, String to, String content) throws MessagingException{
        // 创建邮件
        MimeMessage message = sender.createMimeMessage();
        // spring对邮件的包装工具类
        MimeMessageHelper helper = new MimeMessageHelper(message,"utf-8");
        // 设置邮件标题
        helper.setSubject(title);
        // 收件人
        helper.setTo(to);
        // 发件人
        helper.setFrom(from);
        // 邮件内容，第二个参数: html格式
        helper.setText(content,true);
        // 发送邮件
        sender.send(message);
    }
}
