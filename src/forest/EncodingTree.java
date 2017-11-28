
package forest;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * @author smithk
 */
public class EncodingTree {
    private BinaryTree<Character> encodingTree;
    
    public void saveTree(String where) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(where));
        oos.writeObject (encodingTree);
        oos.close();
    }
    
    public void loadTree(String where) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(where));
        encodingTree = (BinaryTree<Character>)ois.readObject();
        ois.close();
    }
    
    public BinaryTree<Character> rebuildTree (String encodable, String encoded) {
        BinaryTree<Character> encodingTree = null;
        do {
            int ndx = Integer.parseInt (encoded.substring(0, 2));
            encoded = encoded.substring(2);
            
            char code = encodable.charAt(ndx);
            encodable = encodable.substring(0, ndx) + encodable.substring(ndx+1);
            if (encodingTree == null)
                encodingTree = new BinaryTree(code);
            else
                encodingTree.insert (code);
        } while (encodable.length() != 0);
        return encodingTree;
    }
    
    public String buildTree(String encodable) {
        StringBuilder encoded = new StringBuilder();
        while (encodable.length() > 0) {
            int ndx = (int)(Math.random() * encodable.length());
            char code = encodable.charAt (ndx);
            encodable = encodable.substring (0, ndx) + encodable.substring(ndx+1);
            
            if (encodingTree == null)
                encodingTree = new BinaryTree(code);
            else 
                encodingTree.insert (code);
            encoded.append (String.format("%02d", ndx));
        }
        return encoded.toString();
    }
    
    public static void main(String[] args) throws Exception {
        final String alphabet = "abcdefghijk";
        EncodingTree et = new EncodingTree();
        System.out.println(et.rebuildTree (alphabet, et.buildTree(alphabet)).equals(et.encodingTree)); 
        System.out.println(et.encodingTree);

        et.saveTree ("tree.dat");
        
        // et.loadTree ("tree.dat");
        // System.out.println(et.encodingTree);
        
        MappingVisitor mv = new MappingVisitor();
        et.encodingTree.acceptVisitor (mv);
        System.out.println(mv.encode(alphabet));
    }
}
