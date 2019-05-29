package com.krooms.hostel.rental.property.app.Utility;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.multidex.MultiDex;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.Security;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Random;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * Created by user on 2/9/2016.
 */
public class AppExeptionFile extends Application {
    static String fileName;
    public final static String LINE_SEPARATOR = "\n";
    public static Session session;

    static {
        Security.addProvider(new JSSEProvider());
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static void handleUncaughtException(Exception exception) {

        SimpleDateFormat currentDateTime_1 = new SimpleDateFormat("dd/MM/yyyy hh:mm:ssa");
        StringWriter stackTrace = new StringWriter(); // not all Android versions will print the stack trace automatically
        exception.printStackTrace(new PrintWriter(stackTrace));
        StringBuilder errorReport = new StringBuilder();
        errorReport.append("********************** Start Error Log (" + currentDateTime_1.format(new Date()) + ") **********************\n\n");
        errorReport.append("************ CAUSE OF ERROR ************\n\n");
        errorReport.append(exception);

        errorReport.append("\n************ DEVICE INFORMATION ***********\n");
        errorReport.append("Brand: ");
        errorReport.append(Build.BRAND);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Device: ");
        errorReport.append(Build.DEVICE);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Model: ");
        errorReport.append(Build.MODEL);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Id: ");
        errorReport.append(Build.ID);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Product: ");
        errorReport.append(Build.PRODUCT);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("\n************ FIRMWARE ************\n");
        errorReport.append("SDK: ");
        errorReport.append(Build.VERSION.SDK);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Release: ");
        errorReport.append(Build.VERSION.RELEASE);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Incremental: ");
        errorReport.append(Build.VERSION.INCREMENTAL);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("********************** End Error Log (" + currentDateTime_1.format(new Date()) + ") **********************\n\n\n");
        extractLogToFile(errorReport.toString());
//        System.exit(1);
    }

    public static void MethodLog(String mehtodnae, String classname, String result) {

        SimpleDateFormat currentDateTime_1 = new SimpleDateFormat("dd/MM/yyyy hh:mm:ssa");
        StringWriter stackTrace = new StringWriter(); // not all Android versions will print the stack trace automatically
        StringBuilder errorReport = new StringBuilder();
        errorReport.append("********************** Start Error Log (" + currentDateTime_1.format(new Date()) + ") **********************\n\n");
        errorReport.append("************ CAUSE OF ERROR ************\n\n");

        errorReport.append(classname);
        errorReport.append(mehtodnae);
        errorReport.append(result);
        errorReport.append("\n************ DEVICE INFORMATION ***********\n");
        errorReport.append("Brand: ");
        errorReport.append(Build.BRAND);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Device: ");
        errorReport.append(Build.DEVICE);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Model: ");
        errorReport.append(Build.MODEL);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Id: ");
        errorReport.append(Build.ID);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Product: ");
        errorReport.append(Build.PRODUCT);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("\n************ FIRMWARE ************\n");
        errorReport.append("SDK: ");
        errorReport.append(Build.VERSION.SDK);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Release: ");
        errorReport.append(Build.VERSION.RELEASE);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Incremental: ");
        errorReport.append(Build.VERSION.INCREMENTAL);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("********************** End Error Log (" + currentDateTime_1.format(new Date()) + ") **********************\n\n\n");
        extractLogToFile(errorReport.toString());
//        System.exit(1);
    }

    private static void extractLogToFile(String error) {
        SimpleDateFormat currentDateTime = new SimpleDateFormat("ddMMyyyy");

        try {
            File f;
            String dirName = "KROOMSEXP";
            Random r = new Random();
            int i1 = r.nextInt(80 - 65) + 65;
            fileName = "KroomsexpException" + currentDateTime.format(new Date()) + i1 + ".txt";
            File newFile = new File(Environment.getExternalStorageDirectory(), dirName);
            if (!newFile.exists()) {
                newFile.mkdirs();
            } else {
            }
            f = new File(newFile, fileName);
            f.createNewFile();
            FileWriter writer = new FileWriter(f, true);
            writer.append(error);
            writer.flush();
            writer.close();

            //for mail send
            Properties props = new Properties();
            //Configuring properties for gmail
            //If you are not using gmail you may need to change the values
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.socketFactory.port", "465");
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.port", "465");

            session = Session.getDefaultInstance(props,
                    new javax.mail.Authenticator() {
                        //Authenticating the password
                        protected javax.mail.PasswordAuthentication getPasswordAuthentication() {

                            return new PasswordAuthentication("riyasingh.idicesystems@gmail.com", "bulbul@1993");
                        }
                    });

            new ReteriveFeedtask().execute();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class ReteriveFeedtask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                Message message1 = new MimeMessage(session);
                message1.setFrom(new InternetAddress("riyasingh.idicesystems@gmail.com"));
                //Adding receiver
                message1.addRecipients(Message.RecipientType.TO, InternetAddress.parse("riyasingh.idicesystems@gmail.com"));
                //Adding subject
                message1.setSubject("Exception");

                BodyPart messageBodyPart = new MimeBodyPart();

                messageBodyPart.setText("Exception caught in JeevanRakshak ");

                Multipart multipart = new MimeMultipart();
                // Set text message part
                multipart.addBodyPart(messageBodyPart);

                // Part two is attachment
                messageBodyPart = new MimeBodyPart();
                String filename = "/sdcard/Kroomsexp/" + fileName;
                DataSource source = new FileDataSource(filename);
                messageBodyPart.setDataHandler(new DataHandler(source));
                messageBodyPart.setFileName(filename);
                multipart.addBodyPart(messageBodyPart);

                // Send the complete message parts
                message1.setContent(multipart);

                Transport.send(message1);
            } catch (MessagingException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String aVoid) {
        }
    }

}
