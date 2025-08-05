public class RedBlackTree <T extends Comparable<T>> {
    private Node<T> root;

    public RedBlackTree() {
        this.root = null;
    }

    public boolean insert(T data) {
        Node<T> newNode = new Node<>(data);
        newNode.color = Color.RED;
        newNode.left = null;
        newNode.right = null;
        newNode.parent = null;

        if (root == null){
            root = newNode;
            root.color = Color.BLACK;
            return true;
        }

        Node<T> current = root; // To traverse the tree
        Node<T> parent = null; // To keep track of the parent node

        while (current != null) {
            parent = current;
            int comparison = data.compareTo(current.data);

            if (comparison < 0) {
                current = current.left;
            } else if (comparison > 0) {
                current = current.right;
            } else {
                return false; // Element already exists, duplicate not allowed
            }
        }

        newNode.parent = parent;
        if (data.compareTo(parent.data) < 0) {
            parent.left = newNode;
        } else {
            parent.right = newNode;
        }

        fixBalance(newNode);
        return true;
    }

    public boolean delete(T data) {
        Node<T> nodeToDelete = search(data);

        if (nodeToDelete == null) {
            return false; // Node not found
        }
        
        Node<T> nodeToReplace;
        Node<T> nodeToFix;
        Color originalColor = nodeToDelete.color;
        
        // Case 1: Node has no left child
        if (nodeToDelete.left == null) {
            nodeToReplace = nodeToDelete.right;
            transplant(nodeToDelete, nodeToDelete.right);
            nodeToFix = nodeToReplace;
        }

        // Case 2: Node has no right child
        else if (nodeToDelete.right == null) {
            nodeToReplace = nodeToDelete.left;
            transplant(nodeToDelete, nodeToDelete.left);
            nodeToFix = nodeToReplace;
        }
        
        // Case 3: Node has both children
        else {
            // Find successor (minimum in right subtree)
            Node<T> successor = findMinimum(nodeToDelete.right);
            originalColor = successor.color;
            nodeToReplace = successor.right;
            
            if (successor.parent == nodeToDelete) {
                if (nodeToReplace != null) {
                    nodeToReplace.parent = successor;
                }
            } else {
                transplant(successor, successor.right);
                successor.right = nodeToDelete.right;
                successor.right.parent = successor;
            }
            
            transplant(nodeToDelete, successor);
            successor.left = nodeToDelete.left;
            successor.left.parent = successor;
            successor.color = nodeToDelete.color;
            nodeToFix = nodeToReplace;
        }
        
        // Fix Red-Black Tree violations if a black node was deleted
        if (originalColor == Color.BLACK) {
            deleteFixup(nodeToFix);
        }
        
        return true;
    } 

    public Node<T> search(T data) {
        Node<T> current = root;
        
        while (current != null) {
            int comparison = data.compareTo(current.data);
            
            if (comparison < 0) {
                current = current.left;
            } else if (comparison > 0) {
                current = current.right;
            } else {
                return current; // Found the node
            }
        }
        
        return null; // Node not found
    } 

    public void rotateLeft(Node<T> x) {
        Node<T> y = x.right;
        x.right = y.left;
        if (y.left != null) {
            y.left.parent = x;
        }
        y.parent = x.parent;
        if (x.parent == null) {
            root = y;
        } else if (x == x.parent.left) {
            x.parent.left = y;
        } else {
            x.parent.right = y;
        }
        y.left = x;
        x.parent = y;
    }
    public void rotateRight(Node<T> y) {
        Node<T> x = y.left;
        y.left = x.right;
        if (x.right != null) {
            x.right.parent = y;
        }
        x.parent = y.parent;
        if (y.parent == null) {
            root = x;
        } else if (y == y.parent.right) {
            y.parent.right = x;
        } else {
            y.parent.left = x;
        }
        x.right = y;
        y.parent = x;
    }
    public void fixBalance(Node<T> node) {
        while (node != null && node != root && node.parent != null && node.parent.color == Color.RED) {
            if (node.parent == node.parent.parent.left) {
                Node<T> uncle = node.parent.parent.right;
                if (uncle != null && uncle.color == Color.RED) {
                    // Case 1: Uncle is red
                    node.parent.color = Color.BLACK;
                    uncle.color = Color.BLACK;
                    node.parent.parent.color = Color.RED;
                    node = node.parent.parent;
                } else {
                    if (node == node.parent.right) {
                        // Case 2: Node is right child
                        node = node.parent;
                        rotateLeft(node);
                    }
                    // Case 3: Node is left child
                    node.parent.color = Color.BLACK;
                    node.parent.parent.color = Color.RED;
                    rotateRight(node.parent.parent);
                }
            } else {
                // Mirror cases for when parent is right child
                Node<T> uncle = node.parent.parent.left;
                if (uncle != null && uncle.color == Color.RED) {
                    // Case 1: Uncle is red
                    node.parent.color = Color.BLACK;
                    uncle.color = Color.BLACK;
                    node.parent.parent.color = Color.RED;
                    node = node.parent.parent;
                } else {
                    if (node == node.parent.left) {
                        // Case 2: Node is left child
                        node = node.parent;
                        rotateRight(node);
                    }
                    // Case 3: Node is right child
                    node.parent.color = Color.BLACK;
                    node.parent.parent.color = Color.RED;
                    rotateLeft(node.parent.parent);
                }
            }
        }
        if (root != null) {
            root.color = Color.BLACK;
        }
    }

    // Helper method to replace one subtree with another
    private void transplant(Node<T> u, Node<T> v) {
        if (u.parent == null) {
            root = v;
        } else if (u == u.parent.left) {
            u.parent.left = v;
        } else {
            u.parent.right = v;
        }
        
        if (v != null) {
            v.parent = u.parent;
        }
    }

    // Helper method to find minimum node in a subtree
    private Node<T> findMinimum(Node<T> node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    // Method to fix Red-Black Tree violations after deletion
    private void deleteFixup(Node<T> x) {
        while (x != root && (x == null || x.color == Color.BLACK)) {
            if (x != null && x.parent != null && x == x.parent.left) {
                Node<T> sibling = x.parent.right;
                
                // Case 1: Sibling is red
                if (sibling != null && sibling.color == Color.RED) {
                    sibling.color = Color.BLACK;
                    x.parent.color = Color.RED;
                    rotateLeft(x.parent);
                    sibling = x.parent.right;
                }
                
                // Case 2: Sibling's children are both black
                if (sibling != null && 
                    (sibling.left == null || sibling.left.color == Color.BLACK) &&
                    (sibling.right == null || sibling.right.color == Color.BLACK)) {
                    sibling.color = Color.RED;
                    x = x.parent;
                } else if (sibling != null) {
                    // Case 3: Sibling's right child is black
                    if (sibling.right == null || sibling.right.color == Color.BLACK) {
                        if (sibling.left != null) {
                            sibling.left.color = Color.BLACK;
                        }
                        sibling.color = Color.RED;
                        rotateRight(sibling);
                        sibling = x.parent.right;
                    }
                    
                    // Case 4: Sibling's right child is red
                    if (sibling != null) {
                        sibling.color = x.parent.color;
                        x.parent.color = Color.BLACK;
                        if (sibling.right != null) {
                            sibling.right.color = Color.BLACK;
                        }
                        rotateLeft(x.parent);
                        x = root;
                    }
                }
            } else if (x != null && x.parent != null) {
                // Mirror cases for when x is a right child
                Node<T> sibling = x.parent.left;
                
                if (sibling != null && sibling.color == Color.RED) {
                    sibling.color = Color.BLACK;
                    x.parent.color = Color.RED;
                    rotateRight(x.parent);
                    sibling = x.parent.left;
                }
                
                if (sibling != null &&
                    (sibling.right == null || sibling.right.color == Color.BLACK) &&
                    (sibling.left == null || sibling.left.color == Color.BLACK)) {
                    sibling.color = Color.RED;
                    x = x.parent;
                } else if (sibling != null) {
                    if (sibling.left == null || sibling.left.color == Color.BLACK) {
                        if (sibling.right != null) {
                            sibling.right.color = Color.BLACK;
                        }
                        sibling.color = Color.RED;
                        rotateLeft(sibling);
                        sibling = x.parent.left;
                    }
                    
                    if (sibling != null) {
                        sibling.color = x.parent.color;
                        x.parent.color = Color.BLACK;
                        if (sibling.left != null) {
                            sibling.left.color = Color.BLACK;
                        }
                        rotateRight(x.parent);
                        x = root;
                    }
                }
            } else {
                break;
            }
        }
        
        if (x != null) {
            x.color = Color.BLACK;
        }
    }

}