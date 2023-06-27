import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;

class Node_Cost {
    private static final int INF = 100000;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int m = scanner.nextInt();

        List<List<Pair>> adj = new ArrayList<>();
        int[] node_cost = new int[n];

        for (int i = 0; i < n; i++) {
            node_cost[i] = scanner.nextInt();
            adj.add(new ArrayList<>());
        }

        for (int i = 0; i < m; i++) {
            int u = scanner.nextInt();
            int v = scanner.nextInt();
            int w = scanner.nextInt();
            u--;
            v--;
            adj.get(u).add(new Pair(v, w));
            adj.get(v).add(new Pair(u, w));
        }

        int[] dist = new int[n];
        boolean[] visited = new boolean[n];
        for (int i = 0; i < n; i++) {
            dist[i] = INF;
        }
        dist[0] = node_cost[0];

        PriorityQueue<Pair> pq = new PriorityQueue<>();
        pq.add(new Pair(dist[0], 0));

        while (!pq.isEmpty()) {
            int u = pq.poll().second;

            if (visited[u]) {
                continue;
            }
            visited[u] = true;

            for (Pair e : adj.get(u)) {
                int v = e.first;
                int w = e.second;

                if (dist[u] + w + node_cost[v] < dist[v]) {
                    dist[v] = dist[u] + w + node_cost[v];
                    pq.add(new Pair(dist[v], v));
                }
            }
        }

        System.out.println();
        for (int i = 0; i < n; i++) {
            System.out.print(dist[i] + " ");
        }
        System.out.println();
        System.out.println(dist[n - 1]);
    }

    static class Pair implements Comparable<Pair> {
        int first;
        int second;

        public Pair(int first, int second) {
            this.first = first;
            this.second = second;
        }

        @Override
        public int compareTo(Pair other) {
            return Integer.compare(this.first, other.first);
        }
    }
}

/*
6 8
1
2
3
4
5
6
1 2 2
1 3 4
2 4 7
3 5 3
4 6 1
5 4 2
5 6 5
2 3 1


*/