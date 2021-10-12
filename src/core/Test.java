package core;
import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class Test extends JFrame{ 
    private int width = 200;
    private int height = 250;
 
    // create array string color to show in list
    String[] colorName = { "blue", "green", "red" };
    // create array color
    Color[] backGroundColor = { Color.blue, Color.green, Color.red };
    JList<String> myJList;
 
    // create JList width array color
    public Test() {
        // set layout for contenPane
        getContentPane().setLayout(new FlowLayout());
 
        // add list color
        add(createJList());
 
        // set display
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(width, height);
        setLocationRelativeTo(null);
        setVisible(true);
    }
 
    private JList createJList() {
        // create list
        myJList = new JList<String>(colorName);
        // add listen to get action when select item in JList
        myJList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                changeBackGround();
            }
        });
        return myJList;
    }
 
    // change background JFrame
    private void changeBackGround() {
        getContentPane().setBackground(
                backGroundColor[myJList.getSelectedIndex()]);
    }
 
    public static void main(String[] args) {
        new Test();
    }
} 