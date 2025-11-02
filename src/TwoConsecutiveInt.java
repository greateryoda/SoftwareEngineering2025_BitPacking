public class TwoConsecutiveInt extends BitPacking{
    int size_packet; //size of packet in an array
    int[] compressed_arr; //the compressed array
    int compressed_arr_length;

    public TwoConsecutiveInt(int[] arr){
        super(arr);
    }

    @Override
    public int get_compressed_arr_length(){
        return compressed_arr_length;
    }

    @Override
    void compress(int[] arr){
        int index_arr = 0; //the index which iterates in the initial array
        int index_arr_compressed = 0; //the index which iterates in the compressed array

        size_packet = Packing_Length(); //find the packet size for the given array
        int bits = arr.length * size_packet;
        compressed_arr = new int[(bits + 31) / 32];
        compressed_arr_length = (bits + 31) / 32;

        int bit_pos = 0; // current bit position inside the 32-bit word
        while (index_arr < arr.length){

            // Case: value crosses the 32-bit boundary (split)
            if(bit_pos + size_packet > 32){
                int bitsInCurrent = 32 - bit_pos;        // number of remaining free bits in the current 32-bit word
                int bitsInNext = size_packet - bitsInCurrent;  // number of bits that will overflow into the next word

                // Write the lower portion that fits in the current 32-bit word
                int lowerPart = arr[index_arr] & ((1 << bitsInCurrent) - 1);
                compressed_arr[index_arr_compressed] |= (lowerPart << bit_pos);

                // Move to the next 32-bit word
                index_arr_compressed++;

                // Write the remaining higher bits into the next word
                int higherPart = arr[index_arr] >> bitsInCurrent;
                compressed_arr[index_arr_compressed] |= higherPart;

                // Update the current bit position for the new word
                bit_pos = bitsInNext;
            }
            // Case: value fits in the current word
            else{
                compressed_arr[index_arr_compressed] = compressed_arr[index_arr_compressed] | arr[index_arr]<<bit_pos;
                bit_pos+=size_packet;

                if(bit_pos == 32){  // if it arrives to the end of the word
                    bit_pos = 0;    //reinitialization of the bit_position 
                    index_arr_compressed++; // Move to the next 32-bit word
                }
            }
            index_arr++;
        }
    }

    @Override
    int get(int i){
        int bitIndex = i * size_packet;                 // total bit position of the value
        int wordIndex = bitIndex / 32;                  // index of the 32-bit word containing the i-th value
        int offset = bitIndex % 32;                     // bit offset of the i-th value in that word
        int value;

        // Case: value fits in the current word
        if(offset + size_packet <= 32){
            int mask = (1 << size_packet) - 1;
            value = (compressed_arr[wordIndex] >>> offset) & mask;
        }
        // Case: value crosses the 32-bit boundary (split)
        else{
            int lowBits = compressed_arr[wordIndex] >>> offset;  //lower part
            int highBits = compressed_arr[wordIndex+1] & ((1 << (size_packet - (32-offset))) - 1); //higher part
            value = lowBits | (highBits << (32-offset));
        }
        return value;
    }

    @Override
    void afficher(){
        for(int i = 0; i<compressed_arr.length; i++){
            System.out.println(i+" element "+compressed_arr[i]);
        }
    }



}