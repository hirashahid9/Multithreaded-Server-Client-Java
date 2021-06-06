
/*
Name: Hira Shahid
Roll Number: 17L-4090
AP-8A
*/

package multithreadedserver;

import java.net.*;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/*  
    This is a classs which implements threads
    On each request, an object of ClientResponse 
    class is made by MultithreadedServer to 
    fulfill client request
*/

class ClientResponse implements Runnable {
    static PreparedStatement pSt;
    static PreparedStatement pStInsert;
    static PreparedStatement pStDelete;
    static PreparedStatement pStUpdateName;
    static PreparedStatement pStUpdatePass;
    Socket csocket;
    Connection con=null;
    static String encryptDecrypt(String inputString)
    {
        String outputString = "";
        int len = inputString.length();
  
        for (int i = 0; i < len; i++) 
        {
            outputString = outputString + Character.toString((char) (inputString.charAt(i) ^ 'A'));
        }
  
       return outputString;
    }
    
    public static String login(String uname,String pass) throws SQLException
    {
        ResultSet rs = pSt.executeQuery();
        boolean login=false;
        while(rs.next())
        {
            if(uname.equalsIgnoreCase(rs.getString("username")) && pass.equals(encryptDecrypt(rs.getString("password"))))
            {
               return "You have been sucessfully logged in.";
            }
        }
        
        return "Either username or password is wrong.";
    }
    
    public static String register(String uname, String pass) throws SQLException
    {
        System.out.println(uname+" "+pass);
        ResultSet rs=pSt.executeQuery();
        while(rs.next())
        {
            if(uname.equalsIgnoreCase(rs.getString("username")))
            {
               return "Please select a unique username."; 
            }
        }
        pStInsert.setString(1, uname);
        pStInsert.setString(2, encryptDecrypt(pass));
        pStInsert.execute();
        return "You have been successfully registered.";
    }
    
    public static boolean checkDetails(String uname,String pass) throws SQLException
    {
        ResultSet rs=pSt.executeQuery();
        while(rs.next())
        {
            if(uname.equalsIgnoreCase(rs.getString("username")) && pass.equals(encryptDecrypt(rs.getString("password"))))
            {
               return true;
            }
            
        }
        return false;
    }
    
    public static String changeName(String uname,String pass, String newname) throws SQLException
    {
        ResultSet rs=pSt.executeQuery();
        while(rs.next())
        {
            if(newname.equalsIgnoreCase(rs.getString("username")))
            {
                return "Username already taken";
            }
        }
        
        pStUpdateName.setString(2, uname);
        pStUpdateName.setString(1, newname);
        pStUpdateName.execute();
        return "Your name has been updated.";
        
    }
    
    public static String changePass(String uname,String pass, String newpass) throws SQLException
    {
        pStUpdatePass.setString(2, uname);
        pStUpdatePass.setString(1, encryptDecrypt(newpass));
        pStUpdatePass.execute();
        return "Your password has been updated.";
    }

    
    // Constructor
    ClientResponse(Socket csocket) throws SQLException
    {
        this.csocket=csocket;
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MultiThreadedServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        String url="jdbc:ucanaccess://C:\\Users\\hiras\\Documents\\Database21.accdb";
        
        con = DriverManager.getConnection(url);
        
        pSt=con.prepareStatement("Select * from users",ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
        pStInsert=con.prepareStatement("INSERT INTO users(username,password) values (?,?)");
        pStUpdateName=con.prepareStatement("UPDATE users SET username=?"+"WHERE username=?",ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
        pStUpdatePass=con.prepareStatement("UPDATE users SET password=?"+"WHERE username=?",ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
        
        ResultSet rs=pSt.executeQuery();
       
    }
  
    public void run() {
   
        InputStream is=null;
        try {
            is = csocket.getInputStream();
            InputStreamReader isr=new InputStreamReader(is);
            BufferedReader br=new BufferedReader(isr);
            OutputStream out=csocket.getOutputStream();
            PrintWriter pw=new PrintWriter(out,true);
            String name=br.readLine();
            String pass=br.readLine();
            String func=br.readLine();
            if(func.equals("Log in"))
            {
                String result=null;
                try {
                    result = login(name, pass);
                } catch (SQLException ex) {
                    Logger.getLogger(MultiThreadedServer.class.getName()).log(Level.SEVERE, null, ex);
                }
                pw.println(result);
            }
            else if (func.equals("Register"))
            {
                String result=register(name, pass);
                pw.println(result);
            }
            else if(func.equals("Change Username"))
            {
                String query;
                if(checkDetails(name,pass))
                {
                    query="Enter the new username.";
                    pw.println(query);
                    String newname=br.readLine();
                    if(newname!=null && !newname.equalsIgnoreCase("null") && newname.length()<20)
                    {
                        if(newname.equals(name))
                        {
                            pw.println("You already have this as your current username.");
                        }
                        else
                        {
                            pw.println(changeName(name, pass, newname));
                        }
                    }
                }
                else
                {
                    query="Either username or password is incorrect.";
                    pw.println(query);
                }
            }
            else if(func.equals("Change Password"))
            {
                String query;
                if(checkDetails(name,pass))
                {
                    query="Enter the new password.";
                    pw.println(query);
                    String newpass=br.readLine();
                    System.out.println("Password: "+newpass);
                    if(newpass!=null && !newpass.equalsIgnoreCase("null") && newpass.length()>=4 && newpass.length()<=10){
                        System.out.print("here");
                        if(newpass.equals(pass))
                        {
                            pw.println("You already have this as your current password.");
                        }
                        else
                        {
                            try {
                                pw.println(changePass(name, pass, newpass));
                            } catch (SQLException ex) {
                                Logger.getLogger(MultiThreadedServer.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                }
                else
                {
                    query="Either username or password is incorrect.";
                    pw.println(query);
                }
            }       csocket.close();
        } catch (IOException ex) {
            Logger.getLogger(MultiThreadedServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(MultiThreadedServer.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                csocket.close();
                System.out.println("Socket closed.");
                is.close();
                con.close();
            } catch (Exception ex) {
                System.out.println(ex);
            }
        }

            /*PrintWriter out = null;
            BufferedReader in = null;
            try {
                    
                  // get the outputstream of client
                out = new PrintWriter(
                    csocket.getOutputStream(), true);
  
                  // get the inputstream of client
                in = new BufferedReader(
                    new InputStreamReader(
                        csocket.getInputStream()));
  
                String line;
                while ((line = in.readLine()) != null) {
  
                    // writing the received message from
                    // client
                    System.out.printf(
                        " Sent from the client: %s\n",
                        line);
                    out.println(line);
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                    if (in != null) {
                        in.close();
                        csocket.close();
                    }
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }*/
    }
   }