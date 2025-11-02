

public class BitPacking{
    int arr[];

    public enum Method {    //enum for methods
        First,
        Second,
        Third
    }

    public BitPacking(int[] arr){
        this.arr = arr;
    }

    BitPacking createType(Method method){   //Factory Pattern
        switch(method){
            case First : return new TwoConsecutiveInt(arr);
            case Second : return new NotTwoConsecutiveInt(arr);
            case Third : return new OverFlow(arr);
            default: throw new IllegalArgumentException("Unknown method: " + method);
        }
    }
    


    int lengthOfBit(int x) {    //Find length of bits for the given integer
        return x == 0 ? 1 : 32 - Integer.numberOfLeadingZeros(x);
    }

    int Packing_Length() {  //Find size of packing in an array
        int max = 0;
        for (int val : arr)
            if (val > max) max = val;
        return lengthOfBit(max);
    }
    void compress(int[] arr){}

    void afficher(){}

    void decompress(int[] decompressed_arr){
        for(int i = 0; i<arr.length; i++){
            decompressed_arr[i] = get(i);
        }
    }

    int get(int i){return -1;}

    public int get_compressed_arr_length(){
        return -1;
    }
}