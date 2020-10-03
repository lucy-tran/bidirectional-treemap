package comp128.biDirectionalTreeMap;

import java.io.Serializable;

/**
 * A treemap storing both the keys and values in BSTs with nodes bidirectionally linking keys and values.
 * This makes searching for both a key or a value O(logn).
 * The map does not allow duplicate keys OR values.
 *
 * @param <K>
 * @param <V>
 */
public class BidirectionalTreeMap<K extends Comparable<K>, V extends Comparable<V>> {

    /**
     * A Node in the BidirectionalTreeMap.
     *
     * @param <K> The type of data
     * @param <V> The type of the link's data.
     */
    protected static class Node<K, V> implements Serializable {
        /**
         * The information stored in this node.
         */
        public K data;
        /**
         * Reference to the node linked to this.
         */
        public Node<V, K> link;
        /**
         * Reference to the left child.
         */
        public Node<K, V> left;
        /**
         * Reference to the right child.
         */
        public Node<K, V> right;
        /**
         * Reference to the parent
         */
        public Node<K, V> parent;

        // Constructor
        public Node(K data) {
            this.data = data;
            left = null;
            right = null;
            parent = null;
        }
    }
    /**
     * Reference to the root of the BST whose data is of type K.
     */
    protected Node keyRoot;
    /**
     * Reference to the root of the BST whose data is of type V.
     */
    protected Node valueRoot;
    /**
     * The number of pairs in the map.
     */
    protected int size;

    /**
     * constructor initializes the tree with size 0
     */
    public BidirectionalTreeMap() {
        size = 0;
        keyRoot = null;
        valueRoot = null;
    }

    /**
     * Adds the key and value association to the map. The key is stored in the BST with root keyRoot.
     * The value is stored in a separate BST with root valueRoot. Both node objects contain links to each other.
     *
     * @param key
     * @param value
     * @return true if the key/value pair was inserted. If the key or the value already exist in the map, it is not modified and a value of false is returned.
     */
    public boolean put(K key, V value) {
        if (containsKey(key) || containsValue(value)) {
            return false;
        } else {
            Node<K, V> keyNode = new Node(key);
            Node<V, K> valueNode = new Node(value);
            keyNode.link = valueNode;
            valueNode.link = keyNode;
            keyRoot = add(keyRoot, new Node(null), keyNode);
            valueRoot = add(valueRoot, new Node(null), (Node<? extends Comparable, V>) valueNode);
            size++;
        }
        return true;
    }

    /**
     * Recursive add method.
     *
     * @param localRoot The local root of the subtree
     * @param item      The object to be inserted
     * @return The new local root that now contains the
     * inserted item
     * @post The data field addReturn is set true if the item is added to
     * the tree, false if the item is already in the tree.
     */
    private Node add(Node localRoot, Node parent, Node<? extends Comparable, V> item) {
        if (localRoot == null) {
            // item is not in the tree � insert it.
            item.parent = parent;
            return item;
        } else if (item.data.compareTo(localRoot.data) == 0) {
            // item is equal to localRoot.data
            return localRoot;
        } else if (item.data.compareTo(localRoot.data) < 0) {
            // item is less than localRoot.data
            localRoot.left = add(localRoot.left, localRoot, item);
            return localRoot;
        } else {
            // item is greater than localRoot.data
            localRoot.right = add(localRoot.right, localRoot, item);
            return localRoot;
        }
    }

    /**
     * Returns the value associated with a key in O(logn) time.
     *
     * @param key
     * @return value corresponding to key or null if the key does not exist in the map
     */
    public V getValue(K key) {
        Node<K, V> repNode = new Node(key);
        Node<K, V> target = find(keyRoot, repNode);
        if (target != null) {
            return target.link.data;
        }
        return null;
    }

    /**
     * Returns the key associated with a value in O(logn) time. A standard TreeMap would take O(n) time!
     *
     * @param value
     * @return key or null if the value does not exist in the map
     */
    public K getKey(V value) {
        Node<V, K> repNode = new Node(value);
        Node<V, K> target = find(valueRoot, repNode);
        if (target != null) {
            return target.link.data;
        }
        return null;
    }

        /**
     * A recursive helper method to find a target Node in BST.
     *
     * @param localRoot The root of the tree/subtree being visited.
     * @param item The node with the data to be found.
     * @return The node to be found in the BST. Returns null if the target is not in the tree.
     */
    public Node find(Node<? extends Comparable, ?> localRoot, Node<? extends Comparable, ?> item) {
        if (localRoot == null) {
            return null;
        }

        int compareValue = item.data.compareTo(localRoot.data);

        if (compareValue == 0) {
            return localRoot;
        } else if (compareValue > 0) {
            return find(localRoot.right, item);
        } else {
            return find(localRoot.left, item);
        }
    }

    /**
     * Remove the key and corresponding value from the map (and both trees)
     *
     * @param key
     * @return value that was removed that corresponds to key or null if the key does not exist in the map.
     */
    public V remove(K key) {
        if (!containsKey(key)) {
            return null;
        }
        Node<K,V> keyNode = new Node(key);
        Node<K, V> delKeyReturn = new Node(null);
        keyRoot = delete(keyRoot, keyNode, delKeyReturn);

        Node<V, K> valueNode = delKeyReturn.link;
        Node<V, K> delValueReturn = new Node(null);
        valueRoot = delete(valueRoot, valueNode, delValueReturn);

        size--;
        return delValueReturn.data;
    }

    /**
     * Recursive delete method.
     *
     * @param localRoot The root of the current subtree
     * @param item      The item to be deleted
     * @param deleteReturn A node representing the eventually deleted node.
     * @return The modified local root that does not contain
     * the item
     * @post The item is not in the tree;
     * deleteReturn is equal to the deleted item
     * as it was stored in the tree or null
     * if the item was not found.
     */
    public Node delete(Node<K, V> localRoot, Node<? extends Comparable, ?> item, Node deleteReturn) {
        if (localRoot == null) {
            // item is not in the tree.
            return localRoot;
        }

        // Search for item to delete.
        int compResult = item.data.compareTo(localRoot.data);
        if (compResult < 0) {
            // item is smaller than localRoot.data.
            localRoot.left = delete(localRoot.left, item, deleteReturn);
            if (localRoot.left != null) {
                localRoot.left.parent = localRoot;
            }
            return localRoot;
        } else if (compResult > 0) {
            // item is larger than localRoot.data.
            localRoot.right = delete(localRoot.right, item, deleteReturn);
            if (localRoot.right != null) {
                localRoot.right.parent = localRoot;
            }
            return localRoot;
        } else {
            // item is at local root.
            deleteReturn.data = localRoot.data;
            deleteReturn.link = localRoot.link;
            System.out.println("data: " + deleteReturn.data);
            System.out.println("link:" + deleteReturn.link);
            if (!(localRoot.link == null)) {
                localRoot.link.link = null;
            }
            if (localRoot.left == null) {
                // If there is no left child, return right child
                // which can also be null.
                return localRoot.right;
            } else if (localRoot.right == null) {
                // If there is no right child, return left child.
                return localRoot.left;
            } else {
                // Node being deleted has 2 children, replace the data
                // with inorder predecessor.
                if (localRoot.left.right == null) {
                    // The left child has no right child.
                    // Replace the data with the data in the
                    // left child.
                    localRoot.data = localRoot.left.data;
                    // Replace the left child with its left child.
                    localRoot.left = localRoot.left.left;
                    if (localRoot.left != null) {
                        localRoot.left.parent = localRoot;
                    }
                    return localRoot;
                } else {
                    // Search for the inorder predecessor (ip) and
                    // replace deleted node's data with ip.
                    localRoot.data = findLargestChild(localRoot.left);
                    return localRoot;
                }
            }
        }
    }
    
    /**
     * Find the node that is the
     * inorder predecessor and replace it
     * with its left child (if any).
     *
     * @param parent The parent of possible inorder
     *               predecessor (ip)
     * @return The data in the ip
     * @post The inorder predecessor is removed from the tree.
     */
    private K findLargestChild(Node parent) {
        // If the right child has no right child, it is
        // the inorder predecessor.
        if (parent.right.right == null) {
            K returnValue = (K) parent.right.data;
            parent.right = parent.right.left;
            if (parent.right != null) {
                parent.right.parent = parent;
            }
            return returnValue;
        } else {
            return findLargestChild(parent.right);
        }
    }

    /**
     * An inorder traversal of the map ordered by the keys
     *
     * @return a string representing the inorder traversal of the map ordered by keys in the form:
     * "(apple, 3), (banana, 5), (carrot, 4), (date, 6), (eggplant, 1), (fig, 2)"
     */
    public String inOrderTraverseByKeys() {
        Node root = keyRoot;
        StringBuilder sb = new StringBuilder();
        String str = inOrderTraverse(root, sb, true);
        return str.substring(0, str.length() - 2);
    }

    /**
     * An inorder traversal of the map ordered by values
     *
     * @return a string representing the inorder traversal of the map ordered by values in the form:
     * "(eggplant, 1), (fig, 2), (apple, 3), (carrot, 4), (banana, 5), (date, 6)"
     */
    public String inOrderTraverseByValues() {
        Node root = valueRoot;
        StringBuilder sb = new StringBuilder();
        String str = inOrderTraverse(root, sb, false);
        return str.substring(0, str.length() - 2);
    }

    /**
     * Recursive helper method for traversing binary search trees.
     * @param node The currently visited node
     * @param sb StringBuilder to help track the traversed nodes.
     * @param isKeyRoot a boolean represents whether the BST is of key root
     * @return
     */
    public String inOrderTraverse(Node<? extends Comparable, ?> node, StringBuilder sb, boolean isKeyRoot) {
        if (node == null) {
            return "";
        } else {
            inOrderTraverse(node.left, sb, isKeyRoot);
            if (isKeyRoot) {
                sb.append("(" + node.data + ", " + node.link.data + "), ");
            } else {
                sb.append("(" + node.link.data + ", " + node.data + "), ");
            }
            inOrderTraverse(node.right, sb, isKeyRoot);
        }
        return sb.toString();
    }

    /**
     * Checks if the key exists in the map
     *
     * @param key
     * @return true if key was found
     */
    public boolean containsKey(K key) {
        return getValue(key) != null;
    }

    /**
     * Checks if the value exists in the map
     *
     * @param value
     * @return true if found
     */
    public boolean containsValue(V value) {
        return getKey(value) != null;
    }

    /**
     * @return the number of key/value associations contained in the map
     */
    public int size() {
        return size;
    }
}
