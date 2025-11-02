# SoftwareEngineering2025_BitPacking
Bit Packing Compression Project for Université Côte d’Azur


**Author Name:** Yunus Emre

**Author Name:** Dogan



**File Descriptions**
1) BitPacking.java
Defines the base class for all compression types.
Includes the compression interface and a simple factory method.

2) TwoConsecutiveInt.java
Implements Method 1, which allows integers to span across two 32-bit words.

3) NotTwoConsecutiveInt.java
Implements Method 2, ensuring that each integer fits entirely within a single 32-bit word.

4) OverFlow.java
Implements Method 3, introducing an overflow zone for unusually large integers.

5) LatencyThreshold.java
Demonstrates how to calculate the network latency threshold at which compression becomes beneficial for a given method

6) Benchmark.java
Performs automated benchmarking of the three methods over varying array sizes and value ranges.
It was used to produce the timing tables and graphs in the project report.

7) manuelTest.java
Provides an interactive console program for manual testing.
The user can enter array data, select a compression method, and view the compressed and decompressed output directly.


**Compilation and Execution**
1) Compile everything into a /bin directory
javac -d bin src/*.java
2) Run manual testing
java -cp bin manuelTest
3) Run benchmarks
java -cp bin Benchmark

