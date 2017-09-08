import java.util.*;
import java.io.*;
import java.math.*;
import java.lang.*;


public class Hmm2 {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        // [y][x]
        double[][] stateMatrix, obsMatrix;
        double[] initMatrix;
        int[] sequence;
        double[][] deltaMatrix;
        int[][] deltaStates;
        int[] probableSeq;


        public static void main(String[] args){
            new Hmm2();
        }

        public Hmm2(){
            parseMatrices();
            doTheThing();
            writeOutput();
        }

        public void writeOutput(){
            for(int i = 0; i < probableSeq.length;i++){
                System.out.print(probableSeq[i]);
                if(i != probableSeq.length -1){
                    System.out.print(" ");
                }
            }
            System.out.println();
        }

        public void doTheThing(){
            //Initialize
            int deltalength = initMatrix.length;
            int sequenceLength = sequence.length;
            deltaMatrix = new double[sequence.length][deltalength];
            deltaStates = new int[sequence.length][deltalength];
            for(int i = 0; i < deltalength; i++){
                deltaMatrix[0][i] = initMatrix[i] * obsMatrix[i][sequence[0]];
            }

            //Build delta matrices
            for(int t = 1; t < sequenceLength; t++){
                for(int j = 0; j < deltalength; j++){
                    ArrayList<Double> values = new ArrayList<>();
                    for(int i = 0; i < deltalength; i++){
                        values.add(deltaMatrix[t-1][i] * stateMatrix[i][j] * obsMatrix[j][sequence[t]]);
                    }
                    ArrayList<Integer> maxindexes = MaxIndex(values);
                    deltaMatrix[t][j] = values.get(maxindexes.get(0)); //Get the probability because we only have the index to max probabilities
                    deltaStates[t][j] = maxindexes.get(0);
                }
            }

            //Backtrack most probable sequence
            probableSeq = new int[sequenceLength];
            int currentState = -1;
            for(int t = sequenceLength-1; t >= 0; t--){
                if(t == sequenceLength-1){
                    ArrayList<Double> arr = new ArrayList<>();
                    for (int i = 0; i < deltaMatrix[t].length; i++){
                        arr.add(deltaMatrix[t][i]);
                    }
                    currentState = MaxIndex(arr).get(0);
                }else{
                    currentState = deltaStates[t+1][currentState];
                }
                probableSeq[t] = currentState;
            }
        }

        public ArrayList<Integer> MaxIndex(ArrayList<Double> d){
            double currentMax = -1;
            ArrayList<Integer> result = new ArrayList<>();
            for(int i = 0; i < d.size(); i++){
                if(d.get(i) == currentMax){
                    result.add(i);
                }else if(d.get(i) > currentMax){
                    result = new ArrayList<>();
                    currentMax = d.get(i);
                    result.add(i);
                }
            }
            return result;
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
