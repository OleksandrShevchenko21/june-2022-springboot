//package ua.com.owu.june2022springboot.services;
//
//
//import jakarta.mail.MessagingException;
//import jakarta.mail.internet.InternetAddress;
//import jakarta.mail.internet.MimeMessage;
//import lombok.AllArgsConstructor;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.MimeMessageHelper;
//import org.springframework.stereotype.Service;
//import ua.com.owu.june2022springboot.models.Customer;
//
//@Service
//@AllArgsConstructor
//public class MailService {
//
//    private JavaMailSender javaMailSender;
//
//
//    public void send(Customer customer){
//
//        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
//        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
//
//        try {
//            helper.setTo(customer.getEmail());
//            helper.setText("dsgfdsgfdsg");
//            helper.setFrom(new InternetAddress("shevchenko2106@gmail.com"));
//        } catch (MessagingException e) {
//            throw new RuntimeException(e);
//        }
//
//
//        javaMailSender.send(mimeMessage);
//    }
//}
package ua.com.owu.june2022springboot.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import ua.com.owu.june2022springboot.models.Customer;

@Service
@AllArgsConstructor
public class MailService {

    private JavaMailSender javaMailSender;

    public void send(Customer customer) {
        System.out.println(customer);

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        try {
            helper.setTo(customer.getEmail());
//            helper.setText("<a href='http://localhost:8080/customers/activate/"+customer.getActivationToken().getToken()+"'>click to activate</a>", true);
            helper.setText("<a href='http://localhost:8080/customers/activate/"+customer.getId()+"'>click to activate</a>",true);
            helper.setFrom(new InternetAddress("shevchenko2106@gmail.com"));
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        javaMailSender.send(mimeMessage);

    }
}
