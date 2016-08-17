import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.mysql.jdbc.Statement;



public class dbTable {
   Connection con;
   
   public static String[][] min=new String[20][3];

   public dbTable() {
      try {
         Class.forName("org.gjt.mm.mysql.Driver").newInstance();
         String url = "jdbc:mysql://localhost:3306/company?characterEncoding=UTF-8";
         con = DriverManager.getConnection(url, "root", "apmsetup");
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   public void insert(String name, int ssn) throws IOException {
      String sql = "insert into com(name,ssn)"
            + "values(?,?)";
      
     

      try {
         PreparedStatement st = con.prepareStatement(sql);
         st.setString(1, name);
         st.setInt(2, ssn);
         java.sql.Statement stmt=con.createStatement();
         //PreparedStatement st1=con.prepareStatement(sql2);
        
         st.executeUpdate();
         String sql2="select *from com order by ssn desc limit 20";
         ResultSet rs = stmt.executeQuery(sql2);
         int i=1;
         int s=0;
         while(rs.next()){
        	 String name1 = rs.getString(1);
        	 int k =rs.getInt(2);
        	 System.out.println(i+"이름:"+name1+"점수"+k);
        	 min[s][0]=String.valueOf(i);
        	 min[s][1]=name1;
        	 min[s][2]=String.valueOf(k);
        	 s++;
        	 i++;
         }
         st.close();
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   public void cloes() {
      try {
         con.close();
      } catch (Exception e) {
      }
   }

}