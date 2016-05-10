/**
 * Created by VannessTan on 26/04/2016.
 */
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.Future;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class Main {

    public static void main(String[] args) {

        long time = System.currentTimeMillis();
        double[][] array = new double[200][100];
        List<Future<Double>> list = new ArrayList<Future<Double>>();
        Double globalMin = 999999.99;
        int count = 1;

        ExecutorService taskExecutor = Executors.newFixedThreadPool(8); //factory, all are static
        myTask[] myTaskRunnable = new myTask[200];

        for (int i = 0; i < 200; i++) {
            for (int j = 0; j < 100; j++) {
                array[i][j] = Math.random() * 10.24 + -5.12;
            }
            myTaskRunnable[i]= new myTask(array[i]);
            list.add(taskExecutor.submit(myTaskRunnable[i]));

        }

        for (Future<Double> fut : list) {
            try {
                double min = fut.get();
                System.out.printf("Min for row [%d] is %.4f\n", count, fut.get());
                count++;

                if(min < globalMin){globalMin = min;}

            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        taskExecutor.shutdown();

        //while(!taskExecutor.isTerminated()){};

        try {
            taskExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {

        }

        System.out.printf("\nGlobal Min %.2f: ", globalMin);
        System.out.printf("\nExecution Time: %d milliseconds", (System.currentTimeMillis() - time));
    }
}
