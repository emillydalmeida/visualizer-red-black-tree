import java.util.Scanner;

public class Main {
    private static RedBlackTree<Integer> tree;
    private static Scanner scanner;
    
    public static void main(String[] args) {
        tree = new RedBlackTree<>();
        scanner = new Scanner(System.in);
        
        boolean continueProgram = true;
        
        while (continueProgram) {
            displayMenu();
            int option = readOption();
            
            switch (option) {
                case 1:
                    insertValue();
                    break;
                case 2:
                    removeValue();
                    break;
                case 3:
                    searchValue();
                    break;
                case 4:
                    showStatistics();
                    break;
                case 5:
                    insertMultipleValues();
                    break;
                case 6:
                    clearTree();
                    break;
                case 0:
                    continueProgram = false;
                    System.out.println("Thank you for using the Red-Black Tree Visualizer!");
                    break;
                default:
                    System.out.println("Invalid option! Please try again.");
            }
            
            if (continueProgram) {
                System.out.println("\nPress Enter to continue...");
                scanner.nextLine();
            }
        }
        
        scanner.close();
    }
    
    private static void displayMenu() {
        System.out.println("\n┌──────────────────────────────────────────┐");
        System.out.println("│                  MENU                    │");
        System.out.println("├──────────────────────────────────────────┤");
        System.out.println("│ 1. Insert value                         │");
        System.out.println("│ 2. Remove value                         │");
        System.out.println("│ 3. Search value                         │");
        System.out.println("│ 4. Show statistics                      │");
        System.out.println("│ 5. Insert multiple values               │");
        System.out.println("│ 6. Clear tree                           │");
        System.out.println("│ 0. Exit                                  │");
        System.out.println("└──────────────────────────────────────────┘");
        System.out.print("Choose an option: ");
    }
    
    private static int readOption() {
        try {
            int option = Integer.parseInt(scanner.nextLine().trim());
            return option;
        } catch (NumberFormatException e) {
            return -1; 
        }
    }
    
    private static int readValue(String message) {
        while (true) {
            try {
                System.out.print(message);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid integer number.");
            }
        }
    }
    
    private static void insertValue() {
        System.out.println("\n=== INSERTION ===");
        int value = readValue("Enter the value to insert: ");
        
        System.out.println("\nPerforming insertion...");
        boolean success = tree.insert(value);
        
        if (success) {
            System.out.println("Value " + value + " inserted successfully!");
            showQuickStatistics();
        } else {
            System.out.println("Value " + value + " already exists in the tree!");
        }
    }
    
    private static void removeValue() {
        if (isEmpty()) {
            System.out.println("\n=== REMOVAL ===");
            System.out.println("The tree is empty! No elements to remove.");
            return;
        }
        
        System.out.println("\n=== REMOVAL ===");
        showQuickStatistics();
        
        int value = readValue("Enter the value to remove: ");
        
        System.out.println("\nPerforming removal...");
        boolean success = tree.delete(value);
        
        if (success) {
            System.out.println("Value " + value + " removed successfully!");
            showQuickStatistics();
        } else {
            System.out.println("Value " + value + " not found in the tree!");
        }
    }
    
    private static void searchValue() {
        if (isEmpty()) {
            System.out.println("\n=== SEARCH ===");
            System.out.println("The tree is empty! No elements to search.");
            return;
        }
        
        System.out.println("\n=== SEARCH ===");
        showQuickStatistics();
        
        int value = readValue("Enter the value to search: ");
        
        System.out.println("\nPerforming search...");
        Node<Integer> found = tree.search(value);
        
        if (found != null) {
            System.out.println("✓ Value " + value + " found!");
            System.out.println("  Node color: " + found.color);
            
            if (found.parent != null) {
                System.out.println("  Parent: " + found.parent.data);
            } else {
                System.out.println("  This is the root node!");
            }
            
            if (found.left != null) {
                System.out.println("  Left child: " + found.left.data);
            }
            if (found.right != null) {
                System.out.println("  Right child: " + found.right.data);
            }
        } else {
            System.out.println("✗ Value " + value + " not found in the tree!");
        }
    }
    
    private static void showStatistics() {
        System.out.println("\n=== TREE STATISTICS ===");
        
        int totalNodes = countNodes(tree.root);
        
        if (totalNodes == 0) {
            System.out.println("The tree is empty!");
            return;
        }
        
        int redNodes = countRedNodes(tree.root);
        int blackNodes = totalNodes - redNodes;
        int height = calculateHeight(tree.root);
        
        System.out.println("Total nodes: " + totalNodes);
        System.out.println("Red nodes: " + redNodes);
        System.out.println("Black nodes: " + blackNodes);
        System.out.println("Tree height: " + height);
        System.out.println("Root: " + tree.root.data + " (" + tree.root.color + ")");
        
        Node<Integer> smallest = findSmallest(tree.root);
        Node<Integer> largest = findLargest(tree.root);
        
        if (smallest != null && largest != null) {
            System.out.println("Smallest value: " + smallest.data);
            System.out.println("Largest value: " + largest.data);
        }
    }
    
    private static void insertMultipleValues() {
        System.out.println("\n=== MULTIPLE INSERTION ===");
        System.out.println("Enter values separated by spaces:");
        System.out.print("Example: 10 20 30 15 25: ");
        
        try {
            String line = scanner.nextLine();
            String[] values = line.split(" ");
            int inserted = 0;
            int duplicates = 0;
            
            System.out.println("\nPerforming insertions...");
            
            for (String valueStr : values) {
                if (!valueStr.trim().isEmpty()) {
                    try {
                        int value = Integer.parseInt(valueStr.trim());
                        if (tree.insert(value)) {
                            inserted++;
                            System.out.println("✓ " + value + " inserted");
                        } else {
                            duplicates++;
                            System.out.println("✗ " + value + " already exists");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("✗ Invalid value ignored: " + valueStr);
                    }
                }
            }
            
            System.out.println("\nSummary:");
            System.out.println("Inserted: " + inserted);
            System.out.println("Duplicates: " + duplicates);
            
        } catch (Exception e) {
            System.out.println("Error processing values!");
        }
    }
    
    private static void clearTree() {
        System.out.println("\n=== CLEAR TREE ===");
        System.out.print("Are you sure you want to clear the tree? (y/N): ");
        
        String response = scanner.nextLine().toLowerCase();
        if (response.equals("y") || response.equals("yes")) {
            tree = new RedBlackTree<>();
            System.out.println("Tree cleared successfully!");
        } else {
            System.out.println("Operation cancelled.");
        }
    }
    
    private static void showQuickStatistics() {
        int totalNodes = countNodes(tree.root);
        if (totalNodes == 0) {
            System.out.println("Current state: Empty tree");
        } else {
            System.out.println("Current state: " + totalNodes + " nodes, root: " + tree.root.data + " (" + tree.root.color + ")");
        }
    }
    
    private static boolean isEmpty() {
        return tree.root == null;
    }
    
    private static int countNodes(Node<Integer> node) {
        if (node == null) return 0;
        return 1 + countNodes(node.left) + countNodes(node.right);
    }
    
    private static int countRedNodes(Node<Integer> node) {
        if (node == null) return 0;
        int count = (node.color == Color.RED) ? 1 : 0;
        return count + countRedNodes(node.left) + countRedNodes(node.right);
    }
    
    private static int calculateHeight(Node<Integer> node) {
        if (node == null) return 0;
        return 1 + Math.max(calculateHeight(node.left), calculateHeight(node.right));
    }
    
    private static Node<Integer> findSmallest(Node<Integer> node) {
        if (node == null) return null;
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }
    
    private static Node<Integer> findLargest(Node<Integer> node) {
        if (node == null) return null;
        while (node.right != null) {
            node = node.right;
        }
        return node;
    }
}
