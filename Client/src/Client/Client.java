/*
Name: Hira Shahid
Roll Number: 17L-4090
AP-8A
*/

package Client;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

/*  This is the client side for login and registration related system.
    It contains the GUI as well and connects with the server to fulfill its request
*/

public class Client {
    JFrame f;
    
    public void init()
    {
        f=new JFrame("Homework");
        
        Container con=f.getContentPane();
        con.setLayout(new BorderLayout());
        JPanel jp = new JPanel(new GridBagLayout());
        jp.setBackground(Color.BLUE);
        // JTextField tf = new JTextField(15);
       // JLabel l=new JLabel("Select your desired option.");
          
        JTextField jt=new JTextField();
        JButton b = new JButton("Log in");
        JButton b1 = new JButton("Register");
        JButton b2 = new JButton("Change username");
        JButton b3 = new JButton("Change password");
       
        //jp.setBackground(Color.BLACK);
        GridBagLayout layout = new GridBagLayout();
        jp.setLayout(layout);        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill=GridBagConstraints.HORIZONTAL;
        gbc.insets=new Insets(20,0,20,0);
        gbc.gridx=0;
        gbc.gridy=0;
        //jp.add(l,gbc);
        
        JPanel jp1=new JPanel();
        jp1.setLayout(new FlowLayout());
        gbc.gridy=gbc.gridy+1;
        JLabel jl=new JLabel("Username: ");
        jp1.add(jl);
        JTextArea tf=new JTextArea(1,10);
        tf.setEditable(true);
        jp1.add(tf);
        jp1.add(new JLabel("        "));
        JLabel j2=new JLabel("Password: ");
        jp1.add(j2);
        JTextArea tf1=new JTextArea(1,10);
        tf1.setEditable(true);
        jp1.add(tf1);
        jp1.setBackground(Color.BLUE);
        jp.add(jp1,gbc);
        
        
        gbc.gridy++;
        gbc.gridy=gbc.gridy+3;
        gbc.insets=new Insets(10,0,0,0);
        jp.add(b,gbc);
        
        
        gbc.gridy=gbc.gridy+3;
        jp.add(b1,gbc);
        
        gbc.gridy=gbc.gridy+3;
        jp.add(b2,gbc);
        
        gbc.gridy=gbc.gridy+3;
        jp.add(b3,gbc);
        
        con.add(BorderLayout.CENTER, jp);
        
       b.addActionListener(new ActionListener(){
        public void actionPerformed(ActionEvent e)
        {
            String name=tf.getText();
            String pass=tf1.getText();
        
        try {
            
            if(!name.equals("") && !pass.equals("")){
           
            Socket s=new Socket("localhost",2222);
            InputStream is=s.getInputStream();
            InputStreamReader isr=new InputStreamReader(is);
            BufferedReader br=new BufferedReader(isr);

            OutputStream out=s.getOutputStream();
            PrintWriter pw=new PrintWriter(out,true);

            pw.println(name);
            pw.println(pass);
            pw.println("Log in");
            String r=br.readLine();
            JOptionPane.showMessageDialog(null, r);
            System.out.println(r);
            s.close();
            }
            else if(name.equals(""))
            {
                 JOptionPane.showMessageDialog(null, "Please enter username");
            }
            else if (pass==null || pass.equals(""))
            {
                JOptionPane.showMessageDialog(null, "Please enter password");
            }
            }
            catch(IOException | HeadlessException e1)
            {System.out.println(e);}
        }
        });
       
       b1.addActionListener(new ActionListener(){
        public void actionPerformed(ActionEvent e)
        {
            String name=tf.getText();
            String pass=tf1.getText();
        
         try {
        
        if(name!=null && pass!=null && !name.equals("") && !pass.equals("") && usernameConstraintFulfilled(name) && passwordConstraintFulfilled(pass))
            {Socket s=new Socket("localhost",2222);
            InputStream is=s.getInputStream();
            InputStreamReader isr=new InputStreamReader(is);
            BufferedReader br=new BufferedReader(isr);

            OutputStream out=s.getOutputStream();
            PrintWriter pw=new PrintWriter(out,true);

            pw.println(name);
            pw.println(pass);
            pw.println("Register");
            String r=br.readLine();
            JOptionPane.showMessageDialog(null, r);
            System.out.println(r);
            s.close();	
            }
        else
        {
            if(name==null || name.equals(""))
            {
                JOptionPane.showMessageDialog(null, "Please enter username");
            }
            else if (pass==null || pass.equals(""))
            {
                JOptionPane.showMessageDialog(null, "Please enter password");
            }
            else if(!usernameConstraintFulfilled(name))
            {
                JOptionPane.showMessageDialog(null, "Username should not be greater than 20 characters");
            }
            else if (!passwordConstraintFulfilled(pass))
            {
                JOptionPane.showMessageDialog(null, "Password should be between 4 to 10 characters.");
            }
        }
         }catch(IOException | HeadlessException e1)
            {System.out.println(e);}
            }
        });
       
       b2.addActionListener(new ActionListener(){
        public void actionPerformed(ActionEvent e)
        {
         
        try {
           if(!tf.getText().equals("") && !tf1.getText().equals("")){
            Socket s=new Socket("localhost",2222);
            InputStream is=s.getInputStream();
            InputStreamReader isr=new InputStreamReader(is);
            BufferedReader br=new BufferedReader(isr);

            OutputStream out=s.getOutputStream();
            PrintWriter pw=new PrintWriter(out,true);

            String msg=tf.getText();
            pw.println(msg);
            String msg1=tf1.getText();
            pw.println(msg1);
            pw.println("Change Username");
            String r=br.readLine();
            if(!r.contains("incorrect"))
            {
                System.out.print(r);
                String newname=JOptionPane.showInputDialog(r);
                pw.println(newname);
                if(newname!=null && newname.length()<20){
                String res=br.readLine();
                JOptionPane.showMessageDialog(null, res);
                System.out.println(r);
                }
                else if(newname.length()>20)
                {
                JOptionPane.showMessageDialog(null, "username should not be greater than 20 characters");
                    
                }
            }
            else
            {
                JOptionPane.showMessageDialog(null, r);
            }


            s.close();	
           }
           else if(tf.getText().equals(""))
            {
               JOptionPane.showMessageDialog(null, "Please enter username");
            }
            else if (tf1.getText().equals(""))
            {
                JOptionPane.showMessageDialog(null, "Please enter password");
            }
               
        }catch(IOException | HeadlessException e1)
        {System.out.println(e);}

        }
        });
       
       b3.addActionListener(new ActionListener(){
        public void actionPerformed(ActionEvent e)
        {
            String name=tf.getText();
            String pass=tf.getText();
            
         try {
           if(!name.equals("") && !pass.equals("")){
                Socket s=new Socket("localhost",2222);
                InputStream is=s.getInputStream();
                InputStreamReader isr=new InputStreamReader(is);
                BufferedReader br=new BufferedReader(isr);

                OutputStream out=s.getOutputStream();
                PrintWriter pw=new PrintWriter(out,true);

                String msg=tf.getText();
                pw.println(msg);
                String msg1=tf1.getText();
                pw.println(msg1);
                pw.println("Change Password");
                String r=br.readLine();
                if(!r.contains("incorrect"))
                {
                    System.out.print(r);
                    String newpass=JOptionPane.showInputDialog(r);
                    if(newpass!=null &&  newpass.length()>=4 && newpass.length()<=10)
                    {
                        pw.println(newpass);
                        String res=br.readLine();
                        JOptionPane.showMessageDialog(null, res);
                        System.out.println(r);
                    }
                    else
                    {
                        pw.println(newpass);
                        if(newpass!=null){
                        JOptionPane.showMessageDialog(null, "Password should be between 4 to 10 characters.");
                        }
                    }

                }
                else
                {
                    JOptionPane.showMessageDialog(null, r);
                }


                s.close();
           }
           else if(name.equals(""))
           {
               JOptionPane.showMessageDialog(null, "Please enter username");
           }
           else if(pass.equals(""))
           {
               JOptionPane.showMessageDialog(null, "Please enter password");
           }
                
        }catch(IOException | HeadlessException e1)
        {System.out.println(e);}

        }
        });
    
    
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(800,500);
        f.setVisible(true);
    }
    
    public boolean passwordConstraintFulfilled(String pass)
    {
        if(pass.length()>=4 && pass.length()<=10)
            return true;
        return false;
    }
    
    
    public boolean usernameConstraintFulfilled(String name)
    {
        if(name.length()>20)
            return false;
        return true;
    }
    
    Client()
    {
        init();
    }
    public static void main(String[] args) throws IOException {
        Client obj=new Client();
        }
    
}
