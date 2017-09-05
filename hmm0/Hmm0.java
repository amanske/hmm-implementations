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
            System.out.print("1 "); //always 1 row in output
            System.out.print(calcedObsProbs.length + " ");
            for(double d : calcedObsProbs){
                System.out.print(d + " ");
            }
            System.out.println();
        }

        public void doTheThing(){

            int numberofinits = initMatrix[0].length; //number of states

            for(int rowIndex = 0; rowIndex < numberofinits; rowIndex++){

                double initStateProb = initMatrix[0][rowIndex]; //the probability of starting at the current state

                if(initStateProb != 0){ //if starting on state has probability 0, there is no reason to calculate it

                    for(int nextStateInd = 0; nextStateInd < stateMatrix[rowIndex].length; nextStateInd++){ //iterate through state matrix

                        double nextStateProb = stateMatrix[rowIndex][nextStateInd]; //the probability of transferring to a state from the current state

                        for(int obsIndex = 0; obsIndex < obsMatrix[rowIndex].length; obsIndex++){ //iterate through the emission matrix

                            double obsValue = obsMatrix[nextStateInd][obsIndex]; //probability of the obeservation given the state
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
        }
}
