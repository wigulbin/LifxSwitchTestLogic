import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class BulbClient {
    private DatagramSocket socket;
    private InetAddress address;

    private byte[] buf;

    public BulbClient(){
        try{
            socket = new DatagramSocket();
            address = InetAddress.getByName("255.255.255.255");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public String sendMessage(byte[] message){
        try {
            buf = message;

            DatagramPacket packet = new DatagramPacket(buf, buf.length, address, 56700);
            socket.send(packet);
            packet = new DatagramPacket(buf, buf.length);
            socket.receive(packet);
            byte[] receivedMessage = packet.getData();
            String test = "";
            for (byte b : receivedMessage) {
                test += byteToHex(b);
            }
            String received = new String(packet.getData(), 0, packet.getLength());
            return received;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public void close(){
        socket.close();
    }

    public String byteToHex(byte num) {
        char[] hexDigits = new char[2];
        hexDigits[0] = Character.forDigit((num >> 4) & 0xF, 16);
        hexDigits[1] = Character.forDigit((num & 0xF), 16);
        return new String(hexDigits);
    }
}
