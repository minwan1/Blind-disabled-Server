import java.io.BufferedReader;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class GUIServerThread extends Thread {

   private Socket socket;
   private BufferedReader br = null;
   private BufferedWriter bw = null;

   String message = "";
   String strId = "";

   GUIServerConn serverConn = null; // Client 접속 및 메시지 전송 관련 객체

   public GUIServerThread(Socket tmpSocket, GUIServerConn tmpServerConn) {
      socket = tmpSocket;
      serverConn = tmpServerConn;

      try {
         br = new BufferedReader(new InputStreamReader(
               socket.getInputStream(), "UTF-8"));
         bw = new BufferedWriter(new OutputStreamWriter(
               socket.getOutputStream(), "UTF-8"));

         bw.write("CONN [Server 연결성공]" + "\n");
         bw.flush();
         
         

      } catch (IOException e) {
         e.getStackTrace();
      }

   }

   public void run() {

      login(); // Client ID 정보 받기

      if (!strId.equals("")) {
         serverConn.broadCastingClientIdList(); // 접속한 모든 Client ID 전송
         
         

         try {
            while (true) {
               message = br.readLine();
               String[] tmsMsg=message.split("\\|");
               System.out.println("받은 점수 ==>" + strId + ":" + message);
               
               String min=getOnlyNumberString(message);

               
               //////////////

          
               
               //////////////////
               dbTable db=new dbTable();
               db.insert(strId, Integer.parseInt(min));
              
               

               String[] tmpMsg = message.split("\\|"); // | 기호 제외
               switch (tmpMsg[0]) {
               case "CHATTING":
            	   serverConn.broadCasting("CHATTING|[Rank]          [Name]          [Score]"
                           );
            	   
            	   for(int z=0;z<20;z++)
            	   {
            			  serverConn.broadCasting("CHATTING|    [" + dbTable.min[z][0] + "]             "
                                  +dbTable.min[z][1]+"                    "+dbTable.min[z][2]);
            	   }
                  

                  break;
               default:
                  break;
               }

               if (tmpMsg[0].equals("EXIT")) {
                  serverConn.broadCasting("CLIENTCLOSE|*****" + strId
                        + "ㅁ*****");
                  serverConn.broadCasting("EXIT|" + tmpMsg[1]); // 종료한
                                                      // Client
                                                      // List
                                                      // 전송
                  serverConn.exitRoom(this); // Client List 에서 삭제
                  serverConn.display(); // 접속자 리스트 보여주기

                  break;
               }
            }
         } catch (IOException e) {
            e.printStackTrace();
         } finally {
            try {
               br.close();
               bw.close();
               socket.close();

               System.out.println("socket 종료");
            } catch (Exception e) {
            }
         }
      }

   }
   
   public String getOnlyNumberString(String str){
       if(str == null) return str;

       StringBuffer sb = new StringBuffer();
       int length = str.length();
       for(int i = 0 ; i < length ; i++){
           char curChar = str.charAt(i);
           if(Character.isDigit(curChar)) sb.append(curChar);
       }

       return sb.toString();
   }

   // 로그인
   public void login() {

      String tmpId = "";
      boolean isNicOk = false;

      try {
         while (true) {
            tmpId = br.readLine();
            strId = tmpId;

            for (int i = 0; i < serverConn.list.size(); i++) {
               if (serverConn.list.get(i).equals(strId)) { // 아이디가 중복된다면 다시
                                                // 입력 바람
                  bw.write("CLIENTIDNO|" + strId + "\n"); // Client ID 중복
                  bw.flush();

                  System.out.println("ID 종복됬습니다.");
                  isNicOk = true;
                  break;
               } else {
                  isNicOk = false;
               }
            }
            if (isNicOk == false) {
               serverConn.enterRoom(this);
               System.out.println("ID 받아오기 성공 : " + strId);

               serverConn.broadCasting("CLIENTID|" + strId); // 접속한 Client
                                                   // ID 전송
               serverConn.broadCasting("CHATTING|*****" + strId
                     + "님이 추가되었습니다.*****" + "\n");

               serverConn.display(); // 접속자 리스트 보여주기

               break;
            }
         }
      } catch (IOException e) {
      }
   }

   // 메세지 전송
   public void sendMessage(String message) {
      try {
    	  
    	  
         bw.write(message + "\n");
         bw.flush();
      } catch (Exception e) {
         System.out.println("sendMessage() 예외발생");
      }
   }
}