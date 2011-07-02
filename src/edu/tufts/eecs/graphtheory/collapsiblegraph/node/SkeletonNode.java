/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.tufts.eecs.graphtheory.collapsiblegraph.node;

import edu.tufts.eecs.graphtheory.collapsiblegraph.edge.Edge;
import java.io.Serializable;
import java.util.Set;

/**
 *
 * @author Jeremy
 */
public class SkeletonNode implements Node, Serializable {
    private String name;
    private Set<Edge> incomingEdgeSet;
    private Set<Edge> outgoingEdgeSet;

    public SkeletonNode(String name) {
        this.name = name;
    }

    public void setOutgoingEdgeSet(Set<Edge> outgoingEdgeSet) {
        this.outgoingEdgeSet = outgoingEdgeSet;
    }

    public Set<Edge> getOutgoingEdgeSet() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Set<Edge> getIncomingEdgeSet() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getName() {
        return name;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SkeletonNode other = (SkeletonNode) obj;
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return name;
    }

    public double getDistance(Node otherNode) {
        return (double) getLevenshteinDistance(this.getName(), otherNode.getName());
    }

    public static int getLevenshteinDistance(String s, String t) {
        if (s == null || t == null) {
            throw new IllegalArgumentException("Strings must not be null");
        }

        /*
        The difference between this impl. and the previous is that, rather
        than creating and retaining a matrix of size s.length()+1 by t.length()+1,
        we maintain two single-dimensional arrays of length s.length()+1.  The first, d,
        is the 'current working' distance array that maintains the newest distance cost
        counts as we iterate through the characters of String s.  Each time we increment
        the index of String t we are comparing, d is copied to p, the second int[].  Doing so
        allows us to retain the previous cost counts as required by the algorithm (taking
        the minimum of the cost count to the left, up one, and diagonally up and to the left
        of the current cost count being calculated).  (Note that the arrays aren't really
        copied anymore, just switched...this is clearly much better than cloning an array
        or doing a System.arraycopy() each time  through the outer loop.)

        Effectively, the difference between the two implementations is this one does not
        cause an out of memory condition when calculating the LD over two very large strings.
         */

        int n = s.length(); // length of s
        int m = t.length(); // length of t

        if (n == 0) {
            return m;
        } else if (m == 0) {
            return n;
        }

        int p[] = new int[n + 1]; //'previous' cost array, horizontally
        int d[] = new int[n + 1]; // cost array, horizontally
        int _d[]; //placeholder to assist in swapping p and d

        // indexes into strings s and t
        int i; // iterates through s
        int j; // iterates through t

        char t_j; // jth character of t

        int cost; // cost

        for (i = 0; i <= n; i++) {
            p[i] = i;
        }

        for (j = 1; j <= m; j++) {
            t_j = t.charAt(j - 1);
            d[0] = j;

            for (i = 1; i <= n; i++) {
                cost = s.charAt(i - 1) == t_j ? 0 : 1;
                // minimum of cell to the left+1, to the top+1, diagonally left and up +cost
                d[i] = Math.min(Math.min(d[i - 1] + 1, p[i] + 1), p[i - 1] + cost);
            }

            // copy current distance counts to 'previous row' distance counts
            _d = p;
            p = d;
            d = _d;
        }

        // our last action in the above loop was to switch d and p, so p now
        // actually has the most recent cost counts
        return p[n];
    }
}
