//package com.nmdp.cridservice.controller;
//
//import java.sql.*;
//public class Conection
//{
//    public static void main(String a[]) throws ClassNotFoundException, SQLException
//    {
//        try
//        {
//            String url = "jdbc:sqlserver://dcitsqlst2:61237;databaseName=FormsNet3_DVL2;integratedSecurity=true";
//            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
//            Connection conn = DriverManager.getConnection(url);
//            System.out.println("connection created");
//            Statement st=conn.createStatement();
//            String sql="select * from dbo.t_unique_patient";
//            ResultSet rs=st.executeQuery(sql);
//            while(rs.next())
//            {
//                System.out.println("Name: ");
//                //System.out.println("Address : "+rs.getString(2));
//            }
//            if(st!=null)
//                st.close();
//            if(conn!=null)
//                conn.close();
//        }
//        catch(SQLException sqle)
//        {
//            System.out.println("Sql exception "+sqle);
//        }
//    }
//}
