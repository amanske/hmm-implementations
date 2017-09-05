import java.util.*;
import java.io.*;
import java.math.*;
import java.lang.*;


public class Hmm1 {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        // [y][x]
        double[][] stateMatrix, obsMatrix, initMatrix;
        int[] sequence;

        public static void main(String[] args){
            new Hmm1();
        }


        public Hmm1(){
            parseMatrices();
            // doTheThing();
            writeOutput();
        }


        public void writeOutput(){
            System.out.println("sequence: " + Arrays.toString(sequence));
        }

        public void doTheThing(){

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
                         initMatrix = new double[ysize][xsize];
                         for (int y = 0; y < ysize; y++){
                             for(int x = 0; x < xsize; x++){
                                  initMatrix[y][x] = Double.parseDouble(tokens[(y*xsize) + x + 2]);
                             }
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
