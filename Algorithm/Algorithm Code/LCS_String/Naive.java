package Pattern_Matching;

public class Naive {
    public static void main(String[] args) {
        //using interger
        // int[] arr1 = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        // int[] arr2 = {5, 6};
        String s1 = "this test is a test text";
        String s2 = "test";
        search(s1, s2);
    }

    private static void search(String s1, String s2) {
        int n = s1.length();
        int m = s2.length();
        int flag = 0;
        for (int i = 0; i <= n - m; i++) {
            int j;
            for (j = 0; j < m; j++) {
                if (s1.charAt(i + j) != s2.charAt(j))// if (arr1[i + j] != arr2[j]) all are same
                    break;
            }
            if (j == m) {
                flag++;
                System.out.println("Pattern found at index[i:j] =["+i+":"+(j-1+i)+"]");

            }

        }
        if (flag == 0)
            System.out.println("Pattern not found!!!");
        else
            System.out.println("How many times found : " + flag);

    }
}
