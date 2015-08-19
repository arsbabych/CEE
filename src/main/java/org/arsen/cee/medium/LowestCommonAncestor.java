package org.arsen.cee.medium;

import java.io.*;

/**
 * Created by arsen on 19.08.15.
 */
public class LowestCommonAncestor {
    public static void main(String[] args) throws IOException {
        BinarySearchTree tree = new BinarySearchTree();

        tree.addToTree(30);
        tree.addToTree(8);
        tree.addToTree(52);
        tree.addToTree(3);
        tree.addToTree(20);
        tree.addToTree(10);
        tree.addToTree(29);

        File file = new File(args[0]);
        String line, result[];

        if (file.isFile()) {
            InputStream is = new FileInputStream(file);

            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            while ((line = br.readLine()) != null) {
                result = line.split(" ");

                System.out.println(findLowestCommonAncestor(tree, result[0], result[1]));
            }
        }
    }

    private static int findLowestCommonAncestor(BinarySearchTree tree, String value1, String value2) {

        int parent1 = tree.getParent(Integer.parseInt(value1));
        int parent2 = tree.getParent(Integer.parseInt(value2));

        if (parent1 == parent2) {
            return parent1;
        } else {

        }


    }
}

class TreeNode {

    public TreeNode(int value) {
        this.value = value;
        this.level = 0;
    }

    private int value;
    private TreeNode left;
    private TreeNode right;
    private int level;

    public TreeNode getRight() {
        return right;
    }

    public void setRight(TreeNode right) {
        this.right = right;
    }

    public TreeNode getLeft() {
        return left;
    }

    public void setLeft(TreeNode left) {
        this.left = left;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void addNode(TreeNode node) {
        node.setLevel(node.getLevel() + 1);

        if (node.getValue() < this.getValue()) {
            if (this.getLeft() != null) {
                this.getLeft().addNode(node);
            } else {
                this.setLeft(node);
            }
        } else {
            if (this.getRight() != null) {
                this.getRight().addNode(node);
            } else {
                this.setRight(node);
            }
        }
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}

class BinarySearchTree {
    private TreeNode root;

    public void addToTree(int value) {
        if (root == null) {
            root = new TreeNode(value);
        } else if (root.getValue() != value) {
            root.addNode(new TreeNode(value));
        } else {
            throw new RuntimeException("Tree accept only unique values");
        }
    }

    public int getParent(int value) {
        if (root == null) {
            throw new RuntimeException("Tree does not contain nodes yet");
        } else if (root.getValue() == value) {
            throw new RuntimeException("Target node does not have parents");
        } else {

            TreeNode targetNode = root;
            TreeNode parentNode = findParent(targetNode, value);

            if (parentNode != null) {
                return parentNode.getValue();
            } else {
                throw new RuntimeException("Value is not presents in the tree");
            }


            //return root.getParent(value);
        }
    }

    private TreeNode findParent(TreeNode targetNode, int value) {

        if (targetNode.getLeft() != null && targetNode.getRight() != null) {
            if (targetNode.getLeft().getValue() == value || targetNode.getRight().getValue() == value) {
                return targetNode;
            } else {

                TreeNode node = findParent(targetNode.getLeft(), value);

                if (node == null) {
                    return findParent(targetNode.getRight(), value);
                } else {
                    return node;
                }
            }
        } else {
            if (targetNode.getRight() == null && targetNode.getLeft() == null) {
                return null;
            } else if (targetNode.getRight() == null) {
                return findParent(targetNode.getLeft(), value);
            } else {
                return findParent(targetNode.getRight(), value);
            }
        }
    }
}