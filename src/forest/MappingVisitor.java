package forest;

import java.util.HashMap;

/**
 * @author smithk
 */
public class MappingVisitor implements Visitor {
    private HashMap<Integer,Character> decode;
    private HashMap<Character,Integer> encode;
    
    private String directions;
    
    public MappingVisitor() {
        directions = "1";
        decode = new HashMap();
        encode = new HashMap();
    }
    
    public String encode(String clearText) {
        String encoding = "";
        for (int ndx = 0; ndx < clearText.length(); ndx++)
            encoding += String.format ("%d ", encode.get(clearText.charAt(ndx)).intValue());
        return encoding;
    }
    
    public void visit(BinaryTree host) {
        if (host.leftBranch != null) {
            directions += '0';
            host.leftBranch.acceptVisitor (this);
            directions = directions.substring (0, directions.length()-1);
        }
        if (host.rightBranch != null) {
            directions += '1';
            host.rightBranch.acceptVisitor (this);
            directions = directions.substring (0, directions.length()-1);
        }
        
        Integer encoding = Integer.parseInt (directions, 2);
        encode.put ((Character)host.getFruit(), encoding);
        decode.put (encoding, (Character)host.getFruit());
        
        System.out.format("%s %s %d\n", host.getFruit(), directions, encoding);            
    }
}
