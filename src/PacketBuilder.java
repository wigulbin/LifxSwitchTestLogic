import java.util.BitSet;

public class PacketBuilder {
    private BitSet bits;

    private PacketBuilder(){
        bits = new BitSet();
    }

    public static PacketBuilder buildPacket(){
        PacketBuilder builder = new PacketBuilder();
        builder.setHeader();
        return builder;
    }

    public byte[] getByteArray(){
        return bits.toByteArray();
    }

    private void updateLength(){
        int length = bits.length();
        BitSet bitSet = createBitFromInt(length);
        for(int i = 0; i < 8; i++){
             if(bitSet.get(i))
                 bits.set(i);
        }
    }

    private void setHeader(){
        addBitsFromInt(34, 24);
        updateLength();
    }


    private BitSet addBitsFromInt(int value, int start){
        int counter = start;
        while(value > 0){
            if(value % 2 == 0)
                bits.set(counter);
            counter++;
            value = value >>> 1;
        }

        return bits;
    }

    private BitSet createBitFromInt(int value){
        BitSet bitSet = new BitSet();
        int counter = 0;
        while(value > 0){
            if(value % 2 == 0)
                bitSet.set(counter);
            counter++;
            value = value >>> 1;
        }

        return bitSet;
    }
}
