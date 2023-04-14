package org.example;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
public class Client2 {

    BufferedReader bufferedReader;
    PrintWriter printWriter;
    String userName;
    String phoneNumber;
    public Client2() throws IOException {
        JFrame chatWindow = new JFrame("Jhay Chatty");
        JTextArea chatTextArea = new JTextArea(27, 30);
        JTextField chatTextField = new JTextField(30);
        JLabel chatBlankLabel = new JLabel("                  ");
        JButton sendButton = new JButton("Send");
        JPanel chatPanel = new JPanel();
        chatPanel.add(sendButton);
        chatWindow.setLayout(new FlowLayout());
        chatWindow.add(new JScrollPane(chatTextArea));
        chatWindow.add(chatBlankLabel);
        chatWindow.add(chatTextField);
        chatWindow.add(chatPanel);
        chatWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        chatWindow.setSize(420, 600);
        chatWindow.setVisible(true);
        chatWindow.setResizable(false);
        chatWindow.setBackground(new Color(100, 150, 170));
        chatPanel.setBackground(new Color(50,50,50));
        chatTextArea.setEditable(false);
        chatTextField.setEditable(false);
        chatTextArea.setBackground(new Color(140,150,170));
        chatTextArea.setForeground(Color.BLACK);
        Socket socket = new Socket("192.168.88.196",9091);
        bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        printWriter = new PrintWriter(socket.getOutputStream());

        userName = JOptionPane.showInputDialog(chatWindow,
                "Enter Your Username", "Login | Register",
                JOptionPane.PLAIN_MESSAGE);
        phoneNumber = JOptionPane.showInputDialog(chatWindow,
                "Enter Your PhoneNumber", "Login | Register",
                JOptionPane.PLAIN_MESSAGE);
        printWriter.write(userName + ";" + phoneNumber + "\n");
        printWriter.flush();
        String welcomeMessage = bufferedReader.readLine();
        if(welcomeMessage.equals("You are a new User")){
            JOptionPane.showMessageDialog(chatWindow,
                    welcomeMessage+"\nConfirm Your Registration",
                    "Register | Jhay Chatty",JOptionPane.INFORMATION_MESSAGE);
            String newWelcomeMessage = bufferedReader.readLine();
            JOptionPane.showMessageDialog(chatWindow, newWelcomeMessage,
                    "Jhay Chatty", JOptionPane.PLAIN_MESSAGE);
        }else{
            JOptionPane.showMessageDialog(chatWindow, welcomeMessage,
                    "Jhay Chatty", JOptionPane.PLAIN_MESSAGE);
        }        chatTextField.setEditable(true);
        sendButton.addActionListener(e -> {
            String message = chatTextField.getText();
            if (!message.isEmpty()) {
                printWriter.write(userName+": "+message + "\n");
                printWriter.flush();
                chatTextField.setText("");
            }
        });
        // Start a thread to listen for incoming messages from the server
        Thread listenerThread = new Thread(() -> {
         try {
             String message;
             while ((message = bufferedReader.readLine()) != null) {
                 chatTextArea.append(message + "\n");
             }
         } catch (IOException e) {
             throw new RuntimeException(e);
         }
        });
        listenerThread.start();
    }
    public static void main(String[] args) throws IOException{
        Client2 client = new Client2();
    }
}