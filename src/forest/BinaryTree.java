package forest;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;

/**
 * @author smithk
 * @param <T> must implement Comparable
 */
public class BinaryTree<T> implements Iterable<T> {
    protected T fruit;
    public T getFruit() {
        return fruit;
    }
    
    // one could argue that these ought to be granted public accessors, but
    // if the encapsulation is done correctly, a user should not be messing
    // with the tree structure. So, no!
    //
    protected BinaryTree<T> leftBranch;
    protected BinaryTree<T> rightBranch;
    
    protected boolean isLeaf() {
        return leftBranch == null && rightBranch == null;
    }

    /**
     * Creates a BinaryTree with a single fruited node.
     * @param fruit 
     */
    public BinaryTree (T fruit) {
        this.fruit = fruit;
    }
    
    public BinaryTree (T fruit, BinaryTree<T> leftBranch, BinaryTree<T> rightBranch) {
        this.fruit = fruit;
        this.leftBranch = leftBranch;
        this.rightBranch = rightBranch;
    }
    
    public String toString() {
        String generations = "";
        if (leftBranch != null)
            generations = leftBranch.toString();
        if (rightBranch != null)
            generations += rightBranch.toString();
        generations += fruit.toString() + " ";
        return generations;
    }
    
    /**
     * DfIterator implements a left-handed depth-first traversal of a binary
     * tree. Each invocation of next() returns the "next" piece of fruit in
     * traversal order. A stack is used to eliminate the need to completely
     * traverse the tree during construction time. 
     */
    private class DfIterator implements Iterator<T> {
        /**
         * the top of the stack represents the cursor for the iteration, that
         * is, next() returns the data of the node at the top of the stack.
         */
        private Stack<BinaryTree<T>> dfStack;
        
        public DfIterator() {
            dfStack = new Stack();
            // beginning with this node, iterate leftwrards, pushing each 
            // node until the left-most node in the tree has been reached.
            // That leftmost node is at the cursor position for next();
            //
            BinaryTree<T> tree = BinaryTree.this;
            while (tree != null) {
                dfStack.push (tree);
                tree = tree.leftBranch;
                
            }
        }

        @Override
        public boolean hasNext() {
            // if the stack is empty, there is no data at the cursor position.
            return !dfStack.isEmpty();
        }

        @Override
        public T next() {
            if (hasNext()) {
                // pop the data from the cursor position. Then, if there is a 
                // righthand subtree, iteratively push the leftmost nodes of
                // that subtree onto the stack (see ctor).
                //
                BinaryTree<T> tree = dfStack.pop();
                T result = tree.fruit;
                tree = tree.rightBranch;
                while (tree != null) {
                    dfStack.push (tree);
                    tree = tree.leftBranch;
                }
                return result;
            }
            throw new NoSuchElementException();
        }
    }
    
    private class BfIterator implements Iterator<T> {
        private class Queue<E> extends ArrayList<E> {
            public void enqueue(E e) {
                add (e);
            }
            public E dequeue() {
                if (isEmpty())
                    throw new NoSuchElementException();
                return remove(0);
            }
        }
        
        private Queue<BinaryTree<T>> bfQueue;
        
        public BfIterator() {
            bfQueue = new Queue();
            bfQueue.enqueue (BinaryTree.this);
        }

        @Override
        public boolean hasNext() {
            return !bfQueue.isEmpty();
        }

        @Override
        public T next() {
            if (!hasNext())
                throw new NoSuchElementException();
            BinaryTree<T> tree = bfQueue.dequeue();
            if (tree.leftBranch != null)
                bfQueue.enqueue (tree.leftBranch);
            if (tree.rightBranch != null)
                bfQueue.enqueue (tree.rightBranch);
            return tree.getFruit();
        }
    }
    
    /**
     * Two trees are considered equal if congruent iterators return identical
     * sequences of treed data.
     * @param other the "other" tree
     * @return true if the iterators return identical sequences.
     */
    @Override
    public boolean equals (Object other) {
        if (other instanceof BinaryTree) {
            Iterator thisIter = iterator();
            Iterator otherIter = ((BinaryTree)other).iterator();
            while (thisIter.hasNext() && otherIter.hasNext())
                if (!thisIter.next().equals (otherIter.next()))
                    return false;
            return !thisIter.hasNext() && !otherIter.hasNext();
        }
        throw new IllegalArgumentException();
    }
    
    @Override
    public Iterator<T> iterator() {
        return new DfIterator ();
    }

    private void insert (BinaryTree<T> node) {
        // all insertions occur at the leaves, so essentially, insert needs
        // only to trickle down the tree until a landing spot is located.
        //   
        if (!(node.fruit instanceof Comparable) || !(fruit instanceof Comparable))
            throw new UnsupportedOperationException();
        
        if (((Comparable)node.fruit).compareTo (this.fruit) < 0)
            if (leftBranch == null)
                leftBranch = node;
            else
                leftBranch.insert (node);
        else
            if (rightBranch == null)
                rightBranch = node;
            else
                rightBranch.insert (node);
    }
    
    /**
     * 
     * @param fruit that is to be added to the tree. Duplicate elements are
     * permitted and will be added "after" their duplicated predecessors.
     * @return the node that was created to contain the added fruit.
     */
    public BinaryTree<T> insert (T fruit) {
        BinaryTree<T> graft = new BinaryTree(fruit);
        insert (graft);
        return graft;
    }
    
    /**
     * This method removes this, a single node from the tree. If this node
     * is not a leaf, its branches are retained in the tree.
     * @param root A root (ancestor) of this node. root must be non-null if
     * this is a leaf node.
     * @throws IllegalArgumentException if this is a leaf node and root does
     * not specify a non-null ancestor of this node.
     */
    public void remove (BinaryTree<T> root) {
        // non-leaf nodes can be "removed" by replacement. 
        
        BinaryTree<T> successor = leftBranch;
        if (successor != null) {
            fruit = successor.fruit;
            leftBranch = successor.leftBranch;
            if (successor.rightBranch != null)
                insert (successor.rightBranch);
        }
        else {
            successor = rightBranch;
            if (successor != null) {
                fruit = successor.fruit;
                rightBranch = successor.rightBranch;
                if (successor.leftBranch != null)
                    insert (successor.leftBranch);
            }
            else {
                // node is a leaf, so we must find parent.
                if (root == null || root == this)
                    throw new IllegalArgumentException();
                if (!(fruit instanceof Comparable) || !(root.fruit instanceof Comparable))
                    throw new UnsupportedOperationException();
                
                do {
                    int comparison = ((Comparable)fruit).compareTo (root.fruit);
                    if (comparison < 0)
                        if (root.leftBranch == this) {
                            root.leftBranch = null;
                            break;
                        }
                        else
                            root = root.leftBranch;
                    else
                        if (root.rightBranch == this) {
                            root.rightBranch = null;
                            break;
                        }
                        else
                            root = root.rightBranch;
                } while (true);
            }
        }
    }
    
    /**
     * Remove this node from the tree, assuming that this is not a leaf node.
     * @see remove(root)
     * @throws UnsupportedOperationException if this is a leaf
     */
    public void remove() {
        if (isLeaf())
            throw new UnsupportedOperationException();
        remove (null);
    }
    
        
    public static void main(String[] args) {
        BinaryTree<String> bts = new BinaryTree("dog");
        bts.insert ("cat");
        bts.insert ("aardvark");
        bts.insert ("buffalo");
        bts.insert ("horse");
        bts.insert ("zebra");
/*        
        bts.insert ("monkey");
        bts.insert ("cheetah");
        bts.insert ("lion");
        bts.insert ("tiger");
        bts.insert ("bear");
*/

        for (String treed : bts)
            System.out.println(treed);
        System.out.println();
        
        bts.remove(bts);
        System.out.println(bts);
        bts.rightBranch.remove (bts);
        System.out.println(bts);
        bts.rightBranch.remove (bts);
        System.out.println(bts);
    }
}
