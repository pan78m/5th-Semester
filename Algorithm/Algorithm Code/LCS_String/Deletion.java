package Pattern_Matching;

import java.util.Scanner;

public class Deletion {
    public static int lcs(String s1, String s2) {
        int m = s1.length();
        int n = s2.length();

        int[][] dp = new int[m + 1][n + 1];

        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    dp[i][j] = Math.max(dp[i][j - 1], dp[i - 1][j]);
                }
                System.out.print(dp[i][j] + " ");
            }
            System.out.println();
        }
        int i = m, j = n;
        int mismatchCount = 0;
        while (i > 0 && j > 0) {
            if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                i--;
                j--;
            } else if (dp[i][j - 1] > dp[i - 1][j]) {
                mismatchCount++;
                j--;
            } else {
                mismatchCount++;
                i--;
                j--;
            }
        }

        System.out.println("Mismatched character count: " + mismatchCount);

        return dp[m][n];
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        String s1 = input.nextLine();
        String s2 = input.nextLine();
        System.out.println("Length of LCS is " + lcs(s1, s2));
    }
}
