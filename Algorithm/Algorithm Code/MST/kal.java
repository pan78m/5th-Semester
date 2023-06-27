package MST;

import java.util.*;
public class kal {
    static final int N = 9999;
    static Vector<Integer> p = new Vector<Integer>(N + 1);

    // every vertex is parent initialized
    static void MakeSet(int v) {
        p.set(v, v);
    }

    // Check parent same or not??
    static int FindSet(int v) {
        if (v == p.get(v))
            return v;
        return p.set(v, FindSet(p.get(v)));
    }

    // ALL parent are making same?
    static void UnionSet(int a, int b) {
        int x = FindSet(a);
        int y = FindSet(b);
        if (x != y)
            p.set(y, x);
    }

    public static void main(String[] args) {
        int cost = 0;
        Vector<Vector<Integer>> edges = new Vector<Vector<Integer>>();
        Scanner input = new Scanner(System.in);
        int vertex = input.nextInt();
        int e = input.nextInt();
        for (int i = 0; i <= N; i++) {
            p.add(i);
            MakeSet(i);
        }
        for (int i = 0; i < e; i++) {
            Vector<Integer> ed = new Vector<Integer>();
            int w = input.nextInt();
            int u = input.nextInt();
            int v = input.nextInt();
            ed.add(w);
            ed.add(u);
            ed.add(v);
            edges.add(ed);
        }
        Collections.sort(edges, new Comparator<Vector<Integer>>() {
            @Override
            public int compare(Vector<Integer> a, Vector<Integer> b) {
                return a.get(0) - b.get(0);
            }
        });
         int Cycle=0;
         boolean flag=false;
        for (Vector<Integer> i : edges) {
            int w = i.get(0);
            int u = i.get(1);
            int v = i.get(2);
            int x = FindSet(u);
            int y = FindSet(v);
            if (x == y) {
                Cycle++;
                flag = true;
                continue;
            } else {
                cost += w;
                System.out.println("Weight: "+ w +" Cost :"+cost);
                UnionSet(u, v);
            }
        }
            
        if(flag){
            System.out.println("How many Cycle exit in the graph: "+Cycle);
        }
        System.out.println("Minimum Cost: " + cost);
    }

}

/* 
8 9
1 2 5
2 3 6
4 3 2
1 4 9
3 5 5
5 6 10
6 7 7
7 8 1
8 5 1


8 9
5 1 2 
6 2 3 
2 4 3 
9 1 4 
5 3 5 
10 5 6 
7 6 7 
1 7 8 
1 8 5 

   2    3
(0)--(1)--(2)
 |   / \   |
6| 8/   \5 |7
 | /     \ |
(3)-------(4)
      9    
5 7      
0 2 1
1 3 2
0 6 3
3 9 4
1 8 3
1 5 4
2 7 4   

5 7      
2 0 1
3 1 2
6 0 3
9 3 4
8 1 3
5 1 4
7 2 4 
      


      */

