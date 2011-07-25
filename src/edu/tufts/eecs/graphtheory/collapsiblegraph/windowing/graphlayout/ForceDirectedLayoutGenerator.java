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
    //A random nubmer generator
    private static final Random generator = new Random();
    //A map linking DendrogramNodes to their containing ViewableDendrogramNode 
    private Map<DendrogramNode, ViewableDendrogramNode> nodeToViewable = new HashMap<DendrogramNode, ViewableDendrogramNode>();
    private List<ViewableDendrogramNode> vDNodes; //The ViewableDendrogramNodes that should be displayed
    private List<ViewableDendrogramEdge> vDEdges; //The ViewableDendrogramEdges that should be displayed
    private float kineticEnergy = 10000000; //The total amount of kinetic energy in the system.  
    private float previousKineticEnergy = 0;
    //How many iterations there've been on this current simulation
    private int iterations = 0;

    /**
     * 
     * @return A list of the ViewableDendrogramNodes at this moment
     */
    public List<ViewableDendrogramNode> getVDNodes() {
        return vDNodes;
    }

    /**
     * 
     * @return A list of the ViewableDendrogramEdges at this moment
     */
    public List<ViewableDendrogramEdge> getGraphEdges() {
        return vDEdges;
    }

    /**
     * A function to set the graph to use a new DendrogramSlice as its source data. Typically the result of a new zoom
     * @param dendrogramSlice is the DendrogramSlice to use to generate a view
     */
    public void setupLayout(DendrogramSlice dendrogramSlice) {
        kineticEnergy = 10000f; //Set this high to begin with so the loop iterates at least once!
        //Lists to hold the ViewableDendrogramNodes and ViewableDendrogramEdges
        vDNodes = new ArrayList<ViewableDendrogramNode>();
        vDEdges = new ArrayList<ViewableDendrogramEdge>();

        nodeToViewable.clear();
        //The DendrogramNodes and DendrogramEdges we're to work with
        DendrogramNode[] dNodeSet = dendrogramSlice.getVisibleDendrogramNodes();
        DendrogramEdge[] dEdgeSet = dendrogramSlice.getVisibleDendrogramEdges();

        //Create a ViewableDendrogramNode to wrap each DendrogramNode
        for (DendrogramNode thisNode : dNodeSet) {
            int radius = DIAMETER / 2;
            int xCoord = generator.nextInt(MAX_X - radius) + radius;
            int yCoord = generator.nextInt(MAX_Y - radius) + radius;
            ViewableDendrogramNode newVDNode = new ViewableDendrogramNode(thisNode, DIAMETER, xCoord, yCoord);
            nodeToViewable.put(thisNode, newVDNode);
            vDNodes.add(newVDNode);
        }

        //Create a ViewableDendrogramEdge for each DendrogramEdge
        for (DendrogramEdge dEdge : dEdgeSet) {
            if (nodeToViewable.get(dEdge.getSourceDendrogramNode()) != null
                    && nodeToViewable.get(dEdge.getTargetDendrogramNode()) != null) {
                vDEdges.add(new ViewableDendrogramEdge(dEdge, nodeToViewable.get(dEdge.getSourceDendrogramNode()),
                        nodeToViewable.get(dEdge.getTargetDendrogramNode())));
            }
        }
        iterations = 0;
    }

    /**
     * Perform one iteration of the physics based animation routine. 
     */
    public void iterate() {
        //If things are basically done moving, let's not waste CPU cycles
        if (kineticEnergy < 20f) {
            return;
        }
        previousKineticEnergy = kineticEnergy;
        kineticEnergy = 0f;
        
        //Loop over the ViewableDendrogramNodes, reset Force on them to 0, and calculate their mutual repulsion
        for (ViewableDendrogramNode vDNode : vDNodes) {
            vDNode.setXForce(0F);
            vDNode.setYForce(0F);

            for (ViewableDendrogramNode otherVDNode : vDNodes) {
                if (vDNode != otherVDNode) {
                    calculateColoumbRepulsion(vDNode, otherVDNode);
                }
            }
        }
        
        //Iterate over the set of ViewableDendrogramEdges and calculate the attraction they have on nodes
        for (ViewableDendrogramEdge vDEdge : vDEdges) {
            calculateHookeAttraction(vDEdge);
        }
        
        //Loop over the ViewableDendrogramNodes and determine their new velocities and positions
        for (ViewableDendrogramNode vDNode : vDNodes) {
            int oldXCoordinate = vDNode.getXCoordinate();
            int oldYCoordinate = vDNode.getYCoordinate();

            vDNode.setXVelocity(VELOCITY_MODIFIER * (vDNode.getXVelocity() * MOMENTUM_DAMPENING_CONSTANT + vDNode.getXForce()));
            vDNode.setYVelocity(VELOCITY_MODIFIER * (vDNode.getYVelocity() * MOMENTUM_DAMPENING_CONSTANT + vDNode.getYForce()));

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

    /**
     * A function to calculate the repulsion between two Nodes. It will only actually change the values on the vDNodeToChange ViewableDendrogramNode
     * @param vDNodeToChange the ViewableDendrogramNode whose values will actually be adjusted by this operation
     * @param vDNodeToObserve the ViewableDendrogramNode that's only there to calculate the force on the vDNodeToChange
     */
    private static void calculateColoumbRepulsion(ViewableDendrogramNode vDNodeToChange, ViewableDendrogramNode vDNodeToObserve) {
        float distance = calculateDistance(vDNodeToChange, vDNodeToObserve);
        float totalForce = COULOMB_CONSTANT / (float) Math.pow(distance, 2);

        int xDistance = vDNodeToChange.getXCoordinate() - vDNodeToObserve.getXCoordinate();
        int yDistance = vDNodeToChange.getYCoordinate() - vDNodeToObserve.getYCoordinate();
        if (xDistance == 0) {
            xDistance = 1;
        }
        if (yDistance == 0) {
            yDistance = 1;
        }
        float xForce = (xDistance / distance) * totalForce;
        float yForce = (yDistance / distance) * totalForce;

        vDNodeToChange.setXForce(vDNodeToChange.getXForce() + xForce);
        vDNodeToChange.setYForce(vDNodeToChange.getYForce() + yForce);
    }

    /**
     * A function to calculate the attraction effect that a ViewableDendrogramEdge has on the two
     * nodes that it connects. This will update BOTH nodes
     * @param vDEdge the Edge to be processed
     */
    private static void calculateHookeAttraction(ViewableDendrogramEdge vDEdge) {
        ViewableDendrogramNode sourceVDNode = vDEdge.getSourceNode();
        ViewableDendrogramNode targetVDNode = vDEdge.getTargetNode();

        if (sourceVDNode == targetVDNode) {
            return;
        }
        int xDistance = sourceVDNode.getXCoordinate() - targetVDNode.getXCoordinate();
        int yDistance = sourceVDNode.getYCoordinate() - targetVDNode.getYCoordinate();
        float distance = calculateDistance(sourceVDNode, targetVDNode);

        float totalForce = HOOKE_CONSTANT * (SPRING_EQUILIBRIUM_DISTANCE - distance);

        float sourceXForce = (xDistance / distance) * totalForce;
        float sourceYForce = (yDistance / distance) * totalForce;
        sourceVDNode.setXForce(sourceVDNode.getXForce() + sourceXForce);
        sourceVDNode.setYForce(sourceVDNode.getYForce() + sourceYForce);

        xDistance = targetVDNode.getXCoordinate() - sourceVDNode.getXCoordinate();
        yDistance = targetVDNode.getYCoordinate() - sourceVDNode.getYCoordinate();

        float targetXForce = (xDistance / distance) * totalForce;
        float targetYForce = (yDistance / distance) * totalForce;
        targetVDNode.setXForce(targetVDNode.getXForce() + targetXForce);
        targetVDNode.setYForce(targetVDNode.getYForce() + targetYForce);
    }
    
    /**
     * A function to calculate the current 2d space distance between two ViewableDendrogramNodes
     * @param vDNode1 The first of the two ViewableDendrogramNodes we're measuring distance between
     * @param vDNode2 The second of the two ViewableDendrogramNodes we're measuring distance between
     * @return the distance between those two ViewableDendrogramNodes
     */
    private static float calculateDistance(ViewableDendrogramNode vDNode1, ViewableDendrogramNode vDNode2) {
        float distance = (float) Math.sqrt(Math.pow(vDNode1.getXCoordinate() - vDNode2.getXCoordinate(), 2) + Math.pow(vDNode1.getYCoordinate() - vDNode2.getYCoordinate(), 2));
        if (distance < .99) {
            distance = 1;
        }
        return distance;
    }

    /**
     * A function that zooms in the layout by exactly one clustering, allowing users to step through the clustering process
     * @return the distance that this advances the zoom to
     */
    public double zoomIn() {
        //Find the furthest out Cluster that's closer than our current zoom distance
        int furthestClusterIndex = findClosestChildIndex();

        if (furthestClusterIndex == -1) {
            return 0d;
        }

        //Remove this old cluster from the ViewableDendrogramNodes
        ViewableDendrogramNode furthestVDNode = vDNodes.get(furthestClusterIndex);
        vDNodes.remove(furthestClusterIndex);

        ClusterDendrogramNode furthestDNode = (ClusterDendrogramNode) furthestVDNode.getDendrogramNode();
        DendrogramNode[] childVDNodes = furthestDNode.getChildDNodes().toArray(new DendrogramNode[0]);

        //Add the two child clusters to the ViewableDendrogramNodes
        ViewableDendrogramNode leftVDNode = new ViewableDendrogramNode(childVDNodes[0], DIAMETER, furthestVDNode.getXCoordinate() - 50, furthestVDNode.getYCoordinate() - 50);
        nodeToViewable.put(childVDNodes[0], leftVDNode);
        vDNodes.add(leftVDNode);

        ViewableDendrogramNode rightVDNode = new ViewableDendrogramNode(childVDNodes[1], DIAMETER, furthestVDNode.getXCoordinate() + 50, furthestVDNode.getYCoordinate() + 50);
        nodeToViewable.put(childVDNodes[1], rightVDNode);
        vDNodes.add(rightVDNode);
        
        //Check to see if any of the edges touched the deleted node. If so, replace it with its child edges in the ViewableDendrogramEdges
        List<Integer> vDEdgeIndicesToRemove = new ArrayList<Integer>();
        Set<ViewableDendrogramEdge> vDEdgesToADd = new HashSet<ViewableDendrogramEdge>();
        for (int i = 0; i < vDEdges.size(); i++) {
            if (vDEdges.get(i).getSourceNode() == furthestVDNode || vDEdges.get(i).getTargetNode() == furthestVDNode) {
                vDEdgeIndicesToRemove.add(i);
                for (DendrogramEdge de : vDEdges.get(i).getDendrogramEdge().getChildEdges()) {
                    ViewableDendrogramEdge newVDE = new ViewableDendrogramEdge(de, nodeToViewable.get(de.getSourceDendrogramNode()), nodeToViewable.get(de.getTargetDendrogramNode()));
                    vDEdgesToADd.add(newVDE);
                }
            }
        }

        for (int i = vDEdgeIndicesToRemove.size() - 1; i >= 0; i--) {
            vDEdges.remove(vDEdgeIndicesToRemove.get(i).intValue());
        }

        vDEdges.addAll(vDEdgesToADd);
        
        //Start the kinetic energy high to make sure that the animation iterates at least once
        kineticEnergy = Float.MAX_VALUE;

        return furthestDNode.getDistance();
    }

    /**
     * A function that zooms out the layout by exactly one clustering, allowing users to step through the clustering process
     * @return the distance that this advances the zoom to
     */
    public double zoomOut() {
        //Find the two child nodes that are would be the next to be joined together in clustering
        List<Integer> childIndices = findClosestParentsChildIndices();

        if (childIndices == null || childIndices.isEmpty()) {
            return 0d;
        }

        List<ViewableDendrogramNode> childVDNodes = new ArrayList<ViewableDendrogramNode>();
        int xCoordSum = 0;
        int yCoordSum = 0;
        //Remove these children from vDNodes, and average their x and y coordinates to find the location of the new parent node to show
        for (int i = childIndices.size() - 1; i >= 0; i--) {
            int childIndex = childIndices.get(i);
            ViewableDendrogramNode thisVDNode = vDNodes.get(childIndex);

            xCoordSum += thisVDNode.getXCoordinate();
            yCoordSum += thisVDNode.getYCoordinate();

            childVDNodes.add(thisVDNode);
            vDNodes.remove(thisVDNode);
        }

        int newXCoord = xCoordSum / childVDNodes.size();
        int newYCoord = yCoordSum / childVDNodes.size();

        //Create a ViewableDendrogramNode to represent that closest parent
        ViewableDendrogramNode newParent = new ViewableDendrogramNode(childVDNodes.get(0).getDendrogramNode().getParentDNode(), DIAMETER, newXCoord, newYCoord);
        nodeToViewable.put(newParent.getDendrogramNode(), newParent);
        vDNodes.add(newParent);

        List<Integer> vDEdgeIndicesToRemove = new ArrayList<Integer>();
        List<ViewableDendrogramEdge> vDEdgesToAdd = new ArrayList<ViewableDendrogramEdge>();
        Set<DendrogramEdge> parentVDEdges = new HashSet<DendrogramEdge>();
        //Update the edges to reflect our zoom out on nodes
        for (int i = 0; i < vDEdges.size(); i++) {
            for (ViewableDendrogramNode childNode : childVDNodes) {
                if (vDEdges.get(i).getSourceNode() == childNode || vDEdges.get(i).getTargetNode() == childNode) {
                    if (!parentVDEdges.contains(vDEdges.get(i).getDendrogramEdge().getParentDEdge())) {
                        DendrogramEdge parentEdge = vDEdges.get(i).getDendrogramEdge().getParentDEdge();
                        ViewableDendrogramEdge newVDE = new ViewableDendrogramEdge(parentEdge, nodeToViewable.get(parentEdge.getSourceDendrogramNode()), nodeToViewable.get(parentEdge.getTargetDendrogramNode()));
                        vDEdgesToAdd.add(newVDE);
                        parentVDEdges.add(parentEdge);
                    }
                    vDEdgeIndicesToRemove.add(i);
                    break;
                }
            }
        }

        for (int i = vDEdgeIndicesToRemove.size() - 1; i >= 0; i--) {
            vDEdges.remove(vDEdgeIndicesToRemove.get(i).intValue());
        }

        vDEdges.addAll(vDEdgesToAdd);
        kineticEnergy = 100000f;

        return ((ClusterDendrogramNode) newParent.getDendrogramNode()).getDistance();
    }

    /** 
     * Finds the index of the closest cluster that is closer than the current zoom level
     * @return the index of the closest cluster that is closer than the current zoom level
     */
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

    /**
     * Find the pair of vertices that are the children of the closest cluster that is further out than the current zoom level
     * @return the pair of vertices that are the children of the closest cluster that is further out than the current zoom level
     */
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
