package ShortestPath;

import java.util.*;

public class CheapestPathWithStops {
    static final int INF = 100000;

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        System.out.println("Hello World!!\n");

        int n, m;
        n = input.nextInt();
        m = input.nextInt();

        List<Pair> dist = new ArrayList<>(n + 1);
        for (int i = 0; i <= n; i++) {
            dist.add(new Pair(INF, 0));
        }

        List<List<Pair>> graph = new ArrayList<>();
        for (int i = 0; i <= n; i++) {
            graph.add(new ArrayList<>());
        }

        for (int i = 0; i < m; i++) {
            int u, v, w;
            u = input.nextInt();
            v = input.nextInt();
            w = input.nextInt();
            graph.get(u).add(new Pair(v, w));
            graph.get(v).add(new Pair(u, w));
        }

        int s, dst, K;
        s = input.nextInt();
        dst = input.nextInt();
        K = input.nextInt();
        dist.set(s, new Pair(0, 1));

        TreeSet<Triple> set = new TreeSet<>();
        for (int i = 1; i <= n; i++) {
            if (dist.get(i).x < INF)
                System.out.print(dist.get(i).x + "\t");
            else
                System.out.print(-1 + "\t");
        }
        System.out.println();

        set.add(new Triple(0, 1, s));

        while (!set.isEmpty()) {
            Triple x = set.pollFirst();
            int node = x.z;
            int cost = x.x;
            int stops = x.y;

            if (stops > K)
                continue;

            for (Pair i : graph.get(node)) {
                if (dist.get(i.x).x > cost + i.y) {
                    set.remove(new Triple(dist.get(i.x).x, dist.get(i.x).y, i.x));
                    dist.set(i.x, new Pair(cost + i.y, stops + 1));
                    set.add(new Triple(dist.get(i.x).x, dist.get(i.x).y, i.x));
                }
            }

            for (int i = 1; i <= n; i++) {
                if (dist.get(i).x < INF)
                    System.out.print(dist.get(i).x + "\t");
                else
                    System.out.print(-1 + "\t");
            }
            System.out.println();
        }

        System.out.println("Cheapest cost path with at most " + K + " stops from source " + s + " to destination " + dst + " is: ");
        if (dist.get(dst).x < INF)
            System.out.println(dist.get(dst).x);
        else
            System.out.println("No path found.");

        input.close();
    }

    static class Pair {
        int x;
        int y;

        public Pair(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    static class Triple implements Comparable<Triple> {
        int x;
        int y;
        int z;

        public Triple(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public int compareTo(Triple other) {
            if (this.x != other.x)
                return Integer.compare(this.x, other.x);
            else if (this.y != other.y)
                return Integer.compare(this.y, other.y);
            else
                return Integer.compare(this.z, other.z);
        }
    }
}

