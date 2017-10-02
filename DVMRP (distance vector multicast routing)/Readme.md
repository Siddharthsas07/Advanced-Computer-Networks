   /* Class node is defined as :
    class Node 
       int val;   //Value
       int ht;      //Height
       Node left;   //Left child
       Node right;   //Right child

   */

static Node insert(Node root,int val)
{
    Node node = new Node();
    node.val = val;
    
    if (root == null)
        return node;
    insertNewNode(root, val, node);
    
    //System.out.println (root.val);
    
    //printHeight(root);
    //printBalance(root);
    
    return root;
    
}

static int height (Node node){
    if (node == null)
        return -1;
    int leftHeight = height(node.left);
    int rightHeight = height(node.right);
    int nodeHeight =  1 + Math.max (leftHeight, rightHeight);
    node.ht = nodeHeight;
    
    return nodeHeight;
}

static void printHeight (Node node) {
    if (node == null)
        return;
    printHeight(node.left);
    System.out.println (node.val + " has height of " + node.ht);
    printHeight(node.right);
}

static void printBalance (Node node) {
    if (node == null)
        return;
    printBalance(node.left);
    System.out.println(node.val + " has balance of " + getBalance(node));
    printBalance(node.right);
}

static Node insertNewNode (Node root, int val, Node node) {
    if (root == null)
        return node;
    if (val < root.val)
        root.left = insertNewNode(root.left, val, node);
    else if (val > root.val)
        root.right = insertNewNode(root.right, val, node);
        //System.out.println("Node on right of node:" + root.val + " is " + root.right.val);
    height(root);
    
    int balance = getBalance(root);
    
    //left left case
    if (balance > 1 && val < root.val){
        rotateRight(root);
    }
    
    //left right case
    if (balance > 1 && val > root.val){
        
    }
    
    //right left case
    if (balance < -1 && val < root.val){
        
    }

    //right right case
    if (balance < -1 && val > root.val){
        
    }

    
    return root;
}

static int getBalance(Node node){
    if (node == null)
        return 0;
    return height(node.left) - height(node.right);
}

static Node rotateRight(Node node){
    Node child = node.left;
    Node childs_rightChild = child.right;
    
    child.right = node;
    node.
}
