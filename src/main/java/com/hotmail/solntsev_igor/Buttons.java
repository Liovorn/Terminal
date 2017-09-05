package com.hotmail.solntsev_igor;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by solncevigor on 3/26/17.
 */
public class Buttons {

    private static SerialPort serialPort = new SerialPort("COM1") ;

    public static void frame(){
        ButtonFrame frame= new ButtonFrame();//frame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//end of the program
        frame.setVisible(true);

    }
    static class ButtonFrame extends JFrame{
        public ButtonFrame(){
            setSize(DEFAULT_WIDTH,DEFAULT_HEIGHT);//frame size
            setTitle("Scale");
            ButtonPanel panel=new ButtonPanel();
            add(panel);
            add(panel);
            add(panel);
        }
        public static final  int DEFAULT_WIDTH=700;
        public static final  int DEFAULT_HEIGHT=250;
    }
    static class ButtonPanel extends JPanel{// Frame
        JTextField textField=new JTextField(20);
        JTextArea textArea=new JTextArea(10,50);
        public ButtonPanel(){ //constructor of panel add(textField);

            JButton RunButton = new JButton("Пуск"); //create buttons

            add(RunButton); //adding button on a panel

            ButtonActionRun RunAction =new ButtonActionRun();

            RunButton.addActionListener(RunAction); //set action for buttons


            add(textArea);
            textArea.setLineWrap(true);

        }

        private class ButtonActionRun implements ActionListener,Runnable {
            public DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            private SerialPort serialPort = new SerialPort("COM1");
            private Date date = new Date();
            private  String s = dateFormat.format(date);
            public String data;

            @Override
            public void run() {
                try {
                    serialPort.openPort();
                    serialPort.setParams(
                            SerialPort.BAUDRATE_9600,
                            SerialPort.DATABITS_8,
                            SerialPort.STOPBITS_1,
                            SerialPort.PARITY_NONE);
                    serialPort.setEventsMask(serialPort.MASK_RXCHAR);
                    serialPort.addEventListener(new EventListener());

                    data = String.valueOf(serialPort.setEventsMask(serialPort.MASK_RXCHAR));

                } catch (SerialPortException ex) {
                    System.out.println(ex + "rrr");
                }
            }
            @Override
            public void actionPerformed(ActionEvent event){ //Button reaction

                textField.setText(data+ s);
                textArea.append(data + s + "\n");

            }
        }

        private static class EventListener implements SerialPortEventListener {

            public void serialEvent(SerialPortEvent event) {
                if(event.isRXCHAR() && event.getEventValue() == 8){
                    try {
                        byte[] buffer = serialPort.readBytes(8);
                        serialPort.closePort();
                    }
                    catch (SerialPortException ex) {
                        System.out.println(ex);
                    }
                }
            }
        }
    }
}
