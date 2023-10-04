package main;

public class AVLPlayerNode {
    private Player data;
    private double value;
    private AVLPlayerNode parent;
    private AVLPlayerNode leftChild;
    private AVLPlayerNode rightChild;
    private int rightWeight;
    private int balanceFactor;
    private int height;

    /**
     * Constructor, initialize an AVL player node
     * @param data  the player
     * @param value the value that AVL tree's BST property based on
     * Runtime: O(1)
     */
    public AVLPlayerNode(Player data, double value) {
        this.data = data;
        this.value = value;
        this.parent = null;
        this.leftChild = null;
        this.rightChild = null;
        this.rightWeight = 0;
        this.balanceFactor = 0;
        this.height = 1;
    }

    /**
     * Getter method for left child
     * @return node's left child
     * Runtime: O(1)
     */
    public AVLPlayerNode getLeftChild() {
        return leftChild;
    }

    /**
     * Getter method for right child
     * @return node's right child
     * Runtime: O(1)
     */
    public AVLPlayerNode getRightChild() {
        return rightChild;
    }

    /**
     * Getter method for balance factor
     * @return balance factor
     * Runtime: O(1)
     */
    public int getBalanceFactor() {
        return balanceFactor;
    }

    /**
     * Getter method for height
     * @return height
     * Runtime: O(1)
     */
    public int getHeight() {
        return height;
    }

    /**
     * Getter method for Right Weight
     * @return right weight
     * Runtime: O(1)
     */
    public int getRightWeight() {
        return rightWeight;
    }

    /**
     * Getter method for parent
     * @return parent
     * Runtime: O(1)
     */
    public AVLPlayerNode getParent() {
        return parent;
    }

    /**
     * Balance the AVL tree through different type of rotations
     * @return the root of balanced tree
     * Runtime: O(1)
     */
    private AVLPlayerNode balance() {
        AVLPlayerNode root = this;
        if (this.balanceFactor < -1) { // tree right heavy
            if (this.rightChild.balanceFactor <= 0) { // tree right heavy, or balanced
                root = this.rightChild;
                this.rotateLeft(); // L-rotation
            } else {
                // RL-rotation
                root = this.rightChild.leftChild;
                AVLPlayerNode rightSubTree = this.rightChild.leftChild;
                this.rightChild.rotateRight();
                this.rightChild = rightSubTree;
                this.rotateLeft();
            }
        } else if (this.balanceFactor > 1) { // tree left heavy
            if (this.leftChild.balanceFactor >= 0) { // tree left heavy, or balanced
                root = this.leftChild;
                this.rotateRight(); // R-rotation
            } else {
                // LR-rotation
                root = this.leftChild.rightChild;
                AVLPlayerNode leftSubTree = this.leftChild.rightChild;
                this.leftChild.rotateLeft();
                this.leftChild = leftSubTree;
                this.rotateRight();
            }
        }
        // update height and balance factor
        root.updateHeightAndBF();
        return root;
    }

    /**
     * Reconnect left child's and right child's parent to the node
     * Runtime: O(1)
     */
    private void reconnectChildren() {
        if (this.leftChild != null) {
            this.leftChild.parent = this;
        }
        if (this.rightChild != null) {
            this.rightChild.parent = this;
        }
    }

    /**
     * Check whether the node with given value exists in the AVL tree
     * @param value the value of node
     * @return whether the node exists
     * Runtime: O(log n)
     */
    private boolean hasNode(double value) {
        AVLPlayerNode cur = this;
        while (cur != null && cur.value != value) {
            if (value < cur.value) {
                cur = cur.leftChild;
            } else {
                cur = cur.rightChild;
            }
        }
        return cur != null;
    }

    /**
     * Helper method, insert a player with given value into the AVL tree
     * @param newGuy the player
     * @param value the value that AVL tree's BST property based on
     * @return root of the tree
     * Runtime: O(log n)
     */
    public AVLPlayerNode insertHelper(Player newGuy, double value) {
        if (value < this.value) {
            if (this.leftChild == null) {
                this.leftChild = new AVLPlayerNode(newGuy, value);
            } else {
                this.leftChild = this.leftChild.insertHelper(newGuy, value);
            }
        } else {
            if (this.rightChild == null) {
                this.rightChild = new AVLPlayerNode(newGuy, value);
            } else {
                this.rightChild = this.rightChild.insertHelper(newGuy, value);
            }
            this.rightWeight++;
        }
        this.reconnectChildren();
        // update height and balance factor, and balance the AVL tree
        this.updateHeightAndBF();
        return this.balance();
    }

    /**
     * Insert a node into the AVL tree
     * @param newGuy the player
     * @param value  the value that AVL tree's BST property based on
     * @return root of the tree
     * Runtime: O(log n)
     */
    public AVLPlayerNode insert(Player newGuy, double value) {
        return !hasNode(value) ? insertHelper(newGuy, value) : this;
    }

    /**
     * Helper method, delete a node with given value
     * @param value the value of the node to be deleted
     * @return  root of the tree
     * Runtime: O(log n)
     */
    private AVLPlayerNode deleteHelper(double value) {
        AVLPlayerNode root = this;
        // if the node of given value is found, delete the node
        if (this.value == value) {
            if (this.leftChild == null && this.rightChild == null) {
                return null;
            } else if (this.leftChild != null && this.rightChild != null) {
                AVLPlayerNode successor = this.rightChild;
                while (successor.leftChild != null) {
                    successor = successor.leftChild;
                }
                this.data = successor.data;
                this.value = successor.value;
                this.rightChild = this.rightChild.deleteHelper(successor.value);
                this.rightWeight -= 1;
            } else {
                root = this.leftChild != null ? this.leftChild : this.rightChild;
            }
            root.parent = null;
        } else {
            // Search the node with given value
            if (this.value < value && this.rightChild != null) {
                this.rightChild = this.rightChild.deleteHelper(value);
                this.rightWeight -= 1;
            } else if (this.value > value && this.leftChild != null) {
                this.leftChild = this.leftChild.deleteHelper(value);
            }
        }
        root.reconnectChildren();
        // update height and balance factor, and balance the AVL tree
        this.updateHeightAndBF();
        return root.balance();
    }

    /**
     * Delete the node with given value from the AVL tree
     * @param value the value of node that needs to be deleted
     * @return root of the tree
     * Runtime: O(log n)
     */
    public AVLPlayerNode delete(double value) {
        return hasNode(value) ? this.deleteHelper(value) : this;
    }


    /**
     * Update height and balance factor for the node
     * Runtime: O(1)
     */
    private void updateHeightAndBF() {
        int lh = 0, rh = 0;
        if (this.leftChild != null) {
            lh = this.leftChild.height;
        }
        if (this.rightChild != null) {
            rh = this.rightChild.height;
        }
        this.height = Math.max(lh, rh) + 1;
        this.balanceFactor = lh - rh;
    }

    /**
     * Perform a right rotation
     * Runtime: O(1)
     */
    private void rotateRight() {
        AVLPlayerNode l = this.leftChild, lr = this.leftChild.rightChild;
        l.rightChild = this;
        this.leftChild = lr;
        l.parent = this.parent;
        this.parent = l;
        this.reconnectChildren();
        // update height and balance factor
        this.updateHeightAndBF();
        l.updateHeightAndBF();
        // update right weight
        l.rightWeight += this.rightWeight + 1;
    }

    /**
     * Perform a left rotation
     * Runtime: O(1)
     */
    private void rotateLeft() {
        AVLPlayerNode r = this.rightChild, rl = this.rightChild.leftChild;
        r.leftChild = this;
        this.rightChild = rl;
        r.parent = this.parent;
        this.parent = r;
        this.reconnectChildren();
        // update height and balance factor
        this.updateHeightAndBF();
        r.updateHeightAndBF();
        // update right weight
        this.rightWeight -= (r.rightWeight + 1);
    }

    /**
     * Get the player with given value
     * @param value the value of the player
     * @return the player
     * Runtime: O(log n)
     */
    public Player getPlayer(double value) {
        //TODO
        if (this.value == value) {
            return this.data;
        } else if (this.value < value && this.rightChild != null) {
            return this.rightChild.getPlayer(value);
        } else if (this.value > value && this.leftChild != null) {
            return this.leftChild.getPlayer(value);
        } else {
            return null;
        }
    }

    /**
     * Helper method, get the rank of the node that with a given value
     * @param value the value of the node
     * @param rank the rank of the node
     * @return the rank of node, returns -1 if node is not in the AVL tree
     * Runtime: O(log n)
     */
    private int getRankHelper(double value, int rank) {
        if (this.value == value) {
            return rank + this.rightWeight + 1;
        } else if (this.value < value && this.rightChild != null) {
            return this.rightChild.getRankHelper(value, rank);
        } else if (this.value > value && this.leftChild != null) {
            return this.leftChild.getRankHelper(value, rank + this.rightWeight + 1);
        } else {
            return -1;
        }
    }

    /**
     * Get the rank of the node that with a given value
     * @param value the value of the node
     * @return the rank of node, returns -1 if node is not in the AVL tree
     * Runtime: O(log n)
     */
    //this should return the rank of the node with this.value == value
    public int getRank(double value) {
        return this.getRankHelper(value, 0);
    }

    /**
     * Get a string representation of the AVL tree
     * @return parentheses seperated tree of players' names
     * Runtime: O(n)
     */
    public String treeString() {
        //TODO
        String l = "", r = "";
        if (this.leftChild != null) {
            l = this.leftChild.treeString();
        }
        if (this.rightChild != null) {
            r = this.rightChild.treeString();
        }
        return String.format("(%s%s%s)", l, this.data.getName(), r);
    }

    /**
     * Helper method, get the full scoreboard ordered from the highest value to lowest
     * @return the string representation of scoreboard
     * Runtime: O(n)
     */
    private String scoreboardHelper() {
        String res = "";
        if (this.rightChild != null) {
            res += this.rightChild.scoreboardHelper();
        }
        res += String.format("%s\t%s\t%s\n", this.data.getName(), this.data.getID(), this.data.getELO());
        if (this.leftChild != null) {
            res += this.leftChild.scoreboardHelper();
        }
        return res;
    }

    /**
     * Get the full scoreboard ordered from the highest value to lowest
     * @return the string representation of scoreboard
     * Runtime: O(n)
     */
    public String scoreboard() {
        return "NAME\tID\tSCORE\n" + scoreboardHelper();
    }
}