import java.util.ArrayList;
import java.util.Vector;

public class GUIServerConn {

   ArrayList<GUIServerThread> list = new ArrayList<GUIServerThread>();

   // Client 를 ArrayList 에 추가
   public void enterRoom(GUIServerThread tmpMultiServerThread) {
      list.add(tmpMultiServerThread);
   }

   // 접속자 수, 접속자 명
   public void display() {
      System.out.println("현재 랭킹 접속자 정보 : 접속자 수 -> " + list.size());

      if (list.size() != 0) {
         System.out.println("******** 접속한 Client ID ********");
         for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i).strId);
         }
         System.out.println("**********************************");
      }
   }

   // 접속된 모든 Client 리스트 전송
   public void broadCastingClientIdList() {
      GUIServerThread multiServerThread = null;

      String tmpClientID = "CLIENTIDLIST";

      if (list.size() != 0) { // Client ID 획득
         for (int i = 0; i < list.size(); i++) {
            tmpClientID = tmpClientID + "|" + list.get(i).strId;
         }
      }

      multiServerThread = list.get(list.size() - 1);
      multiServerThread.sendMessage(tmpClientID);
   }

   // Client가 채팅에서 나갈 때
   public void exitRoom(GUIServerThread tmpMultiServerThread) {
      boolean isDelete = list.remove(tmpMultiServerThread);
      if (isDelete) {
         System.out.println(tmpMultiServerThread.strId + "Client 제거");
      } else {
         System.out.println(tmpMultiServerThread.strId + "Client 제거 실패");
      }
   }

   // Client 에게 메시지 전송
   public void broadCasting(String message) {
      GUIServerThread serverThread = null;

      for (int i = 0; i < list.size(); i++) {
         serverThread = list.get(i);
         serverThread.sendMessage(message);
      }
   }
}