package utils;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.*;
import java.util.Properties;

public class EmailClient {


    public static void sendEmailWithReport(String htmlBody, String reportPath,
                                           String username, String password) throws IOException, FileNotFoundException {


        final Properties props = new Properties();
        props.load(new FileInputStream(new File("src/test/resources/smtp.properties")));

        // Email config
        String to = username; // Using passed username
        String from = username;
        String host = props.getProperty("host");

        // Get system properties
        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", host);
        properties.setProperty("mail.smtp.port", props.getProperty("port"));
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.starttls.enable", "true");

        // Authenticate using passed credentials
        Session session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });


        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject("Test Report");

            Multipart multipart = new MimeMultipart();

            // HTML body
            MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent(htmlBody, "text/html; charset=utf-8");
            multipart.addBodyPart(htmlPart);

            // Attach Extent Report
            MimeBodyPart attachmentPart = new MimeBodyPart();
            File reportFile = new File(reportPath);
            if (reportFile.exists()) {
                DataSource source = new FileDataSource(reportFile);
                attachmentPart.setDataHandler(new DataHandler(source));
                attachmentPart.setFileName(reportFile.getName());
                multipart.addBodyPart(attachmentPart);
            } else {
                System.out.println("Report file not found: " + reportPath);
            }

            // Final message
            message.setContent(multipart);

            Transport.send(message);
            System.out.println("Email sent successfully.");

        } catch (MessagingException mex) {
            dumpHtmlToFile(htmlBody, "test-output/failed_email_dump.html");
            mex.printStackTrace();
        }
    }

    private static void dumpHtmlToFile(String html, String filePath) {
       File file = new File(filePath);
       if (!file.exists()) {
           try {
               file.createNewFile();
           } catch (IOException e) {
               e.printStackTrace();
           }
       }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(html);
            System.out.println("✅ Email body saved to: " + filePath);
        } catch (IOException ex) {
            System.err.println("❌ Failed to write HTML body to file.");
            ex.printStackTrace();
        }
    }
}
