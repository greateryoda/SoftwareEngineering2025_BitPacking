
import java.util.Scanner;

class manuelTest{
    public static void menu() {
        System.out.println("--------------------------------------------------------------");
        System.out.println("""
        Choose an option:
        1) First Method
        2) Second Method
        3) Third Method
        9) Change array
        -1) Quit
        """);
    }

    //creation of a new array
    public static int[] createArray(Scanner scanner) {
        System.out.println("Enter the length of your array:");
        int length = scanner.nextInt();
        int[] arr = new int[length];
        System.out.println("Enter " + length + " integers:");
        for (int i = 0; i < length; i++) {
            arr[i] = scanner.nextInt();
        }
        return arr;
    }


    public static void print_decompressed_array(int[] decompressed_arr){
        System.out.println("Decompressed array: ");
        for (int val : decompressed_arr)
            System.out.print(val + " ");
        System.out.println();
    }




    public static void main(String[] args){
        long start_comp, fin_comp;  //var for counting the compression time
        long start_decomp, fin_decomp;  //var for counting the decompression time
        long Su,Sc;     //Su : uncompressed size of an array (32 x arr.length) | Sc : compressed size of the array
        Scanner scanner = new Scanner(System.in);

        int[] arr = createArray(scanner);   //creating an array given the user's choice
        int[] decompressed = new int[arr.length];   //for decompression


        BitPacking bitpacking_agent = new BitPacking(arr);
        

        int method_selection = -2;  //initialization of the user's method choice
        while(method_selection != -1){
            menu();
            method_selection = scanner.nextInt();   //selection the method

            switch(method_selection){   //using factory design pattern
                case 1: bitpacking_agent = bitpacking_agent.createType(BitPacking.Method.First); break;
                case 2: bitpacking_agent = bitpacking_agent.createType(BitPacking.Method.Second); break;
                case 3: bitpacking_agent = bitpacking_agent.createType(BitPacking.Method.Third); break;
                case 9: {   //changing the array
                    arr = createArray(scanner);
                    bitpacking_agent = new BitPacking(arr);
                    decompressed = new int[arr.length];
                    continue;
                }
                case -1: System.out.println("quitting...");break;
                default: System.out.println("Invalid choice. Please try again."); continue;
            }
            if(method_selection >= 1 && method_selection <= 3){

                // --- Compression ---
                start_comp = System.nanoTime();
                bitpacking_agent.compress(arr);
                fin_comp = System.nanoTime();
                long total_comp = fin_comp - start_comp;
                System.out.println("Compression time of Method : "+(total_comp));   //total compression time

                System.out.print("This is the compressed array: ");         //print the compressed array
                bitpacking_agent.afficher();


                // --- Decompression ---
                start_decomp = System.nanoTime();
                bitpacking_agent.decompress(decompressed);
                fin_decomp = System.nanoTime();
                long total_decomp = fin_decomp - start_decomp;
                System.out.println("Decompression time of Method : "+(total_decomp));   //total decompression time
                print_decompressed_array(decompressed); //print the decompressed array


                // --- Latency threshold --- (Calculation of the latency with respect to the formula mentionned in the report)
                double total_ms = (total_comp + total_decomp) / 1_000_000.0;    //convert to ms
                Su = (long) arr.length * 32;
                Sc = (long) bitpacking_agent.get_compressed_arr_length() * 32;

                if (Sc >= Su) {
                System.out.println("Compression ineffective (Sc >= Su)");
                }
                else{
                    double t_threshold = total_ms / ((Su - Sc) / 1_000_000.0);  //converting Su,Sc to Mbits
                    System.out.printf("Threshold latency t*: %.4f ms/Mbit%n", t_threshold);
                }

            }


        }
        scanner.close();
        
    }
}