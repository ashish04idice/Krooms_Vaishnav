package com.krooms.hostel.rental.property.app;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;

import com.krooms.hostel.rental.property.app.Utility.JSSEProvider;

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
public class AppError extends Application {
    static String fileName;
    public final static String LINE_SEPARATOR = "\n";
    public static Session session;
    static String urlmain = "", urlmainmethod = "", version = "";

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
    }

    public static void handleUncaughtException(Exception exception, String classname, String propertyid, String versionCode) {

        SimpleDateFormat currentDateTime_1 = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
        StringWriter stackTrace = new StringWriter(); // not all Android versions will print the stack trace automatically
        exception.printStackTrace(new PrintWriter(stackTrace));
        StringBuilder errorReport = new StringBuilder();
        errorReport.append("********************** Start Error Log (" + currentDateTime_1.format(new Date()) + ") **********************\n\n");
        errorReport.append("************ CAUSE OF ERROR ************\n\n");
        errorReport.append(exception);
        errorReport.append("\n************ Class name ***********\n");
        errorReport.append("Class name: ");
        errorReport.append(classname);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Property Id: ");
        errorReport.append(propertyid);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("VersionCode: ");
        errorReport.append(versionCode);
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
        extractLogToFile(errorReport.toString(), propertyid);
    }

    public static void MethodLog(String methodname, String classname, String result, String propertyid, String versionCode) {

        SimpleDateFormat currentDateTime_1 = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
        StringWriter stackTrace = new StringWriter(); // not all Android versions will print the stack trace automatically
        StringBuilder errorReport = new StringBuilder();
        errorReport.append("-------Start Error Log (" + currentDateTime_1.format(new Date()) + ")-------\n\n");
        // errorReport.append("************ CAUSE OF ERROR ************\n\n");
        errorReport.append("------- CLASS DETAILS -------\n\n");
        errorReport.append("Class-:  " + classname + " ");
        errorReport.append("\n \n");
        errorReport.append("Property Id: ");
        errorReport.append(propertyid);
        errorReport.append("\n \n");
        errorReport.append("VersionCode: ");
        errorReport.append(versionCode);
        errorReport.append("\n \n");
        errorReport.append("Method-:   " + methodname + " ");
        errorReport.append("\n  \n");
        errorReport.append("Result-: " + result + " ");
        errorReport.append(" \n ");
        errorReport.append("\n------- DEVICE INFORMATION -------\n");
        errorReport.append(" \n ");
        errorReport.append("Brand: ");
        errorReport.append(Build.BRAND);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Device: ");
        errorReport.append(Build.DEVICE);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Model: ");
        errorReport.append(Build.MODEL);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("\n ");
        errorReport.append("------- End Error Log (" + currentDateTime_1.format(new Date()) + ")------\n\n\n");
        extractMethodLogToFile(errorReport.toString(), propertyid);
    }

    private static void extractMethodLogToFile(String error, String propertyid) {
        SimpleDateFormat currentDateTime = new SimpleDateFormat("ddMMyyyy");

        try {
            File f;
            String dirName = "_Krooms_Exception";
            String fileName = "Krooms_Exception_Report" + currentDateTime.format(new Date()) + ".txt";
            File newFile = new File(Environment.getExternalStorageDirectory(), dirName);
            if (!newFile.exists()) {
                newFile.mkdirs();
            } else {
            }
            f = new File(newFile, fileName);
            if (!f.exists()) {
                f.createNewFile();
            }
            FileWriter writer = new FileWriter(f, true);
            writer.append(error);
            writer.flush();
            writer.close();

            urlmainmethod = f.getAbsolutePath();
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
                        protected PasswordAuthentication getPasswordAuthentication() {

                            return new PasswordAuthentication("kroomexception@gmail.com", "krooms@123");
                        }
                    });

            new ReteriveFeedtaskForMenthod(propertyid).execute();

            //
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void extractLogToFile(String error, String propertyid) {
        SimpleDateFormat currentDateTime = new SimpleDateFormat("ddMMyyyy");

        try {
            File f;
            String dirName = "_Krooms_Exception";
            Random r = new Random();
            int i1 = r.nextInt(80 - 65) + 65;
            fileName = "kroomsException" + currentDateTime.format(new Date()) + i1 + ".txt";
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
            urlmain = f.getAbsolutePath();
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
                        protected PasswordAuthentication getPasswordAuthentication() {

                            return new PasswordAuthentication("kroomexception@gmail.com", "krooms@123");
                        }
                    });

            new ReteriveFeedtask(propertyid).execute();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class ReteriveFeedtask extends AsyncTask<String, Void, String> {

        String propertyid = "";


        public ReteriveFeedtask(String propertyid) {
            this.propertyid = propertyid;
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                Message message1 = new MimeMessage(session);
                message1.setFrom(new InternetAddress("kroomexception@gmail.com"));
                //Adding receiver
                message1.addRecipients(Message.RecipientType.TO, InternetAddress.parse("kroomexception@gmail.com"));
                //Adding subject
                message1.setSubject("Exception");

                BodyPart messageBodyPart = new MimeBodyPart();

                messageBodyPart.setText("Exception caught in Krooms " + propertyid);

                Multipart multipart = new MimeMultipart();
                // Set text message part
                multipart.addBodyPart(messageBodyPart);

                // Part two is attachment
                messageBodyPart = new MimeBodyPart();
                String filename = urlmain;
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


    static class ReteriveFeedtaskForMenthod extends AsyncTask<String, Void, String> {

        String propertyid = "";

        public ReteriveFeedtaskForMenthod(String propertyid) {
            this.propertyid = propertyid;
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                Message message1 = new MimeMessage(session);
                message1.setFrom(new InternetAddress("kroomexception@gmail.com"));
                //Adding receiver
                message1.addRecipients(Message.RecipientType.TO, InternetAddress.parse("kroomexception@gmail.com"));
                //Adding subject
                message1.setSubject("Exception");

                BodyPart messageBodyPart = new MimeBodyPart();

                messageBodyPart.setText("Exception caught method " + propertyid);

                Multipart multipart = new MimeMultipart();
                // Set text message part
                multipart.addBodyPart(messageBodyPart);

                // Part two is attachment
                messageBodyPart = new MimeBodyPart();
                String filename = urlmainmethod;
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
