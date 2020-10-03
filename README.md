# Bidirectional TreeMap

The implementation of a Treemap that allows both searching by keys and searching by values run in O(1).

### Project description

A Bidirectional TreeMap is similar to Java's TreeMap class in that it stores associations between keys 
and values. The standard TreeMap enables looking up a value by the key in O(logn) time, while also enabling 
iteration over the keys in sorted order. In addition to searching by key in O(logn) time, a Bidirectional 
TreeMap also allows you to search by value in O(logn) time.

The typical implementation of a TreeMap stores nodes containing the key and value in a single binary search
tree, ordered by the keys. This Bidirectional TreeMap will instead be stored in two distinct binary search trees,
one storing the keys and one storing the values. The associations are stored as bidirectional links between
the nodes of the two trees.

Like a TreeMap, it is not possible to edit the key of an association, because that edit may disrupt the overall
binary search ordering property of the tree. Because the values of our BidirectionalTreeMap are also stored in
their own binary search tree, we must also prohibit editing of the values in a BidirectionalTreeMap. If the key
or the value of an association needs to be changed, that data must be removed from the tree and re-inserted, i.e. 
you cannot just call put again with the same key but a different value.