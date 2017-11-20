
package forest;
import java.util.Scanner;

/**
 * Implements a very basic recursive descent parser, returning a parse tree
 * that can be evaluated through a node-last depth-first walk of the tree.
 * @author smithk
 */
public class Parser {
    /**
     * this is a bit of absurdity and really is more work than it is worth.
     * I wanted to use the Scanner as a tokenizer, so... But it really is a
     * mess and requires tokens to be space-delimited (which I could have
     * solved, I guess). Just a mess. Nevertheless, it is nice to be able
     * to have a "hasNext" that absorbs without an express call to next when
     * I really don't need the token itself.
     */
    private class TokenStream {
        final static int NUMBER = 1;
        private Scanner stream;
        
        TokenStream (String is) {
            stream = new Scanner (is);
        }
        
        boolean hasNext() {
            return stream.hasNext();
        }
        
        boolean hasNext(int tokenType, boolean absorb) {
            boolean result;
            switch (tokenType) {
                case NUMBER:
                    result = stream.hasNextInt(); 
                    break;
                case '+': 
                case '(':
                case ')':
                case '*':
                    result = stream.hasNext ("\\" + (char)tokenType); 
                    break;
                default:
                    result = stream.hasNext (String.valueOf((char)tokenType));
                    break;
            }
            if (result && absorb)
                stream.next();
            return result;
        }
        
        boolean hasNext (int tokenType) {
            return hasNext (tokenType, true);
        }
        
        String next() {
            return stream.next();
        }
    }
    
    private TokenStream tokens;
    
    Parser (String src) {
        tokens = new TokenStream (src);
    }
    
    public boolean isEOF (){
        return !tokens.hasNext();
    }
    
    /**
     * we should define the language that we will be parsing. We will use
     * a modified BNF for this purpose, but only for documentation.
     * 
     * EXPR ::= TERM '+' EXPR | TERM '-' EXPR | TERM
     * TERM ::= FACTOR '*' TERM | FACTOR '/' TERM | FACTOR
     * FACTOR ::= '(' EXPR ')' | '-' FACTOR | NUMBER
     * 
     * however, because that generates a right associative tree, we will
     * use the "typical" transformation.
     * 
     * EXPR ::= TERM { +|- TERM }
     * TERM ::= FACTOR { *|/ FACTOR }
     * FACTOR ::= '(' EXPR ')' | '-' FACTOR | NUMBER
     */
    
    private BinaryTree expression() {
        BinaryTree term = term();
        boolean addop = false;
        while ((addop = tokens.hasNext('+')) || tokens.hasNext('-')) {
            term = new BinaryTree(addop ? "+" : "-", term, term());
            addop = false;
        }
        return term;
    }
    
    private BinaryTree term() {
        BinaryTree factor = factor();
        boolean mulop = false;
        while ((mulop = tokens.hasNext('*')) || tokens.hasNext('/')) {
            factor = new BinaryTree(mulop ? "*" : "/", factor, factor());
            mulop = false;
        }
        return factor;
    }
    
    private BinaryTree factor() {
        BinaryTree factor;
        if (tokens.hasNext('(')) {
            factor = expression();
            if (!tokens.hasNext(')'))
                throw new IllegalStateException();
        }
        else if (tokens.hasNext('-')) {
            factor = new BinaryTree ("_", factor(), null);
        }
        else if (tokens.hasNext(TokenStream.NUMBER, false))
            factor = new BinaryTree(tokens.next());
        else
            throw new IllegalStateException();
        return factor;
    }
    
    public static void main (String[] args) {
        Parser p = new Parser ("( 2 - ( 45 - - 37 - 10 ) ) * 6 / 2 / 11 - ( 4 + 5 ) ");
        BinaryTree parsed = p.expression();
        if (!p.isEOF())
            throw new IllegalStateException();
        
        System.out.println(parsed);
    }
}
