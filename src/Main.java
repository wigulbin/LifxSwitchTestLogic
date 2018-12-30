import java.util.BitSet;

public class Main {

    public static void main(String[] args) {
        System.out.println("Hello World!");

        BulbClient client = new BulbClient();

        String hex = "31000034000000000000000000000000000000000000000000000000000000006600000000000000005555000000040000";
        String start        = "0034";
        String source       = "00000000";

        String target       = "0000000000000000";
        String reserved1    = "000000000000";
        String ackRes       = "00";
        String sequence     = "66";

        String reserved2    = "0000000000000000";
        String messageType  = "7500";
        String reserved3    = "0000";

        String header = start + source + target + reserved1 + ackRes + sequence + reserved2 + messageType + reserved3 + "000000040000";
        System.out.println((header.length() + 4) / 2);
        String length = decimal2hex((header.length() + 4) / 2);
        header = length  + "00" + header;

        System.out.println(header);

        byte[] bytes = hexStringToByteArray(hex);
        System.out.println(client.sendMessage(bytes));

        PacketBuilder builder = PacketBuilder.buildPacket();


//        System.out.println(new String(hexStringToByteArray("c884fec2d4"), 0, hexStringToByteArray("c884fec2d4").length));
    }

    public static byte[] hexStringToByteArray(String s) {
        byte[] b = new byte[s.length() / 2];
        for (int i = 0; i < b.length; i++) {
            int index = i * 2;
            int v = Integer.parseInt(s.substring(index, index + 2), 16);
            b[i] = (byte) v;
        }
        return b;
    }

    public static String decimal2hex(int d) {
        String digits = "0123456789ABCDEF";
        if (d <= 0) return "0";
        int base = 16;   // flexible to change in any base under 16
        String hex = "";
        while (d > 0) {
            int digit = d % base;              // rightmost digit
            hex = digits.charAt(digit) + hex;  // string concatenation
            d = d / base;
        }
        return hex;
    }
}

