package uet.oop.bomberman.controller;

import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.graphics.Sprite;

import java.util.*;

public class Graph {
    private final int numOfVerticess;
    private final List<Vertices> verticesList;
    private final List<Integer>[] adj;

    public Graph(List<Vertices> verticesList) {
        this.verticesList = verticesList;
        numOfVerticess = verticesList.size();
        adj = new List[numOfVerticess];
        for (int i = 0; i < numOfVerticess; i++) {
            adj[i] = new ArrayList<>();
        }
    }

    public void addAdjVertices(int v1, int v2) {
        adj[v1].add(v2);
        adj[v2].add(v1);
    }

    public List<Vertices> BFS(int start, int end) {
        boolean[] marked = new boolean[numOfVerticess];
        int[] trace = new int[numOfVerticess];

        for (boolean i : marked) {
            i = false;
        }
        marked[start] = true;

        Queue<Integer> q = new LinkedList();
        q.add(start);

        while (!q.isEmpty()) {
            int u = q.poll();
            for (int i = 0; i < adj[u].size(); i++) {
                int v = adj[u].get(i);
                if (!marked[v]) {
                    q.add(v);
                    marked[v] = true;
                    trace[v] = u;
                }
            }
        }

        if (!marked[end]) return null;

        List<Integer> path = new ArrayList<>();
        while (true) {
            path.add(end);
            if (end == start) {
                if (path.size() == 1) path.add(start);
                break;
            }
            end = trace[end];
        }
        Collections.reverse(path);

        List<Vertices> verticesPathList = new ArrayList<>();
        for (int i : path) verticesPathList.add(verticesList.get(i));
        return verticesPathList;

    }

    public static int getVerticesIndex(int xTile, int yTile) {
        int x = xTile / Sprite.SCALED_SIZE;
        int y = yTile / Sprite.SCALED_SIZE;
        return y * BombermanGame.WINDOW_WIDTH + x;
    }
}
