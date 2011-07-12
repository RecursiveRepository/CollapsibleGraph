package edu.tufts.eecs.graphtheory.collapsiblegraph.windowing.graphlayout;

import edu.tufts.eecs.graphtheory.collapsiblegraph.clustering.DendrogramNode;

/**
 *
 * @author Jeremy
 */
 public class ViewableDendrogramNode {

        TemporaryLayoutNode layoutNode;
        DendrogramNode dendrogramNode;
        int diameter;
        int xCoordinate;
        int yCoordinate;

        public ViewableDendrogramNode(DendrogramNode dendrogramNode, int diameter, int xCoordinate, int yCoordinate) {
            this.dendrogramNode = dendrogramNode;
            this.diameter = diameter;
            this.xCoordinate = xCoordinate;
            this.yCoordinate = yCoordinate;
        }

        public DendrogramNode getDendrogramNode() {
            return dendrogramNode;
        }

        public int getDiameter() {
            return diameter;
        }

        public int getXCoordinate() {
            return xCoordinate;
        }

        public int getYCoordinate() {
            return yCoordinate;
        }
        
        public void setXCoordinate(int xCoordinate) {
            this.xCoordinate = xCoordinate;
        }

        public void setYCoordinate(int yCoordinate) {
            this.yCoordinate = yCoordinate;
        }
        
        public void setLayoutNode(TemporaryLayoutNode layoutNode) {
            this.layoutNode = layoutNode;
        }
        
        public TemporaryLayoutNode getLayoutNode() {
            return layoutNode;
        }
    }
