package edu.tufts.eecs.graphtheory.collapsiblegraph.windowing.graphlayout;

import edu.tufts.eecs.graphtheory.collapsiblegraph.clustering.ClusterDendrogramNode;
import edu.tufts.eecs.graphtheory.collapsiblegraph.clustering.DendrogramEdge;
import edu.tufts.eecs.graphtheory.collapsiblegraph.clustering.DendrogramNode;
import edu.tufts.eecs.graphtheory.collapsiblegraph.viewing.DendrogramSlice;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * A class to handle the arrangement of Graphs so that they are pleasant. It does so by imitating a 2 -d physical space in which
 * nodes repel each other and edges pull nodes together
 * @author Jeremy
 */
public class ForceDirectedLayoutGenerator {

    //constants to represent the min&max X&Y values for space that they may draw upon
    private static int MIN_X = 0;
    private static int MAX_X = 1024;
    private static int MIN_Y = 0;
    private static int MAX_Y = 500;
    
    //The constant DIAMETER at which nodes should be drawn.
    private static int DIAMETER = 20;
    //The distance at which edges prefer nodes to be from each other
    private static final int SPRING_EQUILIBRIUM_DISTANCE = 20;
    //A Bogus Hooke's constant that seems to work nicely 
    private static final float HOOKE_CONSTANT = 10f;
    //A bogus Coulomb constant that seems to work nicely 
    private static final float COULOMB_CONSTANT = 8987000000f / 100f;
    //This thing goes NUTS unless you dampen the velocity to 1% 
    private static final float VELOCITY_MODIFIER = .01f;
    //The amount that the velocity of each node should be decreased by per iteration
    private static final float MOMENTUM_DAMPENING_CONSTANT = .5f;
    
    //A random nubmer generatlr
    private static final Random generator = new Random();
    
    //A map linking DendrogramNodes to their containing ViewableDendrogramNode 
    private Map<DendrogramNode, ViewableDendrogramNode> nodeToViewable = new HashMap<DendrogramNode, ViewableDendrogramNode>();
    private List<ViewableDendrogramNode> vDNodes; //The ViewableDendrogramNodes that should be displayed
    private List<ViewableDendrogramEdge> vDEdges; //The ViewableDendrogramEdges that should be displayed
    
    
    private float kineticEnergy = 10000000; //The total amount of kinetic energy in the system.  
    private float previousKineticEnergy = 0;
    
    //How many iterations there've been on this current simulation
    private int iterations = 0;
    
    
    private static final boolean debug = false;

    public List<ViewableDendrogramNode> getGraphNodes() {
        return vDNodes;
    }

    public List<ViewableDendrogramEdge> getGraphEdges() {
        return vDEdges;
    }

    public void setupLayout(DendrogramSlice ds) {
        kineticEnergy = 10000f;
        vDNodes = new ArrayList<ViewableDendrogramNode>();
        vDEdges = new ArrayList<ViewableDendrogramEdge>();

        nodeToViewable.clear();
        DendrogramNode[] nodeSet = ds.getVisibleDendrogramNodes();
        DendrogramEdge[] edgeSet = ds.getVisibleDendrogramEdges();

       



        for (DendrogramNode thisNode : nodeSet) {
            int radius = DIAMETER / 2;
            int xCoord = generator.nextInt(MAX_X - radius) + radius;
            int yCoord = generator.nextInt(MAX_Y - radius) + radius;
            ViewableDendrogramNode newVDNode = new ViewableDendrogramNode(thisNode, DIAMETER, xCoord, yCoord);
            nodeToViewable.put(thisNode, newVDNode);
            vDNodes.add(newVDNode);
        }


        for (DendrogramEdge dEdge : edgeSet) {
            if (nodeToViewable.get(dEdge.getSourceDendrogramNode()) != null
                    && nodeToViewable.get(dEdge.getTargetDendrogramNode()) != null) {
                vDEdges.add(new ViewableDendrogramEdge(dEdge, nodeToViewable.get(dEdge.getSourceDendrogramNode()),
                        nodeToViewable.get(dEdge.getTargetDendrogramNode())));
            }
        }
        iterations = 0;
    }

    public void iterate() {
        if (kineticEnergy < 20f) {
            return;
        }
        previousKineticEnergy = kineticEnergy;
        kineticEnergy = 0f;
        if(debug) System.out.println("Iteration " + iterations);
        int node = 0;
        for (ViewableDendrogramNode vDNode : vDNodes) {
            if(debug) System.out.print(" node: " + node + "  ");
            vDNode.setXForce(0F);
            vDNode.setYForce(0F);
            if(debug) System.out.print(" x:" + vDNode.getXCoordinate() + "  y:" + vDNode.getYCoordinate());
            for (ViewableDendrogramNode otherVDNode : vDNodes) {
                if (vDNode != otherVDNode) {
                    calculateColoumbRepulsion(vDNode, otherVDNode);

                }
            }
            if(debug) System.out.print("  postcoloumbX:" + vDNode.getXForce() + "  postcoloumbY: " + vDNode.getYForce());
            for (ViewableDendrogramEdge vDEdge : vDEdges) {
                calculateHookeAttraction(vDEdge);
            }


            vDNode.setXVelocity(VELOCITY_MODIFIER * (vDNode.getXVelocity() * MOMENTUM_DAMPENING_CONSTANT + vDNode.getXForce()));
            vDNode.setYVelocity(VELOCITY_MODIFIER * (vDNode.getYVelocity() * MOMENTUM_DAMPENING_CONSTANT + vDNode.getYForce()));
            if(debug) System.out.print("  postHookeX: " + vDNode.getXForce() + "  postHookeY:" + vDNode.getYForce());
            if(debug) System.out.print(" XVel: " + vDNode.getXVelocity() + " YVel: " + vDNode.getYVelocity() + "\n");

            node++;
        }


        for (ViewableDendrogramNode vDNode : vDNodes) {
            int oldXCoordinate = vDNode.getXCoordinate();
            int oldYCoordinate = vDNode.getYCoordinate();
            float xVelocity = vDNode.getXVelocity();
            float yVelocity = vDNode.getYVelocity();

            int newXCoordinate = (int) (oldXCoordinate + xVelocity);
            int newYCoordinate = (int) (oldYCoordinate + yVelocity);

            int radius = vDNode.getDiameter() / 2;

            if (newXCoordinate == Integer.MAX_VALUE || newXCoordinate + radius > MAX_X) {
                newXCoordinate = MAX_X - radius;
               
            } else if (newXCoordinate == Integer.MIN_VALUE || newXCoordinate - radius < MIN_X) {
                newXCoordinate = MIN_X + radius;
               
            }

            if (newYCoordinate == Integer.MAX_VALUE || newYCoordinate + radius > MAX_Y) {
                newYCoordinate = MAX_Y - radius;
               
            } else if (newYCoordinate == Integer.MIN_VALUE || newYCoordinate - radius < MIN_Y) {
                newYCoordinate = MIN_Y + radius;
               
            }

            vDNode.setXCoordinate(newXCoordinate);
            vDNode.setYCoordinate(newYCoordinate);
            kineticEnergy += Math.abs(xVelocity) + Math.abs(yVelocity);

        }
        iterations++;
    }

    private static void calculateColoumbRepulsion(ViewableDendrogramNode nodeToChange, ViewableDendrogramNode nodeToObserve) {
        float distance = calculateDistance(nodeToChange, nodeToObserve);
        float totalForce = COULOMB_CONSTANT / (float) Math.pow(distance, 2);

        int xDistance = nodeToChange.getXCoordinate() - nodeToObserve.getXCoordinate();
        int yDistance = nodeToChange.getYCoordinate() - nodeToObserve.getYCoordinate();
        if (xDistance == 0) {
            xDistance = 1;
        }
        if (yDistance == 0) {
            yDistance = 1;
        }
        float xForce = (xDistance / distance) * totalForce;
        float yForce = (yDistance / distance) * totalForce;

        nodeToChange.setXForce(nodeToChange.getXForce() + xForce);
        nodeToChange.setYForce(nodeToChange.getYForce() + yForce);
    }

    private static void calculateHookeAttraction(ViewableDendrogramEdge vde) {
        ViewableDendrogramNode sourceNode = vde.getSourceNode();
        ViewableDendrogramNode targetNode = vde.getTargetNode();

        if (sourceNode == targetNode) {
            return;
        }
        int xDistance = sourceNode.getXCoordinate() - targetNode.getXCoordinate();
        int yDistance = sourceNode.getYCoordinate() - targetNode.getYCoordinate();
        float distance = calculateDistance(sourceNode, targetNode);

        float totalForce = HOOKE_CONSTANT * (SPRING_EQUILIBRIUM_DISTANCE - distance);

        float sourceXForce = (xDistance / distance) * totalForce;
        float sourceYForce = (yDistance / distance) * totalForce;
        sourceNode.setXForce(sourceNode.getXForce() + sourceXForce);
        sourceNode.setYForce(sourceNode.getYForce() + sourceYForce);

        xDistance = targetNode.getXCoordinate() - sourceNode.getXCoordinate();
        yDistance = targetNode.getYCoordinate() - sourceNode.getYCoordinate();

        float targetXForce = (xDistance / distance) * totalForce;
        float targetYForce = (yDistance / distance) * totalForce;
        targetNode.setXForce(targetNode.getXForce() + targetXForce);
        targetNode.setYForce(targetNode.getYForce() + targetYForce);
    }

    private static float calculateDistance(ViewableDendrogramNode node1, ViewableDendrogramNode node2) {
        float distance = (float) Math.sqrt(Math.pow(node1.getXCoordinate() - node2.getXCoordinate(), 2) + Math.pow(node1.getYCoordinate() - node2.getYCoordinate(), 2));
        if (distance < .99) {
            distance = 1;
        }
        return distance;
    }

    public double zoomIn() {
        int furthestClusterIndex = findClosestChildIndex();

        if (furthestClusterIndex == -1) {
            return 0d;
        }

        ViewableDendrogramNode vdn = vDNodes.get(furthestClusterIndex);
        vDNodes.remove(furthestClusterIndex);
        

        ClusterDendrogramNode cdn = (ClusterDendrogramNode) vdn.getDendrogramNode();
        List<DendrogramNode> theChildNodes = cdn.getChildDNodes();

        DendrogramNode[] childNodes = theChildNodes.toArray(new DendrogramNode[0]);


        ViewableDendrogramNode leftNode = new ViewableDendrogramNode(childNodes[0], DIAMETER, vdn.getXCoordinate() - 50, vdn.getYCoordinate() - 50);
        nodeToViewable.put(childNodes[0], leftNode);
        vDNodes.add(leftNode);

        ViewableDendrogramNode rightNode = new ViewableDendrogramNode(childNodes[1], DIAMETER, vdn.getXCoordinate() + 50, vdn.getYCoordinate() + 50);
        nodeToViewable.put(childNodes[1], rightNode);
        vDNodes.add(rightNode);
        


        List<Integer> edgeIndicesToRemove = new ArrayList<Integer>();
        Set<ViewableDendrogramEdge> edgesToAdd = new HashSet<ViewableDendrogramEdge>();
        for (int i = 0; i < vDEdges.size(); i++) {
            if (vDEdges.get(i).getSourceNode() == vdn || vDEdges.get(i).getTargetNode() == vdn) {
                edgeIndicesToRemove.add(i);
                for (DendrogramEdge de : vDEdges.get(i).getDendrogramEdge().getChildEdges()) {
                    ViewableDendrogramEdge newVDE = new ViewableDendrogramEdge(de, nodeToViewable.get(de.getSourceDendrogramNode()), nodeToViewable.get(de.getTargetDendrogramNode()));
                    edgesToAdd.add(newVDE);
                }
            }
        }

        for (int i = edgeIndicesToRemove.size() - 1; i >= 0; i--) {
            vDEdges.remove(edgeIndicesToRemove.get(i).intValue());
        }

        vDEdges.addAll(edgesToAdd);
        kineticEnergy = 100000f;

        return cdn.getDistance();
    }

    public double zoomOut() {
        List<Integer> childIndices = findClosestParentsChildIndices();

        if (childIndices == null || childIndices.isEmpty()) {
            return 0d;
        }

        List<ViewableDendrogramNode> childNodes = new ArrayList<ViewableDendrogramNode>();
        int xCoordSum = 0;
        int yCoordSum = 0;

        for (int i = childIndices.size() - 1; i >= 0; i--) {
            int childIndex = childIndices.get(i);
            ViewableDendrogramNode thisVDNode = vDNodes.get(childIndex);

            xCoordSum += thisVDNode.getXCoordinate();
            yCoordSum += thisVDNode.getYCoordinate();

            childNodes.add(thisVDNode);
            vDNodes.remove(thisVDNode);
        }

        int newXCoord = xCoordSum / childNodes.size();
        int newYCoord = yCoordSum / childNodes.size();

        ViewableDendrogramNode newParent = new ViewableDendrogramNode(childNodes.get(0).getDendrogramNode().getParentDNode(), DIAMETER, newXCoord, newYCoord);
        nodeToViewable.put(newParent.getDendrogramNode(), newParent);
        vDNodes.add(newParent);
        


         List<Integer> edgeIndicesToRemove = new ArrayList<Integer>();
        List<ViewableDendrogramEdge> edgesToAdd = new ArrayList<ViewableDendrogramEdge>();
        Set<DendrogramEdge> parentEdges = new HashSet<DendrogramEdge>();

        for (int i = 0; i < vDEdges.size(); i++) {
            for (ViewableDendrogramNode childNode : childNodes) {
                if (vDEdges.get(i).getSourceNode() == childNode || vDEdges.get(i).getTargetNode() == childNode) {
                    if (!parentEdges.contains(vDEdges.get(i).getDendrogramEdge().getParentDEdge())) {
                        DendrogramEdge parentEdge = vDEdges.get(i).getDendrogramEdge().getParentDEdge();
                        ViewableDendrogramEdge newVDE = new ViewableDendrogramEdge(parentEdge, nodeToViewable.get(parentEdge.getSourceDendrogramNode()), nodeToViewable.get(parentEdge.getTargetDendrogramNode()));
                        edgesToAdd.add(newVDE);
                        parentEdges.add(parentEdge);
                    }
                    edgeIndicesToRemove.add(i);
                    break;
                }
            }
        }

        for (int i = edgeIndicesToRemove.size() - 1; i >= 0; i--) {
            vDEdges.remove(edgeIndicesToRemove.get(i).intValue());
        }

        vDEdges.addAll(edgesToAdd);
        kineticEnergy = 100000f;

        return ((ClusterDendrogramNode) newParent.getDendrogramNode()).getDistance();

    }

    public int findClosestChildIndex() {
        double furthestDistance = Double.MIN_VALUE;
        int furthestClusterIndex = -1;
        for (int i = 0; i < vDNodes.size(); i++) {
            ViewableDendrogramNode vdn = vDNodes.get(i);
            if (vdn.getDendrogramNode() instanceof ClusterDendrogramNode) {
                if (((ClusterDendrogramNode) vdn.getDendrogramNode()).getDistance() > furthestDistance) {
                    furthestDistance = ((ClusterDendrogramNode) vdn.getDendrogramNode()).getDistance();
                    furthestClusterIndex = i;
                }
            }
        }
        return furthestClusterIndex;
    }

    public List<Integer> findClosestParentsChildIndices() {
        List<Integer> childIndices = null;
        double closestDistance = Double.MAX_VALUE;

        for (int i = 0; i < vDNodes.size(); i++) {
            ViewableDendrogramNode vdn = vDNodes.get(i);
            if (vdn.getDendrogramNode().getParentDNode() != null) {
                ClusterDendrogramNode cdn = (ClusterDendrogramNode) vdn.getDendrogramNode().getParentDNode();
                if (childIndices != null && cdn == vDNodes.get(childIndices.get(0)).getDendrogramNode().getParentDNode()) {
                    childIndices.add(i);
                }
                if (cdn.getDistance() < closestDistance) {
                    childIndices = new ArrayList<Integer>();
                    childIndices.add(i);
                    closestDistance = cdn.getDistance();
                }
            }
        }
        return childIndices;
    }
}
