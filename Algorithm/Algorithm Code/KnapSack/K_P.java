package KnapSack;

public class K_P {
    public static void main(String[] args) {
        // Input: N = 2, W = 3
        // val[] = {1, 1}
        // wt[] = {2, 1}
        // int P[] = { 0,1, 4, 5, 7 };
        // int w[] = { 0, 1, 3, 4, 5 };
        int P[] = { 0, 1, 1 };
        int w[] = { 0, 2, 1 };
        // int m = 8;
        // int n = 4;
        int m = 3;
        int n = 2;
        int T[][] = new int[n + 1][m + 1];
        System.out.println("Store the element of table using Table method ");
        Table(n, m, T, w, P);

        /// Which object Selected
        Selected(n, m, T, w);
    }

    private static void Table(int N, int W, int[][] T, int[] w, int[] P) {
        int weight=1, object;
        for (object = 1; object <= N; object++) {
            for (weight = 1; weight <= W; weight++) {
                if (weight == 0 || object == 0)
                    T[object][weight] = 0;
                else if (w[object] <= weight) {
                    int maxProfit = 0;
                    for (int repetitions = 1; repetitions <= weight / w[object]; repetitions++) {
                        maxProfit = Math.max(maxProfit, T[object - 1][weight - repetitions * w[object]] + repetitions * P[object]);
                    }
                    T[object][weight] = Math.max(T[object - 1][weight], maxProfit);
                } else
                    T[object][weight] = T[object - 1][weight];

                System.out.print(T[object][weight] + " ");
            }
            System.out.println();
        }

        System.out.print("\nMaximum Profit: " + T[object - 1][weight - 1] + " ");
    }

    private static void Selected(int n, int m, int[][] T, int[] w) {
        int i = n, j = m;
        System.out.println("\nHere 1 means Selected and 0 means Deselected ");
        while (i > 0 && j > 0) {
            if (T[i][j] != T[i - 1][j]) {
                System.out.println("Object:" + i + " = 1");
                j = j - w[i];
                i--;
            } else {
                System.out.println("Object:" + i + " = 0");
                i--;
            }
        }
    }
    
}

/*
 * class Solution{
    static int knapSack(int N, int W, int val[], int wt[])
    {
       int[][] dp = new int[N + 1][W + 1];

        for (int i = 1; i <= N; i++) {
            for (int w = 1; w <= W; w++) {
                if (wt[i - 1] <= w) {
                    dp[i][w] = Math.max(dp[i - 1][w], val[i - 1] + dp[i][w - wt[i - 1]]);
                } else {
                    dp[i][w] = dp[i - 1][w];
                }
            }
        }
       
        return dp[N][W];
    }
}
 */
