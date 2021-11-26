package it.unibo.oop.lab.workers02;

import java.util.stream.IntStream;

public class MultiThreadedSumMatrix implements SumMatrix {
    
    private final int nthread;
    
    public MultiThreadedSumMatrix(final int n) {
        this.nthread = n;
    }

    @Override
    public double sum(double[][] matrix) {
        int size = matrix.length;
        int load = size / nthread;
        System.out.println("Nthreads: " + this.nthread);
        IntStream.iterate(load, start -> start + load).limit(nthread);
        

        return 0;
    }

}


/* size = 10 (*10)
 * nthreads = 2
 * load = 5
 * 
 * for (i = 0; i < 10; i += 5 )
 * 
 * 0 -> 5 -> 10
 * 
 * 1 2 3 4 5 6 7 8 9 0
 * 2 3 4 5 6 7 8 8 9 1
 * 3 4 5 6 7 8 9 0 1 2
 * 3 4 5 6 7 8 9 0 1 2
 * 3 4 5 6 7 8 9 0 1 2
 * 3 4 5 6 7 8 9 0 1 2
 * 3 4 5 6 7 8 9 0 1 2
 * 3 4 5 6 7 8 9 0 1 2
 * 3 4 5 6 7 8 9 0 1 2
 * 3 4 5 6 7 8 9 0 1 2
 * 
 */