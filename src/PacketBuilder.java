import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class PacketBuilder {
    private BitSet bits;
    private List<Byte> byteList;

    private BitSet payloadBits;

    private PacketBuilder(){
        bits = new BitSet();
        byteList = new ArrayList<>();
        byteList.add(((byte) 0));
        byteList.add(((byte) 0));
    }

    public static PacketBuilder buildPacket(){
        PacketBuilder builder = new PacketBuilder();
        builder.createHeader();
        return builder;
    }

    public byte[] getByteArray(){
        List<Byte> bytes = new ArrayList<>();
        byte[] byteArr = new byte[bytes.size()];


        byte[] byteArray = bits.toByteArray();
        byteArray[0] = (byte)byteArray.length;

        return byteArray;
    }
    public List<Byte> getByteList(){
        List<Byte> bytes = new ArrayList<>(getHeaderBytes());
        return bytes;
    }

    private List<Byte> getHeaderBytes(){
        List<Byte> byteList = new ArrayList<>(36);
        byte[] bytes = bits.toByteArray();
        for (byte aByte : bytes)
            byteList.add(aByte);

        while(byteList.size() < 36)
            byteList.add((byte) 0);

        setLength(byteList);

        return byteList;
    }

    private void setLength(List<Byte> byteList){
        byteList.set(0, (byte)byteList.size());
        byteList.set(1, (byte)(byteList.size() >>> 8));
    }

    private void createEmptyPayload(){
        payloadBits = new BitSet();

    }

    public void createHeader(){
        setProtocol();
        setAddressable(true);
        setTagged(true);

        setMessage(117);

        byteList = getByteList();
    }

    private void setTagged(boolean on){
        setBits(on, 29);
    }
    private void setAddressable(boolean on){
        setBits(on, 28);
    }

    private void setProtocol(){
        addBitsFromInt(1024, 16);
    }

    private void setUniqueValue(){
        //TODO create unique value
    }

    public void setTarget(String macHex){
        //TODO allow for target to be set starts at bit 64
        List<Integer> ints = convertHexToInts(macHex);
        AtomicInteger start = new AtomicInteger(64);
        for (Integer anInt : ints)
            addBitsFromInt(anInt, start.getAndAdd(8));

        setTagged(false);
    }

    private void setAckRequired(){
        bits.set(182);
    }
    private void setResRequired(){
        bits.set(183);
    }
    private void setSequence(){

    }

    private void setMessage(int message){
        addBitsFromInt(message, 256);
    }


    public void addBitsFromInt(int value, int start){
        int counter = start;
        while(value > 0){
            if(value % 2 != 0)
                bits.set(counter);
            counter++;
            value = value >>> 1;
        }
    }

    private BitSet createBitFromInt(int value){
        BitSet bitSet = new BitSet();
        int counter = 0;
        while(value > 0){
            if(value % 2 != 0)
                bitSet.set(counter);
            counter++;
            value = value >>> 1;
        }

        return bitSet;
    }

    private List<Integer> convertHexToInts(String hex){
        List<Integer> ints = new ArrayList<>(hex.length()/2);
        for(int i = 0; i < hex.length()/2; i++)
            ints.add(Integer.parseInt(hex.substring(i, i + 2), 16));

        return ints;
    }

    public void setPower(boolean on, int durationMili){
        int num = on ? 65535 : 0;
        List<Byte> bytes = createBytesFromInt(num, 2);
        bytes.addAll(createBytesFromInt(durationMili, 4));
        System.out.println(bytes);
        byteList.addAll(bytes);
        setLength(byteList);
    }





    public static List<Byte> createBytesFromInt(int num, int byteNum){
        List<Byte> bytes = new ArrayList<>(4);
        for(int i = 0; i < byteNum*8; i+=8)
            bytes.add((byte)(num>>>i));

        return bytes;
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

    private void setBits(boolean on, int bit){
        if(on)
            bits.set(bit);
        else
            bits.clear(bit);
    }
}
