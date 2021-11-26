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
        return IntStream.iterate(0, start -> start + load)
                .limit(nthread)
                .mapToObj(start -> new Worker(matrix, start, load))
                .peek(Thread::start)
                .peek(MultiThreadedSumMatrix::joinUninterruptibly)
                .mapToInt(Worker::getSum)
                .sum();
    }
    
    private static void joinUninterruptibly(final Thread target) {
        var joined = false;
        while (!joined) {
            try {
                target.join();
                joined = true;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    private class Worker extends Thread {
        
        private final double[][] matrix;
        private final int start;
        private final int size;
        private int sum;
        
        public Worker(final double[][] matrix, final int start, final int size) {
            this.matrix = matrix;
            this.start = start;
            this.size = size;
        }
        
        @Override
        public void run() {
            for (int i = start; i < start + size; i++) {
                for(int j = 0; j < matrix[i].length; j++) {
                    sum += matrix[i][j];
                }
            }
        }
        
        public int getSum() {
            return this.sum;
        }
        
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