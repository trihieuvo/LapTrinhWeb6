package com.votrihieu.BAI04.utils;

import java.util.Properties;
import java.util.Random;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

public class EmailUtil {
    
   
    private static final String FROM_EMAIL = "hieu9810.vn@gmail.com"; 
    private static final String APP_PASSWORD = "gzkk nquh groh lpyz"; 

    public static void sendOtpEmail(String toEmail, String otp) {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Authenticator auth = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM_EMAIL, APP_PASSWORD);
            }
        };
        
        Session session = Session.getInstance(props, auth);

        try {
            MimeMessage msg = new MimeMessage(session);
            msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
            msg.setFrom(new InternetAddress(FROM_EMAIL));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
            msg.setSubject("Mã OTP để đặt lại mật khẩu của bạn", "UTF-8");
            
            String emailContent = "<h3>Xin chào,</h3>"
                                + "<p>Bạn đã yêu cầu đặt lại mật khẩu. Mã OTP của bạn là:</p>"
                                + "<h1 style='color:blue;'>" + otp + "</h1>"
                                + "<p>Mã này sẽ hết hạn sau 10 phút. Vui lòng không chia sẻ mã này cho bất kỳ ai.</p>"
                                + "<p>Trân trọng.</p>";
            
            msg.setContent(emailContent, "text/html; charset=UTF-8");

            Transport.send(msg);
            System.out.println("Email gửi thành công!");
        } catch (Exception e) {
            System.out.println("Gặp lỗi khi gửi email:");
            e.printStackTrace();
        }
    }
    
    public static String generateOtp() {
        // Tạo OTP 6 chữ số
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }
}