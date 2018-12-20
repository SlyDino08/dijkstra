import java.io.*;
import java.util.*;

public class Dijkstra {

	private class Item implements Comparable {
		// Used for PriorityQueue
		private int distance;
		private int node;
		private int last;

		private Item(int d, int x, int y) {
			distance = d;
			node = x;
			last = y;
		}

		public int compareTo(Object z) {
			return distance - ((Item) z).distance;
		}
	}

	private class Vertex {
		private EdgeNode edges1;
		private EdgeNode edges2;

		private Vertex() {
			edges1 = null;
			edges2 = null;
		}

		private void addEdge1(EdgeNode i) {
			i.next1 = edges1;
			edges1 = i;
		}

		private void addEdge2(EdgeNode i) {
			i.next2 = edges2;
			edges2 = i;
		}
	}

	private class EdgeNode {
		private int vertex1;
		private int vertex2;
		private EdgeNode next1;
		private EdgeNode next2;
		private int weight;

		private EdgeNode(int v1, int v2, EdgeNode e1, EdgeNode e2, int w) {
			// PRE: v1 < v2
			vertex1 = v1;
			vertex2 = v2;
			next1 = e1;
			next2 = e2;
			weight = w;
		}
	}

	private Vertex[] g;

	public Dijkstra(int size) {
		g = new Vertex[size];
		for (int i = 0; i < size; i++) {
			g[i] = new Vertex();
		}

	}

	public void addEdge(int v1, int v2, int w) {
		// PRE: v1 and v2 are legitimate vertices
		// (i.e. 0 <= v1 < g.length and 0 <= v2 < g.length

		// System.out.println("vertex1: " + v1 + " vertex2: " + v2 + " weight: " + w);
		EdgeNode a = new EdgeNode(v1, v2, null, null, w);
		g[v1].addEdge1(a);
		g[v2].addEdge2(a);
	}

	public Item[] getPaths(int j) {

		// instance variables
		PriorityQueue<Item> p = new PriorityQueue<>();
		int paths = 0;
		Item[] items = new Item[g.length];
		byte[] visited = new byte[g.length];
		for (int k = 0; k < visited.length; k++) {
			visited[k] = 0;
		}
		p.add(new Item(0, j, -1));

		// while we havent found all the paths
		while (paths < g.length) {

			// remove items from queue we have already visited
			while (visited[p.peek().node] != 0) {
				p.remove();
			}

			// get item to visit
			Item i = p.poll();
			visited[i.node] = 1;
			paths++;

			// item is now known
			items[i.node] = i;

			// add all edgeNodes from edges1 and edges2 from a vertex to queue
			EdgeNode n = g[i.node].edges1;
			while (n != null) {
				if (visited[n.vertex2] == 0) {
					p.add(new Item(i.distance + n.weight, n.vertex2, i.node));
				}
				n = n.next1;
			}
			n = g[i.node].edges2;
			while (n != null) {
				if (visited[n.vertex1] == 0) {
					p.add(new Item(i.distance + n.weight, n.vertex1, i.node));
				}
				n = n.next2;
			}

		}
		return items;
	}

	public void printRoutes(int j) {
		// find and print the best routes from j to all other nodes in the graph

		// get paths
		Item[] items = getPaths(j);

		// print all routes

		String result = "";
		for (int i = 0; i < items.length; i++) {
			int l = i;
			while (items[l].last != -1) {
				result = items[l].node + " " + result;
				l = items[l].last;
			}
			result = j + " " + result;
			System.out.println("shortest path from " + j + " to " + items[i].node + " is " + result
					+ " with a distance of: " + items[i].distance);
			result = "";
		}

	}

	public static void main(String args[]) throws IOException {
		BufferedReader b = new BufferedReader(new FileReader(args[0]));
		String line = b.readLine();
		int numNodes = new Integer(line);
		line = b.readLine();
		int source = new Integer(line);
		System.out.println(source);
		Dijkstra g = new Dijkstra(numNodes);
		line = b.readLine();
		while (line != null) {
			Scanner scan = new Scanner(line);
			g.addEdge(scan.nextInt(), scan.nextInt(), scan.nextInt());
			line = b.readLine();
		}
		g.printRoutes(source);
	}
}