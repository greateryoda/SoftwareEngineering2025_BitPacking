import java.util.Arrays;

public class OverFlow extends BitPacking {
    int threshold = 0;  
    int[] length_of_each;   // bit-length of each integer in the original array
    int[] compressed_arr;
    int threshold_index = 0; // number of elements fitting under the threshold
    int overflow_count = 0; // number of overflow elements
    int packed_int_count = 0; // number of 32-bit words used for the main compressed area
    int compressed_arr_length;

    public OverFlow(int[] arr) {
        super(arr);
        this.length_of_each = new int[arr.length];
    }


    @Override
    public int get_compressed_arr_length(){
        return compressed_arr_length;
    }


    @Override
    int Packing_Length() {
        threshold_index = 0;

        // Compute the bit length of each element
        for (int i = 0; i < arr.length; i++) {
            length_of_each[i] = lengthOfBit(arr[i]);
        }

        // Copy and sort the bit lengths
        int[] temp = Arrays.copyOf(length_of_each, arr.length);
        Arrays.sort(temp);

        // Choose the 75th percentile as the threshold
        double percentile = 0.75; // 75% of values are small enough to fit under the threshold
        int index = (int) Math.floor(percentile * (temp.length - 1));
        threshold = temp[index];

        // Count how many values fit within this threshold
        for (int len : length_of_each) {
            if (len <= threshold) threshold_index++;
        }
        return threshold;
    }


    //Computes how many 32-bit integers are required to pack n values
    int computePackedWordCount(int n, int w) {
        int words = 1;
        int pos = 0;
        for (int i = 0; i < n; i++) {
            if (pos + w > 32) {
                words++;
                pos = 0;
            }
            pos += w;
        }
        return words;
    }


    /*
     Compresses the array using overflow encoding
     Values under the threshold are packed into 32-bit words
     Overflow values are stored at the end of the array
     Each packed value uses one extra bit as a flag: (0 indicates number under threshold, 1 indicates overflow number)
    */
    @Override
    void compress(int[] arr) {
        Packing_Length();
        overflow_count = length_of_each.length - (threshold_index);


        int w = threshold + 1; // 1 extra bit for flag + data

        packed_int_count = computePackedWordCount(arr.length, w); //calculate packed int

        int total_len = packed_int_count + overflow_count;  //overall length needed for compressed array
        compressed_arr = new int[total_len+2];  // +2 for safety margin 
        compressed_arr_length = total_len+2;

        int index_arr = 0;  //the index which iterates in the initial array
        int compress_index_arr = 0; //the index which iterates in the compressed array
        int position_bit = 0;   // bit position within current 32-bit word
        int quantity_of_overflow_numbers = 0;   //counter for the overflow numbers in the overflow area

        while(index_arr < arr.length){
            //if next value doesnt fit in the current 32-bit word
            if(position_bit + w > 32){
                position_bit = 0;
                compress_index_arr++;
            }
            
            //for integers which are under the threshold
            if(length_of_each[index_arr] <= threshold){ 
                compressed_arr[compress_index_arr] = compressed_arr[compress_index_arr] | (arr[index_arr]<<position_bit);
                
                
            }
            //The overflow case
            else{   
                int mask = (1<<threshold) | quantity_of_overflow_numbers;
                compressed_arr[compress_index_arr] = compressed_arr[compress_index_arr] | (mask << position_bit);   //add the overflow indicator in the normal area
                compressed_arr[packed_int_count + quantity_of_overflow_numbers] = arr[index_arr];   //add the overflow number in the overflow area
                quantity_of_overflow_numbers++;
            }
            position_bit += w;
            index_arr++;
        }
    }

    @Override
    int get(int i) {
        int w = threshold + 1;
        int wordIndex = i / (32/w); // Which 32-bit word contains the element
        int offset = (i % (32/w))*w; // Bit offset of the element within that word
        int flag = (compressed_arr[wordIndex] >> offset) & (1<<threshold);  //extract the flag
        int mask = (1 << threshold) - 1;

        // The case of value which is under the threshold
        if(flag == 0){
            return (compressed_arr[wordIndex] >> offset) & mask;
        }
        // The overflow case
        else{
            int index_overflow_number = (compressed_arr[wordIndex] >> offset) & mask;   //extracting the index of the overflow number
            return compressed_arr[packed_int_count + index_overflow_number];
        }
    }

    @Override
    void afficher() {
        for (int i = 0; i < compressed_arr.length; i++) {
            System.out.println(i+" element "+compressed_arr[i]);
        }
    }



}
