package KnapSack;

public class Knapsack_0_1 {
    public static void main(String[] args) {
        // int p[] = {  1, 2, 3 };
        // int wt[] = { 4, 5,1 };
        int p[] = { 0, 1, 2, 5, 6 };
        int wt[] = { 0, 2, 3, 4, 5 };
        // int p[] = { 0, 1, 1 };
        // int wt[] = { 0, 2, 1 };
        // int m = 3, n = 2, w=0, ob;
        int m = 8, n = 4, w=0, ob;
        int k[][] = new  int[5][9];
        for (ob = 0; ob <= n; ob++) {
            for (w = 0; w <= m; w++) {
                if (w == 0 || ob == 0)
                    k[ob][w] = 0;
                else if (wt[ob] <= w)
                    k[ob][w] = Math.max(p[ob] + k[ob - 1][w - wt[ob]], k[ob - 1][w]);
                else
                    k[ob][w] = k[ob - 1][w];

                System.out.print(k[ob][w] + " ");
            }
            System.out.println();
        }
        //System.out.println("\nMaximum profit: "+k[ob-1][w-1]);
        System.out.println("\nMaximum profit: "+k[n][m]);
        int i = n, j = m;
        System.out.println("\nSelect the object: ");
        while (i >0 && j >= 0) {
            if (k[i][j] == k[i - 1][j]) {
                System.out.println(i + " =0");
                i--;

            } else {
               // System.out.println(j+"??");
                System.out.println(i + " =1");
                j = j - wt[i];
               // System.out.println(j+"?");
                i--;

            }

        }
    }
}
