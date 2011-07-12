package edu.tufts.eecs.graphtheory.collapsiblegraph.windowing.graphlayout;

 public class TemporaryLayoutNode {
        private ViewableDendrogramNode vDNode;
        private float xForce;
        private float yForce;
        private float xVelocity;
        private float yVelocity;
        
        public TemporaryLayoutNode(ViewableDendrogramNode vDNode) {
            this.vDNode = vDNode;
            xForce=0.0f;
            yForce=0.0f;
            xVelocity=0.0f;
            yVelocity=0.0f;
        }
        
        public ViewableDendrogramNode getVDNode() {
            return vDNode;
        }
        
        public void setXForce(float xForce) {
            this.xForce = xForce;
        }
        
        public float getXForce() {
            return xForce;
        }
        
        public void setYForce(float yForce) {
            this.yForce = yForce;
        }
        
        public float getYForce() {
            return yForce;
        }
        
        public void setXVelocity(float xVelocity) {
            this.xVelocity = xVelocity;
        }
        
        public float getXVelocity() {
            return xVelocity;
        }
        
        public void setYVelocity(float yVelocity) {
            this.yVelocity = yVelocity;
        }
        
        public float getYVelocity() {
            return yVelocity;
        }
    }
    
