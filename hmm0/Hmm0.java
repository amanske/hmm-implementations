import java.util.*;
import java.io.*;
import java.math.*;
import java.lang.*;


public class Hmm0 {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        // [y][x]
        double[][] stateMatrix, obsMatrix, initMatrix;
        double[] calcedObsProbs;

        public static void main(String[] args){
            new Hmm0();
        }


        public Hmm0(){
            parseMatrices();
            doTheThing();
            writeOutput();
        }


        public void writeOutput(){
            System.out.print("1 ");
            System.out.print(calcedObsProbs.length + " ");
            for(double d : calcedObsProbs){
                System.out.print(d + " ");
            }
            System.out.println();
        }

        public void doTheThing(){
            int numberofinits = initMatrix[0].length;
            for(int rowIndex = 0; rowIndex < numberofinits; rowIndex++){
                double initStateProb = initMatrix[0][rowIndex];
                // System.out.println("init state probability: "  + initStateProb);
                if(initStateProb != 0){
                    for(int nextStateInd = 0; nextStateInd < stateMatrix[rowIndex].length; nextStateInd++){
                        double nextStateProb = stateMatrix[rowIndex][nextStateInd];
                        // System.out.println("next state probability: "  + nextStateProb);

                        for(int obsIndex = 0; obsIndex < obsMatrix[rowIndex].length; obsIndex++){
                            double obsValue = obsMatrix[nextStateInd][obsIndex];
                            // System.out.println("obs value probability: "  + obsValue);
                            calcedObsProbs[obsIndex] += obsValue * nextStateProb * initStateProb;
                        }
                    }
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
                     int xsize = Integer.parseInt(tokens[1]);
                     if(id == 0){
                         stateMatrix = new double[ysize][xsize];
                         for (int y = 0; y < ysize; y++){
                             for(int x = 0; x < xsize; x++){
                                  stateMatrix[y][x] = Double.parseDouble(tokens[(y*xsize) + x + 2]);
                             }
                         }
                     }else if(id == 1){
                         calcedObsProbs = new double[xsize];
                         obsMatrix = new double[ysize][xsize];
                         for (int y = 0; y < ysize; y++){
                             for(int x = 0; x < xsize; x++){
                                  obsMatrix[y][x] = Double.parseDouble(tokens[(y*xsize) + x + 2]);
                             }
                         }
                     }else if(id == 2){
                         initMatrix = new double[ysize][xsize];
                         for (int y = 0; y < ysize; y++){
                             for(int x = 0; x < xsize; x++){
                                  initMatrix[y][x] = Double.parseDouble(tokens[(y*xsize) + x + 2]);
                             }
                         }
                     }
                     id++;
                 }
            } catch (IOException e){

            }
            // System.out.println(Arrays.deepToString(stateMatrix));
            // System.out.println(Arrays.deepToString(obsMatrix));
            // System.out.println(Arrays.deepToString(initMatrix));

        }
}
