package MST;

import java.util.*;

public class Prim {
    static final int N = 999;
    static final int INF = 100000;
    static int n, m, cost;
    static List<List<int[]>> g = new ArrayList<>();
    static boolean[] vis = new boolean[N];
    static int[] dist = new int[N];

    public static void primMST(int source) {
        Arrays.fill(dist, INF);
        dist[source] = 0;

        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[0] - b[0]);
        pq.offer(new int[] { 0, source });

        while (!pq.isEmpty()) {
            int[] x = pq.poll();
            int w = x[0];
            int u = x[1];
            if (vis[u])

                continue;

            vis[u] = true;
            cost += w;

            for (int[] i : g.get(u)) {
                if (vis[i[0]])

                    continue;
                if (dist[i[0]] > i[1]) {
                    dist[i[0]] = i[1];
                    pq.offer(new int[] { dist[i[0]], i[0] });
                }
            }
        }

    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        n = sc.nextInt();
        m = sc.nextInt();
      
        for (int i = 0; i <= n; i++) {
            g.add(new ArrayList<>());
        }

        for (int i = 0; i < m; i++) {
            int w = sc.nextInt();
            int u = sc.nextInt();
            int v = sc.nextInt();
            g.get(u).add(new int[] { v, w });
            g.get(v).add(new int[] { u, w });
        }
        sc.close();

        primMST(1);
        System.out.println(cost);
    }
}
