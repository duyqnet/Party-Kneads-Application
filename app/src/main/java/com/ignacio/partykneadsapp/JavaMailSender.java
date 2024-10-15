package com.ignacio.partykneadsapp;

import android.os.AsyncTask;
import android.util.Log;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class JavaMailSender extends AsyncTask<Void, Void, Void> {
    private String email;
    private String subject;
    private String message;

    public JavaMailSender(String email, String subject, String message) {
        this.email = email;
        this.subject = subject;
        this.message = message;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");

            Session session = Session.getInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("partykneads@gmail.com", "btoqroqphwaqskqo"); // Use your Gmail and App Password
                }
            });

            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress("partykneads@gmail.com"));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            msg.setSubject(subject);
            msg.setText(message);
            Transport.send(msg);
            Log.d("JavaMailSender", "Email sent successfully");
        } catch (Exception e) {
            Log.e("JavaMailSender", "Error sending email: " + e.getMessage());
        }
        return null;
    }
}
