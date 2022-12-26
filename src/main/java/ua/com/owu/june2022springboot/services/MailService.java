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

@Service  // чтоб в будущем создался Bean
@AllArgsConstructor
public class MailService {

    private JavaMailSender javaMailSender;  // в библиотеке уже существует механизм, который делает всю логику.
                                            // Ему необходимы только application.properties
                                            // Необходимо сказать в какой момент и какое сообщение отправлять

    public void send(Customer customer) {
        System.out.println(customer);

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();  //тип, который используется в почте для того, чтоб отправлять сообщения и файла разного формата
                                                                        // сам MimeMessage не имеет удобных методов, чтоб с ним работать
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage); // создаем MimeMessageHelper, он добавляет дополнительные характеристики Паттаерн -декоратор
        try {
            helper.setTo(customer.getEmail()); //выбираем куда отправлять сообщение. Берем email с customer
//            helper.setText("<a href='http://localhost:8080/customers/activate/"+customer.getActivationToken().getToken()+"'>click to activate</a>", true);
            helper.setText("<a href='http://localhost:8080/customers/activate/"+customer.getId()+"'>click to activate</a>",true);//текст, который хотим отправить. Вмещает html разметку true
            helper.setFrom(new InternetAddress("shevchenko2106@gmail.com"));//от чьего имени отправляем сообщение. Это надо, чтоб нормально подписывались сертификаты если работаем на windows
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        javaMailSender.send(mimeMessage);

    }
}
