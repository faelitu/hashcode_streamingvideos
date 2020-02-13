/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package youtubeopt;

import ilog.concert.IloException;
import ilog.concert.IloIntVar;
import ilog.concert.IloLinearIntExpr;
import ilog.concert.IloLinearNumExpr;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author vilela
 */
public class YoutubeOpt {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException, IOException, IloException {
        String filePath = "kittens.in.txt";
        File file = new File(filePath);

        BufferedReader br = new BufferedReader(new FileReader(file));

        String st = br.readLine();
        Integer V = Integer.parseInt(st.split(" ")[0]);
        Integer E = Integer.parseInt(st.split(" ")[1]);
        Integer R = Integer.parseInt(st.split(" ")[2]);
        Integer C = Integer.parseInt(st.split(" ")[3]);
        Integer CP = Integer.parseInt(st.split(" ")[4]);
        Double BM = 999999.0;

        Integer SV[] = new Integer[V];
        st = br.readLine();
        int i = 0;
        int j = 0;
        int k = 0;
        for (String size : st.split(" ")) {
            SV[i] = Integer.parseInt(size);
            i++;
        }
        Integer CParr[] = new Integer[C + 1];
        for (i = 0; i < C; i++) {
            CParr[i] = CP;
        }
        CParr[C] = Integer.MAX_VALUE;
        Integer KC[][] = new Integer[E][C + 1];
        for (i = 0; i < E; i++) {
            for (j = 0; j < C; j++) {
                KC[i][j] = 5000;
            }
        }
        for (i = 0; i < E; i++) {
            st = br.readLine();
            int K = Integer.parseInt(st.split(" ")[1]);
            KC[i][C] = Integer.parseInt(st.split(" ")[0]);
            for (j = 0; j < K; j++) {
                st = br.readLine();
                int c = Integer.parseInt(st.split(" ")[0]);
                KC[i][c] = Integer.parseInt(st.split(" ")[1]);
            }
        }
        Integer A[][] = new Integer[V][E];
        for (i = 0; i < V; i++) {
            for (j = 0; j < E; j++) {
                A[i][j] = 0;
            }

        }
//        Integer Rn[] = new Integer[R];
//        Integer Rv[] = new Integer[R];
//        Integer Re[] = new Integer[R];
        for (i = 0; i < R; i++) {
            st = br.readLine();
            int v = Integer.parseInt(st.split(" ")[0]);
            int e = Integer.parseInt(st.split(" ")[1]);
            A[v][e] = Integer.parseInt(st.split(" ")[2]);
//            Rn[i] = Integer.parseInt(st.split(" ")[2]);
//            Re[i] = Integer.parseInt(st.split(" ")[1]);
//            Rv[i] = Integer.parseInt(st.split(" ")[0]);
        }

        br.close();
        IloCplex model = new IloCplex();

        IloIntVar x[][] = new IloIntVar[V][C + 1];
        for (j = 0; j < V; j++) {
            for (k = 0; k < C + 1; k++) {
                x[j][k] = model.intVar(0, 1);
            }
        }

        IloLinearNumExpr obj = model.linearNumExpr();
        for (i = 0; i < E; i++) {
            for (j = 0; j < V; j++) {
                for (k = 0; k < C + 1; k++) {

                    obj.addTerm(x[j][k], A[j][i] * KC[i][k] * SV[j]);
                }

            }
        }
        A = null;
        KC = null;
                //Rest 3
        for (k = 0; k < C + 1; k++) {
            IloLinearNumExpr exp = model.linearNumExpr();
            for (j = 0; j < V; j++) {
                exp.addTerm(x[j][k], SV[j]);
            }
            model.addLe(exp, CParr[k]);
        }
        SV = null; 
        CParr = null;
        for (j = 0; j < V; j++) {
            IloLinearNumExpr exp = model.linearNumExpr();

            for (k = 0; k < C + 1; k++) {
                exp.addTerm(x[j][k], 1);
            }
            model.addGe(exp, 1);

        }

        System.out.println("rest 3");

        //OBJ
        model.addMinimize(obj);
        model.setParam(IloCplex.Param.MIP.Tolerances.MIPGap, 0.1);
        System.gc();
        if (model.solve()) {
//            double sumTS = 0;
//            double sumRD = 0;
//            for (i = 0; i < R; i++){
//                int mL = KC[Re[i]][C];
//                for (k = 0; k < C+1; k++) {
//                    if((KC[Re[i]][k] < mL) && (KC[Re[i]][k] != 5000) && (model.getValue(x[Rv[i]][k]) == 1)){
//                        mL = KC[Re[i]][k];
//                    }
//                }
//                sumTS += Rn[i] * (KC[Re[i]][C] - mL);
//                sumRD += Rn[i];
//            }
//            
//            Double score = (sumTS*1000)/sumRD;
//            System.out.println(score);
            System.out.println(model.getObjValue());
        } else {
            System.out.println("fodeu");
        }
        // TODO code application logic here
    }

}