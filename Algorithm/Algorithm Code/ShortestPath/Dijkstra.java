package ShortestPath;

import java.util.*;

public class Dijkstra {
    static final int INF = 100000;

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        System.out.println("Hello Word!!");

        int n, m;
        n = input.nextInt();
        m = input.nextInt();
       

        // Initialize the distances array
        int[] dist = new int[n + 1];
        Arrays.fill(dist, INF);

        List<List<Pair>> graph = new ArrayList<>();
        for (int i = 0; i <= n; i++) {
            graph.add(new ArrayList<>());
        }

        for (int i = 0; i < m; i++) {
            int u, v, w;
            u = input.nextInt();
            v = input.nextInt();
            w = input.nextInt();
            graph.get(u).add(new Pair(v, w)); // single destination shortest path??
                                // je graph use korbo tar direction change kore dibo tahole seta single destination shortest path
                                // hoye jabe
            graph.get(v).add(new Pair(u, w));
        }

        int s; //when use signle destination shortest path use here destination to find the and other node which is indicat
        //line  80 after dijkstra use then print this  System.out.println(dist[4]);
        s = input.nextInt();
        dist[s] = 0;

        TreeSet<Pair> set = new TreeSet<>();
 // print the table first iteration start
        for (int i = 1; i <= n; i++) {
            if (dist[i] < INF)
                System.out.print(dist[i] + "\t");
            else
                System.out.print(-1 + "\t");
        }
        System.out.println();
     // print the table first iteration end

        set.add(new Pair(0, s));

        while (!set.isEmpty()) {
            Pair x = set.pollFirst();
            int u = x.y;

            for (Pair i : graph.get(u)) {
                if (dist[i.x] > dist[u] + i.y) {
                    set.remove(new Pair(dist[i.x], i.x));
                    dist[i.x] = dist[u] + i.y;
                    set.add(new Pair(dist[i.x], i.x));
                }
            }

            // print the table others iteration start
            for (int i = 1; i <= n; i++) {
                if (dist[i] < INF)
                    System.out.print(dist[i] + "\t");
                else
                    System.out.print(-1 + "\t");
            }
            System.out.println();
             // print the table others iteration end
        }

        // single pair shortest path
        System.out.println(dist[4]);

        System.out.println("Single Source shortest path output");
        //  cout << "\nSingle Destination shortest path output\n";// jodi graph directed hoy and direction chage kore dei tahole eta single destinatio
    //destination shorest path hobe
        for (int i = 1; i <= n; i++) {
            System.out.print(dist[i] + "  ");
        }

        input.close();
    }

    static class Pair implements Comparable<Pair> {
        int x;
        int y;

        public Pair(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int compareTo(Pair other) {
            if (this.x != other.x)
                return Integer.compare(this.x, other.x);
            else
                return Integer.compare(this.y, other.y);
        }
    }
}
