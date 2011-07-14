package edu.tufts.eecs.graphtheory.collapsiblegraph.windowing.graphlayout;

import edu.tufts.eecs.graphtheory.collapsiblegraph.clustering.DendrogramEdge;
import edu.tufts.eecs.graphtheory.collapsiblegraph.clustering.DendrogramNode;
import edu.tufts.eecs.graphtheory.collapsiblegraph.viewing.DendrogramSlice;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 *
 * @author Jeremy
 */
public class ForceDirectedLayoutGenerator {

    private static int MIN_X = 0;
    private static int MAX_X = 1024;
    private static int MIN_Y = 0;
    private static int MAX_Y = 500;
    private static int DIAMETER = 20;
    private static final int SPRING_EQUILIBRIUM_DISTANCE = 20;
    private static final float HOOKE_CONSTANT = 10f;
    private static final float COULOMB_CONSTANT = 8987000000f/ 10f;
    private static final float VELOCITY_MODIFIER = .01f;
    private static final float MOMENTUM_DAMPENING_CONSTANT = .5f;
    private static final Random generator = new Random();
    private Map<DendrogramNode, ViewableDendrogramNode> nodeToViewable = new HashMap<DendrogramNode, ViewableDendrogramNode>();
    
    private List<ViewableDendrogramNode> vDNodes;
    private List<ViewableDendrogramEdge> vDEdges;        
    private float kineticEnergy=10000000;
    private float previousKineticEnergy=0;
    private int iterations = 0;

     List<TemporaryLayoutNode> layoutNodes ;
    List<ViewableDendrogramEdge> dendrogramEdges;
    
    public List<ViewableDendrogramNode> getGraphNodes() {
        return vDNodes;
    }
    
    public List<ViewableDendrogramEdge> getGraphEdges() {
        return vDEdges;
    }
    
    public float getKineticEnergy() {
        return kineticEnergy;
    }
    public void setupLayout(DendrogramSlice ds) {
        kineticEnergy = 10000f;
        vDNodes = new ArrayList<ViewableDendrogramNode>();
        vDEdges = new ArrayList<ViewableDendrogramEdge>();

        nodeToViewable.clear();
        DendrogramNode[] nodeSet = ds.getVisibleDendrogramNodes();
        DendrogramEdge[] edgeSet = ds.getVisibleDendrogramEdges();

        layoutNodes = new ArrayList<TemporaryLayoutNode>();
        dendrogramEdges = new ArrayList<ViewableDendrogramEdge>();
        

        for (DendrogramNode thisNode : nodeSet) {
            int radius = DIAMETER / 2;
            int xCoord = generator.nextInt(MAX_X - radius) + radius;
            int yCoord = generator.nextInt(MAX_Y - radius) + radius;
            ViewableDendrogramNode newVDNode = new ViewableDendrogramNode(thisNode, DIAMETER, xCoord, yCoord);
            nodeToViewable.put(thisNode, newVDNode);
            TemporaryLayoutNode newBouncyNode = new TemporaryLayoutNode(newVDNode);
            newVDNode.setLayoutNode(newBouncyNode);
            layoutNodes.add(newBouncyNode);
            vDNodes.add(newVDNode);
        }

        for (DendrogramEdge thisEdge : edgeSet) {
            ViewableDendrogramEdge newVDE = new ViewableDendrogramEdge(thisEdge, nodeToViewable.get(thisEdge.getSourceDendrogramNode()), nodeToViewable.get(thisEdge.getTargetDendrogramNode()));
            dendrogramEdges.add(newVDE);
        }
        
        for (DendrogramEdge dEdge : edgeSet) {
            vDEdges.add(new ViewableDendrogramEdge(dEdge, nodeToViewable.get(dEdge.getSourceDendrogramNode()),
                    nodeToViewable.get(dEdge.getTargetDendrogramNode())));
        }    
        iterations=0;
    }
    
    

    public void iterate() {
            if(kineticEnergy < 10f) {
                return;
            }
            previousKineticEnergy = kineticEnergy;
            kineticEnergy = 0f;
            System.out.println("Iteration " + iterations );
            int node = 0;
            for (TemporaryLayoutNode layoutNode : layoutNodes) {
                System.out.print(" node: " + node + "  ");
                layoutNode.setXForce(0F);
                layoutNode.setYForce(0F);
                System.out.print(" x:" + layoutNode.getVDNode().getXCoordinate() + "  y:" + layoutNode.getVDNode().getYCoordinate());
                for (TemporaryLayoutNode otherLayoutNode : layoutNodes) {
                    if (layoutNode != otherLayoutNode) {
                        calculateColoumbRepulsion(layoutNode, otherLayoutNode);
                        
                    }
                }
                System.out.print("  postcoloumbX:" + layoutNode.getXForce() + "  postcoloumbY: " + layoutNode.getYForce());
                for (ViewableDendrogramEdge vDEdge : dendrogramEdges) {
               //     calculateHookeAttraction(vDEdge);
                }

                
                layoutNode.setXVelocity(VELOCITY_MODIFIER * (layoutNode.getXVelocity() * MOMENTUM_DAMPENING_CONSTANT + layoutNode.getXForce()));
                layoutNode.setYVelocity(VELOCITY_MODIFIER * (layoutNode.getYVelocity() * MOMENTUM_DAMPENING_CONSTANT + layoutNode.getYForce()));
                System.out.print("  postHookeX: " + layoutNode.getXForce() + "  postHookeY:" + layoutNode.getYForce());
                System.out.print(" XVel: " + layoutNode.getXVelocity() + " YVel: " + layoutNode.getYVelocity() + "\n");
                
                node++;
            }


            for (TemporaryLayoutNode layoutNode : layoutNodes) {
                int oldXCoordinate = layoutNode.getVDNode().getXCoordinate();
                int oldYCoordinate = layoutNode.getVDNode().getYCoordinate();
                float xVelocity = layoutNode.getXVelocity();
                float yVelocity = layoutNode.getYVelocity();

                int newXCoordinate = (int) (oldXCoordinate + xVelocity);;
                int newYCoordinate = (int) (oldYCoordinate + yVelocity);

                int radius = layoutNode.getVDNode().getDiameter() / 2;


                if (newXCoordinate == Integer.MAX_VALUE || newXCoordinate + radius > MAX_X) {
                    newXCoordinate = MAX_X - radius;
                    xVelocity = 0;
                } else if (newXCoordinate == Integer.MIN_VALUE || newXCoordinate - radius < MIN_X) {
                    newXCoordinate = MIN_X + radius;
                    xVelocity = 0;
                }

                if (newYCoordinate == Integer.MAX_VALUE || newYCoordinate + radius > MAX_Y) {
                    newYCoordinate = MAX_Y - radius;
                    yVelocity = 0;
                } else if (newYCoordinate == Integer.MIN_VALUE || newYCoordinate - radius < MIN_Y) {
                    newYCoordinate = MIN_Y + radius;
                    yVelocity = 0;
                }

                layoutNode.getVDNode().setXCoordinate(newXCoordinate);
                layoutNode.getVDNode().setYCoordinate(newYCoordinate);
                kineticEnergy += Math.abs(xVelocity) + Math.abs(yVelocity);
                
            }
            iterations++;
    }
    
    private static void calculateColoumbRepulsion(TemporaryLayoutNode nodeToChange, TemporaryLayoutNode nodeToObserve) {
        float distance = calculateDistance(nodeToChange.getVDNode(), nodeToObserve.getVDNode());
        float totalForce = COULOMB_CONSTANT / (float) Math.pow(distance, 2);

        int xDistance = nodeToChange.getVDNode().getXCoordinate() - nodeToObserve.getVDNode().getXCoordinate();
        int yDistance = nodeToChange.getVDNode().getYCoordinate() - nodeToObserve.getVDNode().getYCoordinate();
        if (xDistance < 1) {
            xDistance = 1;
        }
        if (yDistance < 1) {
            yDistance = 1;
        }
        float xForce = (xDistance / distance) * totalForce;
        float yForce = (yDistance / distance) * totalForce;

        nodeToChange.setXForce(nodeToChange.getXForce() + xForce);
        nodeToChange.setYForce(nodeToChange.getYForce() + yForce);
    }

    private static void calculateHookeAttraction(ViewableDendrogramEdge vde) {
        TemporaryLayoutNode sourceNode = vde.getSourceNode().getLayoutNode();
        TemporaryLayoutNode targetNode = vde.getTargetNode().getLayoutNode();

        if(sourceNode==targetNode) {
            return;
        }
        int xDistance = sourceNode.getVDNode().getXCoordinate() - targetNode.getVDNode().getXCoordinate();
        int yDistance = sourceNode.getVDNode().getYCoordinate() - targetNode.getVDNode().getYCoordinate();
        float distance = calculateDistance(sourceNode.getVDNode(), targetNode.getVDNode());

        float totalForce = HOOKE_CONSTANT * (SPRING_EQUILIBRIUM_DISTANCE - distance);

        float sourceXForce = (xDistance / distance) * totalForce;
        float sourceYForce = (yDistance / distance) * totalForce;
        sourceNode.setXForce(sourceNode.getXForce() + sourceXForce);
        sourceNode.setYForce(sourceNode.getYForce() + sourceYForce);

        xDistance = targetNode.getVDNode().getXCoordinate() - sourceNode.getVDNode().getXCoordinate();
        yDistance = targetNode.getVDNode().getYCoordinate() - sourceNode.getVDNode().getYCoordinate();
        
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
}
