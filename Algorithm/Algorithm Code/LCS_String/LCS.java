package LIS_LCS;



public class LCS {
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
        // Which element are selected
        StringBuilder lcsBuilder = new StringBuilder();
        while (i > 0 && j > 0) {
            if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                lcsBuilder.insert(0, s2.charAt(j - 1)); // Append character to the LCS
                // lcsBuilder.insert(0, s1.charAt(i - 1)); // Append character to the LCS
                i--;
                j--;
            } else if (dp[i][j - 1] > dp[i - 1][j]) {
                j--;
            } else {
                i--;
            }
        }
        /*
         * List<String> commonSequences = new ArrayList<>();
         * StringBuilder lcsBuilder = new StringBuilder();
         * 
         * while (i > 0 && j > 0) {
         * if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
         * lcsBuilder.insert(0, s2.charAt(j - 1));
         * i--;
         * j--;
         * } else if (dp[i][j - 1] > dp[i - 1][j]) {
         * j--;
         * } else if (dp[i][j - 1] < dp[i - 1][j]) {
         * i--;
         * } else {
         * // Found a branch, continue exploring both options
         * 
         * // Clone the StringBuilder to preserve the current LCS
         * StringBuilder newLCSBuilder = new StringBuilder(lcsBuilder);
         * 
         * // Explore the option by decrementing j
         * lcsBuilder.insert(0, s2.charAt(j - 1));
         * j--;
         * 
         * // Explore the option by decrementing i
         * newLCSBuilder.insert(0, s1.charAt(i - 1));
         * i--;
         * 
         * // Add both options to the list of common sequences
         * commonSequences.add(lcsBuilder.toString());
         * commonSequences.add(newLCSBuilder.toString());
         * }
         * }
         * 
         * // If there is still any remaining part of s1 or s2, add it to the common
         * sequences
         * while (i > 0) {
         * lcsBuilder.insert(0, s1.charAt(i - 1));
         * i--;
         * }
         * while (j > 0) {
         * lcsBuilder.insert(0, s2.charAt(j - 1));
         * j--;
         * }
         * 
         * // Add the final LCS to the list of common sequences
         * commonSequences.add(lcsBuilder.toString());
         * 
         * System.out.println("Common Sequences:");
         * for (String sequence : commonSequences) {
         * System.out.println(sequence);
         * }
         */

        System.out.println("Longest Common Subsequence: " + lcsBuilder.toString());

        return dp[m][n];

    }

    public static void main(String[] args) {
        // String s1 = "ABCDEFGH";
        // String s2 ="abcdefgh";
        // String s1 = "ACADB";
        // String s2 = "CBDA";
        String s1 = "AGGTAB";
        String s2 = "GXTXAYB";
        System.out.println("Length of LCS is " + lcs(s1, s2));
    }
}
/*
 * The lcs method takes two strings s1 and s2 as input and returns the length of
 * their longest common subsequence. The algorithm uses a two-dimensional array
 * dp to store the lengths of LCS for all prefixes of s1 and s2. The entry
 * dp[i][j] represents the length of LCS for the prefixes s1.substring(0, i) and
 * s2.substring(0, j).
 * 
 * The algorithm initializes the first row and column of dp to zero, since the
 * LCS of an empty string and any other string is zero. It then iterates through
 * the remaining entries of dp, computing the LCS for longer and longer prefixes
 * of s1 and s2. If the current characters of s1 and s2 match, the LCS length
 * for the current prefixes is increased by one. Otherwise, the LCS length for
 * the current prefixes is taken as the maximum of the LCS lengths for the
 * prefixes obtained by removing the last character of s1 and s2.
 * 
 * Finally, the algorithm returns dp[m][n], where m and n are the lengths of s1
 * and s2, respectively. This represents the length of the LCS for the entire
 * strings s1 and s2. In the example main method, we compute the LCS for the
 * strings "AGGTAB" and "GXTXAYB" and print the result, which is 4.
 * 
 * 
 */
