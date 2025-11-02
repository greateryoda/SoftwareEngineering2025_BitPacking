import java.util.Random;

class LatencyThreshold {
  public static void main(String[] args) {
    Random rnd = new Random(42);
    int[] arr = rnd.ints(100000000, 0, 4096).toArray();
    int[] dec = new int[arr.length];

    BitPacking m1 = new BitPacking(arr).createType(BitPacking.Method.Third);  //selection of a method

    long start = System.nanoTime();
    m1.compress(arr);
    m1.decompress(dec);
    long end = System.nanoTime();

    double total_ms = (end - start) / 1_000_000.0;
    long Su = (long) arr.length * 32;
    long Sc = (long) m1.get_compressed_arr_length() * 32;

    if (Sc >= Su) {
      System.out.println("Compression ineffective (Sc >= Su)");
      return;
    }

    double t_threshold = total_ms / ((Su - Sc) / 1_000_000.0);
    System.out.printf("Total time: %.3f ms%n", total_ms);
    System.out.printf("Threshold latency t*: %.4f ms/Mbit%n", t_threshold);
  }
}
