
public class NotTwoConsecutiveInt extends BitPacking{
    int size_packet;    //size of packet in an array
    int[] compressed_arr;   //the compressed array
    int compressed_arr_length;  //compressed array length

    public NotTwoConsecutiveInt(int[] arr){
        super(arr);
    }

    @Override
    public int get_compressed_arr_length(){ //getter of the length array
        return compressed_arr_length;
    }

    @Override
    void compress(int[] arr){
        int index_arr = 0;  //the index which iterates in the initial array
        int index_arr_compressed = 0;   //the index which iterates in the compressed array
        size_packet = Packing_Length(); //find the packet size for the given array
        int number_in_one_int = 32 / size_packet; // number of compressed integers that can fit inside one 32-bit integer
        int num=0;

        compressed_arr = new int[(arr.length + number_in_one_int - 1) / number_in_one_int];
        compressed_arr_length =  (arr.length + number_in_one_int - 1) / number_in_one_int;

        while(index_arr < arr.length){
            int mini_required = Math.min(arr.length-index_arr,number_in_one_int); // how many elements fit into the current 32-bit integer
            for(int i = 0; i<mini_required; i++){
                num = num | arr[index_arr]<<size_packet*i;
                index_arr++;
            }
            compressed_arr[index_arr_compressed++] = num;   
            num = 0;    //reinitialization to 0
        }
    }

    @Override
    int get(int i){
        int wordIndex = i / (32/size_packet); // index of the 32-bit word containing the i-th value
        int offset = (i % (32/size_packet))*size_packet; // bit offset of the i-th value in that word
        int mask = (1 << size_packet) - 1;  //mask to get the value 
        return (compressed_arr[wordIndex] >> offset) & mask;
    }
    
    @Override
    void afficher(){
        for(int i = 0; i<compressed_arr.length;i++){
            System.out.println(i+" element "+compressed_arr[i]);
        }
    }
}
