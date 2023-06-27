package LIS_LCS;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class CommonSubsequences {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        String s1 = input.nextLine();
        String s2 = input.nextLine();
        input.close();
        // String s1 = "AGGTAB";ACDF
        // String s2 = "GXTXAYB";ABCDEF

        Set<String> commonSubsequences = findCommonSubsequences(s1, s2);

        List<String> sortedSubsequences = new ArrayList<>(commonSubsequences);
        Collections.sort(sortedSubsequences, (a, b) -> b.length() - a.length());

        if (sortedSubsequences.isEmpty()) {
            System.out.println("No common subsequences found.");
        } else {
            System.out.println("Common Subsequences (in descending order of lengths):");
            for (String subsequence : sortedSubsequences) {
                System.out.println(subsequence);
            }
        }
    }

    public static Set<String> findCommonSubsequences(String s1, String s2) {
        int m = s1.length();
        int n = s2.length();

        Set<String>[][] dp = new HashSet[m + 1][n + 1];

        // Initialize the table
        for (int i = 0; i <= m; i++) {
            for (int j = 0; j <= n; j++) {
                dp[i][j] = new HashSet<>();
            }
        }
        // Build the table
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                    for (String subseq : dp[i - 1][j - 1]) {
                        dp[i][j].add(subseq + s1.charAt(i - 1));
                    }
                    dp[i][j].add(String.valueOf(s1.charAt(i - 1)));
                } else {
                    dp[i][j].addAll(dp[i - 1][j]);
                    dp[i][j].addAll(dp[i][j - 1]);
                }
            }
        }
        return dp[m][n];
    }
}
