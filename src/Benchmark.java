import java.util.Random;

class Benchmark {
  public static void main(String[] args) {

    int[] sizes = {10, 100, 1000, 100000, 1000000, 10000000, 100000000};
    Random rnd = new Random(42);
    int repetitions = 5;

    for (int size : sizes) {
      System.out.println("\n=== Array size: " + size + " ===");
      int[] arr = rnd.ints(size, 0, 4096).toArray();
      int[] decompressed = new int[arr.length];

      long totalCompress1 = 0, totalCompress2 = 0, totalCompress3 = 0;
      long totalDecompress1 = 0, totalDecompress2 = 0, totalDecompress3 = 0;

      for (int rep = 0; rep < repetitions; rep++) {

        // ----- Method 1 -----
        BitPacking m1 = new BitPacking(arr);
        m1 = m1.createType(BitPacking.Method.First);
        long start = System.nanoTime();
        m1.compress(arr);
        long mid = System.nanoTime();
        m1.decompress(decompressed);
        long end = System.nanoTime();
        totalCompress1 += (mid - start);
        totalDecompress1 += (end - mid);

        // ----- Method 2 -----
        BitPacking m2 = new BitPacking(arr);
        m2 = m2.createType(BitPacking.Method.Second);
        start = System.nanoTime();
        m2.compress(arr);
        mid = System.nanoTime();
        m2.decompress(decompressed);
        end = System.nanoTime();
        totalCompress2 += (mid - start);
        totalDecompress2 += (end - mid);

        // ----- Method 3 -----
        BitPacking m3 = new BitPacking(arr);
        m3 = m3.createType(BitPacking.Method.Third);
        start = System.nanoTime();
        m3.compress(arr);
        mid = System.nanoTime();
        m3.decompress(decompressed);
        end = System.nanoTime();
        totalCompress3 += (mid - start);
        totalDecompress3 += (end - mid);
      }

      // Average times (ns)
      long avgC1 = totalCompress1 / repetitions;
      long avgC2 = totalCompress2 / repetitions;
      long avgC3 = totalCompress3 / repetitions;
      long avgD1 = totalDecompress1 / repetitions;
      long avgD2 = totalDecompress2 / repetitions;
      long avgD3 = totalDecompress3 / repetitions;

      System.out.println("Average compression times (microseconds):");
      System.out.println("  Method 1: " + avgC1 / 1000.0);
      System.out.println("  Method 2: " + avgC2 / 1000.0);
      System.out.println("  Method 3: " + avgC3 / 1000.0);

      System.out.println("Average decompression times (microseconds):");
      System.out.println("  Method 1: " + avgD1 / 1000.0);
      System.out.println("  Method 2: " + avgD2 / 1000.0);
      System.out.println("  Method 3: " + avgD3 / 1000.0);

      System.out.println("Total (compress+decompress) times (microseconds):");
      System.out.println("  Method 1: " + (avgC1 + avgD1) / 1000.0);
      System.out.println("  Method 2: " + (avgC2 + avgD2) / 1000.0);
      System.out.println("  Method 3: " + (avgC3 + avgD3) / 1000.0);
    }
  }
}
