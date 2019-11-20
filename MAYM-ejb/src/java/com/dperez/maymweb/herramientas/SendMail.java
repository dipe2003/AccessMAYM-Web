
package com.dperez.maymweb.herramientas;

import java.util.Properties;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author dperez
 */
public class SendMail {
    @EJB
    private ControladorPropiedad prop;
    private static String user;
    private static String pass;
    private static String mail;
    private static String host;
    private static int puerto;
    
    private static Properties props;
    private static Session session;
    
    public SendMail() {}
        
    @PostConstruct
    public void init(){
        user = prop.getMailUser();
        pass = prop.getMailPass();
        mail = prop.getMailFrom();
        host = prop.getMailSmtp();
        puerto = prop.getMailPort();
        
        props = new Properties();
        props.put("mail.transport.protocol", "smtp");
        //props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.port", puerto);              
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.auth", "true");
        
        session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(user, pass);
                    }
                });

    }
        
    public boolean enviarMail(String to,String mensaje, String asunto){
        
        try{
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(mail));
            
            // Set To
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            
            // Set Asunto
            message.setSubject(asunto);
            
            // Set contenido de mail en html
            message.setContent(mensaje, "text/html" );
            
            // Enviar mensaje
            Transport.send(message);
            System.out.println("El mensaje fue enviado.");
            return true;
        }catch (MessagingException mex) {
            //mex.printStackTrace();
            System.out.println("El mensaje no fue enviado: " + mex.getMessage());
        }
        return false;
    }
}

