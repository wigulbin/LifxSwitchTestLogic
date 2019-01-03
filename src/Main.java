import java.util.BitSet;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        BulbClient client = new BulbClient();

        String hex = "31000034000000000000000000000000000000000000000000000000000000006600000000000000005555000000040000";
        String start        = "0034";
        String source       = "00000000";

        String target       = "0000000000000000";
        String reserved1    = "000000000000";
        String ackRes       = "00";
        String sequence     = "00";

        String reserved2    = "0000000000000000";
        String messageType  = "7500";
        String reserved3    = "0000";

        String header = start + source + target + reserved1 + ackRes + sequence + reserved2 + messageType + reserved3 + "FFFF00040000";
        System.out.println((header.length() + 4) / 2);
        String length = decimal2hex((header.length() + 4) / 2);
        header = length  + "00" + header;

        System.out.println(header);

        byte[] bytes = hexStringToByteArray(header);
        PacketBuilder builder = PacketBuilder.buildPacket();

        List<Byte> byteList = builder.getByteList();
        builder.setPower(true, 1024);
        System.out.println(byteList);

        byte[] bytes1 = builder.getByteArray();

        System.out.println(client.sendMessage(bytes));
    }

    public static byte[] hexStringToByteArray(String s) {
        byte[] b = new byte[s.length() / 2];
        for (int i = 0; i < b.length; i++) {
            int index = i * 2;
            int v =     Integer.parseInt(s.substring(index, index + 2), 16);
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


    public static BitSet addBitsFromInt(int value, int start){
        BitSet bits = new BitSet();
        int counter = start;
        while(value > 0){
            if(value % 2 != 0)
                bits.set(counter);
            counter++;
            value = value >>> 1;
        }

        return bits;
    }
}

