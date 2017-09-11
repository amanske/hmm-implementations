import java.util.*;
import java.io.*;
import java.math.*;
import java.lang.*;


public class Hmm4 {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        // [y][x]
        double[][] stateMatrix, obsMatrix;
        double[] initMatrix;
        int[] sequence;
        double[][] alphaMatrix;
        double[][] betaMatrix;
        double[] c;
        double[][][] digamma;
        double[][] gamma;

        int maxIters = 30;
        int iters = 0;
        double oldLogProb = Double.NEGATIVE_INFINITY;
        double logProb = 0;

        public static void main(String[] args){
            new Hmm4();
        }

        public Hmm4(){
            parseMatrices();
            //CONVERGENCE LOOP

            while(iters < maxIters && (logProb > oldLogProb || iters == 1)){
                doTheThing();
                iters++;
            }
            writeOutput();
        }

        public void writeOutput(){
            //A
            System.out.print(stateMatrix[0].length + " " + stateMatrix[0].length + " ");
            for(int i = 0; i < stateMatrix[0].length; i++){
                for(int j = 0; j < stateMatrix[0].length; j++){
                    System.out.print(stateMatrix[i][j] + " ");
                }
            }
            System.out.println();

            //B
            System.out.print(obsMatrix.length + " " + obsMatrix[0].length + " ");
            for(int i = 0; i < obsMatrix.length; i++){
                for(int j = 0; j < obsMatrix[0].length; j++){
                    System.out.print(obsMatrix[i][j] + " ");
                }
            }
            System.out.println();

        }

        public void doTheThing(){
            alpha();
            beta();
            gamma();
            reestimate();
            compLogProb();

        }

        public void compLogProb(){
            oldLogProb = logProb;
            int T = sequence.length;
            int N = initMatrix.length;
            logProb = 0;
            for(int i = 0; i < T; i++){
                logProb += Math.log(c[i]);
            }
            logProb = -logProb;
        }

        public void reestimate(){
            //re-estimate initial
            int T = sequence.length;
            int N = initMatrix.length;
            int K = obsMatrix[0].length;
            for(int i = 0; i < N; i++){
                initMatrix[i] = gamma[0][i];
            }

            //re-estimate stateMatrix
            for(int i = 0; i < N; i++){
                for(int j = 0; j < N; j++){
                    double numer = 0;
                    double denom = 0;
                    for(int t = 0; t < T-1; t++){
                        numer += digamma[t][i][j];
                        denom += gamma[t][i];
                    }
                    stateMatrix[i][j] = numer/denom;
                }
            }

            //re-estimate obsMatrix
            for(int i = 0; i < N; i++){
                for(int j = 0; j < K; j++){
                    double numer = 0;
                    double denom = 0;
                    for(int t = 0; t < T-1; t++){
                        if(sequence[t] == j){
                            numer += gamma[t][i];
                        }
                        denom += gamma[t][i];
                    }
                    obsMatrix[i][j] = numer/denom;
                }
            }
        }

        public void gamma(){
            int T = sequence.length;
            int N = initMatrix.length;
            digamma = new double[T][N][N];
            gamma = new double[T][N];
            for(int t = 0; t < T-1; t++){
                double denom = 0;
                for(int j = 0; j < N; j++){
                    for(int i = 0; i < N; i++){
                        denom += alphaMatrix[t][j] * stateMatrix[j][i] * obsMatrix[i][sequence[t+1]] * betaMatrix[t+1][i];
                    }
                }
                for(int j = 0; j < N; j++){
                    for(int i = 0; i < N; i++){
                        digamma[t][j][i] = (alphaMatrix[t][j] * stateMatrix[j][i] * obsMatrix[i][sequence[t+1]] * betaMatrix[t+1][i]) / denom;
                        gamma[t][j] += digamma[t][j][i];
                    }
                }

                //Special case for gamma_T-1(i)
                denom = 0;
                for(int i = 0; i < N; i++){
                    denom += alphaMatrix[T-1][i];
                }
                for(int i = 0; i < N; i++){
                    gamma[T-1][i] = alphaMatrix[T-1][i]/denom;
                }
            }
        }

        public void beta(){
            int betalength = initMatrix.length;
            int sequenceLength = sequence.length;

            betaMatrix = new double[sequenceLength][betalength];

            //Beta_T-1(i) scaled
            int T = sequenceLength-1; //T is the last timestep
            for(int i = 0; i < betalength; i++){
                betaMatrix[T][i] = c[T];
            }

            //Looping (sum of alpha_t-1 [i] + a[i][j]) * b_i(O_t)

            for(int t = T-1; t >= 0; t--){
                for(int j = 0; j < betalength; j++){
                    double currentsum = 0;
                    for(int i = 0; i < betalength; i++){
                        currentsum += stateMatrix[j][i] * obsMatrix[i][sequence[t+1]] * betaMatrix[t+1][i];
                    }
                    betaMatrix[t][j] = currentsum * c[t];
                }
            }
        }

        public void alpha(){
            int alphalength = initMatrix.length;
            int sequenceLength = sequence.length;
            c = new double[sequenceLength];
            c[0] = 0;

            alphaMatrix = new double[sequenceLength][alphalength];

            for(int i = 0; i < alphalength; i++){
                alphaMatrix[0][i] = initMatrix[i] * obsMatrix[i][sequence[0]];
                c[0] = c[0] + alphaMatrix[0][i];
            }

            //Scale alpha 0
            c[0] = 1.0/c[0];
            for(int i = 0; i < alphalength; i++){
                alphaMatrix[0][i] = c[0] * alphaMatrix[0][i];
            }


            //Looping (sum of alpha_t-1 [i] + a[i][j]) * b_i(O_t)

            for(int t = 1; t < sequenceLength; t++){
                c[t] = 0;

                for(int j = 0; j < alphalength; j++){
                    double currentsum = 0;
                    for(int i = 0; i < alphalength; i++){
                        currentsum += alphaMatrix[t-1][i] * stateMatrix[i][j];
                    }
                    int observation = sequence[t];
                    alphaMatrix[t][j] = currentsum * obsMatrix[j][observation];
                    c[t] = c[t] + alphaMatrix[t][j];
                }

                //Scale alpha_t[i]
                c[t] = 1.0/c[t];
                for(int i = 0; i < alphalength; i++){
                    alphaMatrix[t][i] = c[t] * alphaMatrix[t][i];
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
