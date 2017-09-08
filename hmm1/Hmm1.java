import java.util.*;
import java.io.*;
import java.math.*;
import java.lang.*;


public class Hmm1 {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        // [y][x]
        double[][] stateMatrix, obsMatrix;
        double[] initMatrix;
        int[] sequence;
        double[] alpha, alphaPrev;
        double[] alphaTemp;

        public static void main(String[] args){
            new Hmm1();
        }

        public Hmm1(){
            parseMatrices();
            doTheThing();
            writeOutput();
        }

        public void writeOutput(){
            double sum = 0;
            for(int i = 0; i < alpha.length; i++){
                sum += alpha[i];
            }
            System.out.println(sum);
        }

        public void doTheThing(){
            //Initialize pi* b(O_1)
            int alphalength = initMatrix.length;
            alpha = new double[alphalength];
            alphaPrev = new double[alphalength];

            for(int i = 0; i < alphalength; i++){
                alpha[i] = initMatrix[i] * obsMatrix[i][sequence[0]];
            }

            //Looping (sum of alpha_t-1 [i] + a[i][j]) * b_i(O_t)
            int sequenceLength = sequence.length;

            for(int t = 1; t < sequenceLength; t++){
                alphaPrev = alpha;
                alpha = new double[alphalength];
                for(int j = 0; j < alphalength; j++){
                    double currentsum = 0;
                    for(int i = 0; i < alphalength; i++){
                        currentsum += alphaPrev[i] * stateMatrix[i][j];
                    }
                    int observation = sequence[t];
                    alpha[j] = currentsum * obsMatrix[j][observation];
                }

            }


        }

        public void parseMatrices(){

            try{
                 String line = null;
                 int id = 0;
                 while( (line = reader.readLine()) != null){
                     String[] tokens = line.split(" ");
                     int ysize = Integer.parseInt(tokens[0]);
                     int xsize = Integer.parseInt(tokens[1]); // >0
                     if(id == 0){
                         stateMatrix = new double[ysize][xsize];
                         for (int y = 0; y < ysize; y++){
                             for(int x = 0; x < xsize; x++){
                                  stateMatrix[y][x] = Double.parseDouble(tokens[(y*xsize) + x + 2]);
                             }
                         }
                     }else if(id == 1){
                         obsMatrix = new double[ysize][xsize];
                         for (int y = 0; y < ysize; y++){
                             for(int x = 0; x < xsize; x++){
                                  obsMatrix[y][x] = Double.parseDouble(tokens[(y*xsize) + x + 2]);
                             }
                         }
                     }else if(id == 2){
                         initMatrix = new double[xsize];
                         for(int x = 0; x < xsize; x++){
                              initMatrix[x] = Double.parseDouble(tokens[x + 2]);
                         }
                     }else if(id == 3){
                         int sequenceSize = Integer.parseInt(tokens[0]);
                         sequence = new int[sequenceSize];
                         for(int i = 0; i < sequenceSize; i++){
                             sequence[i] = Integer.parseInt(tokens[i + 1]);
                         }
                     }
                     id++;
                 }
            } catch (IOException e){

            }
        }
}
