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

    
}
