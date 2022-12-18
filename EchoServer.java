import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class EchoServer {
    static int Port = 4444;

    static String msg = "";
    static List<String> data = new ArrayList<String>();
    static Boolean found = false;

    public static void main(String[] args) {

        System.out.println("Server running on port : " + String.valueOf(Port));
        try {
            ServerSocket socket = new ServerSocket(Port);
            while (true) {
                Socket msocket = socket.accept();
                System.out.println("---> Incomming request ... ");
                DataInputStream dis = new DataInputStream(msocket.getInputStream());
                msg = dis.readUTF();

                // get user email from received data
                int posRecEm = msg.indexOf("email\":\"");
                posRecEm += String.valueOf("email\":\"").length();
                int posRecPass = msg.indexOf("\"password");
                posRecPass -= 2;
                String recEm = msg.substring(posRecEm, posRecPass);
                // System.out.println("msg " + msg);

                // if user not found add account

                Iterator<String> iterator = data.iterator();
                for (String user : data) {
                    int posEm = user.indexOf("email\":\"");
                    posEm += String.valueOf("email\":\"").length();
                    int posPass = user.indexOf("\"password");
                    posPass -= 2;
                    String email = user.substring(posEm, posPass);

                    if (email.trim().equals(recEm.trim())) {
                        found = true;
                        break;
                    } else {
                        found = false;
                    }
                }
                if (found == false) {
                    data.add(msg);
                }

                // send response to client
                DataOutputStream dos = new DataOutputStream(msocket.getOutputStream());

                String res = "User not found !";
                Iterator<String> iter = data.iterator();
                while (iter.hasNext()) {
                    String i = (String) iter.next();
                    String em = getEmail(i);
                    if (em.trim().equals(recEm.trim())) {
                        res = i;
                    }
                }
                System.out.println("==> Received data : " + msg);
                System.out.println("==> Sending data : " + res);

                dos.writeUTF(res);
                dos.flush();
                dis.close();
                dos.close();
                // System.out.println("==> data" + data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static String getEmail(String msg) {
        int posRecEm = msg.indexOf("email\":\"");
        posRecEm += String.valueOf("email\":\"").length();
        int posRecPass = msg.indexOf("\"password");
        posRecPass -= 2;
        String recEm = msg.substring(posRecEm, posRecPass);
        return recEm;
    }

    public static String searchUser(String email) {
        String res = "User not found !";
        Iterator<String> iterator = data.iterator();
        while (iterator.hasNext()) {
            String i = (String) iterator.next();
            int posEm = i.indexOf("email\":\"");
            posEm += String.valueOf("email\":\"").length();
            int posPass = i.indexOf("\"password");
            posPass -= 2;
            String user_email = i.substring(posEm, posPass);
            if (user_email.trim() == email.trim()) {
                res = i;
                break;
            }

        }
        return res;
    }

}
