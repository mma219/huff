import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

// -------------------------------------------------------------------------
/**
 *  Write a one-sentence summary of your class here.
 *  Follow it with additional details about its purpose, what abstraction
 *  it represents, and how to use it.
 *
 *  @author Ryan Morgan, Matt Addessi
 *  @version Apr 9, 2016
 */
public class FileCompressor implements IHuffModel
{

    private int[] myCounts = new int[256];

    /**
     *
     */
    Stack myStack = new Stack();
    private ArrayList<String> encodings = new ArrayList<String>();
    private String[] encode1 = new String[256];
    private char[] encode2 = new char[256];
    private int counting = 0;
    BitOutputStream out;
    BitOutputStream out1;

    // ----------------------------------------------------------
    /**
     * Place a description of your method here.
     * @param args
     */

    // ----------------------------------------------------------
    /**
     * Place a description of your method here.
     */
    public void test() {
        for (int i = 0; i < encode1.length; i++) {
            if(encode1[i] != null) {
                //System.out.print(encode1[i]);
                //System.out.println(encode2[i]);

            }
        }


    }

    // ----------------------------------------------------------
    /**
     * Place a description of your method here.
     * @param root
     * @param out
     */
    public void traversalAgain(HuffBaseNode root, BitOutputStream out) {
        if(root != null) {
            if (root.isLeaf()) {
                out.write(1, 1);
                out.write(9, ((HuffLeafNode)root).element());
            }
            else {
                out.write(1, 0);
                traversalAgain(((HuffInternalNode)root).left(), out);
                traversalAgain(((HuffInternalNode)root).right(), out);
            }
        }

    }

    public void write(InputStream stream, String file, boolean force) {
       int compSize = compressedSize();
       System.out.println("The compressed size is " + compSize);
       int ogSize = originalSize();
       System.out.println("The original size is " + ogSize);
       if (force == false) {
           if (ogSize < compSize) {
               System.out.println("Compression was not performed. The original size is "+ ogSize + " and the compressed size is " + compSize + ".");
               return;
           }
       }
       out = new BitOutputStream(file);
       out.write(BITS_PER_INT, MAGIC_NUMBER);
       traversalAgain(this.buildTree().root(), out);
       for (int x = 0; x < 256; x++) {
           if (encode1[x] != null) {
               //System.out.println(x + " " + encode1[x] + " " + myCounts[x] + " " + encode1[x] + " " + encode2[x]);
           }
       }
       //System.out.println("PREDICTED SIZE " + pBits);

       int inbits = 0;
       BitInputStream bits = (BitInputStream) stream;

       try
       {
           while ((inbits = bits.read(BITS_PER_WORD)) != -1) {
               for(int x = 0; x < 256; x++) {
                   if(encode2[x] == (char) inbits) {
                       for(int y = 0; y < encode1[x].toCharArray().length; y++) {
                           out.write(1, encode1[x].toCharArray()[y]);
                       }
                   }
               }

           }
       }

       catch (IOException e)
       {
           // TODO Auto-generated catch block
           e.printStackTrace();
       }

       //out.write(1,1);
       //out.write(9, PSEUDO_EOF);
       for (int x = 0; x < 256; x++) {
           if(encode2[x] == PSEUDO_EOF) {
               for(int y = 0; y < encode1[x].toCharArray().length; y++) {
                   out.write(1, encode1[x].toCharArray()[y]);
               }
           }
       }
       out.close();
       System.out.println("Compression complete.");


    }

    public int originalSize() {
        int returnable = 0;
        for (int i = 0; i < 256; i++) {
            if (myCounts[i] != 0) {
                returnable += (myCounts[i] * 8);
            }
        }
        return returnable / 8;
    }
    // ----------------------------------------------------------
    /**
     * Place a description of your method here.
     * @return
     */
    public int countNonNull() {
        int count = 0;
        for(int x = 0; x < 256; x++) {
            if (myCounts[x] != 0) {
                count++;
            }

        }
        return count;
    }

    public int compressedSize() {
        int bitss = 0;
        bitss += 32;
        bitss += treeCounter(buildTree().root());
        bitss += fileSize();
        return bitss / 8;
    }

    public int fileSize() {
        int size = 0;
        for(int i = 0; i < 256; i++) {
            if(encode1[i] != null) {
                for (int x = 0; x < 256; x++) {
                    if(x == (int) encode2[i]) {
                        size += ((encode1[i].length() * myCounts[x]));
                    }

                }
            }
        }
        return size;
    }

    public int treeCounter(HuffBaseNode node) {
        if (node == null) {
            return 0;
        }
        if (node.isLeaf()) {
            return 10;
        }
        else {
            return 1 + treeCounter(((HuffInternalNode)node).left()) + treeCounter(((HuffInternalNode)node).right());
        }

    }

    // ----------------------------------------------------------
    /**
     * Place a description of your method here.
     * @return
     */
    public MinHeap createTrees() {
        int counter = 0;
        HuffTree[] myTrees = new HuffTree[countNonNull() + 1];
        for (int x = 0; x < 256; x++) {
            if (myCounts[x] != 0) {
                myTrees[counter] = new HuffTree((char) x, myCounts[x]);
                counter++;
            }
        }
        myTrees[counter] = new HuffTree((char)PSEUDO_EOF, 1);
        MinHeap Hheap = new MinHeap(myTrees, counter+1, counter+1);
        return Hheap;

    }

    // ----------------------------------------------------------
    /**
     * Place a description of your method here.
     * @return
     */
    HuffTree buildTree() {
        HuffTree tmp1, tmp2, tmp3 = null;
        MinHeap Hheap = createTrees();
        while (Hheap.heapsize() > 1) { // While two items left
          tmp1 = (HuffTree)Hheap.removemin();
          tmp2 = (HuffTree)Hheap.removemin();
          tmp3 = new HuffTree(tmp1.root(), tmp2.root(),
                                   tmp1.weight() + tmp2.weight());
          Hheap.insert(tmp3);   // Return new tree to heap
        }
        return tmp3;            // Return the tree
      }

    // ----------------------------------------------------------
    /**
     * Place a description of your method here.
     * @param rt
     */
    public void preorder(HuffBaseNode rt) {

        String holder = "";
        if (rt == null) return; // Empty subtree - do nothing
        if (rt.isLeaf()) {

            for (int i = 0; i < myStack.size(); i++) {
                System.out.print(myStack.get(i));
                holder += myStack.get(i);
            }


         encode1[counting] = holder;
         encode2[counting] = ((HuffLeafNode)rt).element();
         counting++;
         holder = "";
         myStack.pop();
         System.out.println(" encodes " + ((HuffLeafNode)rt).element());

        }
        else {

            myStack.push(0);
            preorder(((HuffInternalNode)rt).left());    // Process all nodes in left
            myStack.push(1);
            preorder(((HuffInternalNode)rt).right());   // Process all nodes in right
            if(myStack.size() > 0) {
                myStack.pop();
            }
        }


     }

    @Override
    public void showCodings()
    {
        // TODO Auto-generated method stub
        this.createTrees();
        System.out.println("Showing codings...");
        this.preorder(this.buildTree().root());
        System.out.println("End of showing codings...");
        System.out.println("-------------------------");
    }

    @Override
    public void showCounts()
    {
        System.out.println("Showing counts...");
        for (int x = 0; x < 256; x++) {
            if ((myCounts[x]) != 0) {
                System.out.println(((char) x) + " occurs " + myCounts[x] + " times.");
            }
        }
        System.out.println("End of show counts...");
        System.out.println("-------------------------");



    }

    @Override
    public void initialize(InputStream stream)
    {
        int inbits = 0;
        BitInputStream bits = (BitInputStream) stream;

        for (int x = 0; x < 256; x++) {
            myCounts[x] = 0;
        }
        try
        {
            while ((inbits = bits.read(BITS_PER_WORD)) != -1) {
                //System.out.println(inbits);
                myCounts[inbits] += 1;

            }
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


    @Override
    public void uncompress(InputStream in, OutputStream out)
    {
        //part 1 of decompression... check magic number
        BitOutputStream bOut = ((BitOutputStream) out);
        BitInputStream bIn = ((BitInputStream) in);
        int magic = 0;
        try
        {
            magic = bIn.read(BITS_PER_INT);

        }
        catch (IOException e2)
        {
            // TODO Auto-generated catch block
            e2.printStackTrace();
        }
        if (magic != MAGIC_NUMBER) { //if not magic number, throw exception
            try
            {
                throw new IOException("magic number not right");
            }
            catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
         }

      //part 3 of decompression, use the Huffman tree built from step 2 to write decompressed file
        HuffBaseNode node = null;
        try
        {
            node = rebuildTree(bIn);
        }
        catch (IOException e1)
        {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } //(bit)
        int bits = 0;
        HuffBaseNode temp = node;


        while (true)
        {
            try
            {
                bits = bIn.read(1);
            }
            catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if (bits == -1)
            {
                try
                {
                    throw new IOException("unexpected end of input file");

                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                //break;
            }


            else
            {
                // use the zero/one value of the bit read
                // to traverse Huffman coding tree
                // if a leaf is reached, decode the character and print UNLESS
                // the character is pseudo-EOF, then decompression done

                if (((bits & 1) == 0) ) { // read a 0, go left in tree
                    temp = ((HuffInternalNode)temp).left();
                }

                else  { // read a 1, go right in tree
                    temp = ((HuffInternalNode)temp).right();
                }
                if (temp.isLeaf()) //at leaf-node in tree
                  {
                      if (((HuffLeafNode)temp).element() == PSEUDO_EOF) {
                        //if leaf node stores PSEUDO character
                            break; // out of loop
                        }
                        else {
                        //write character stored in leaf-node
                            bOut.write(BITS_PER_WORD, ((int)((HuffLeafNode)temp).element()));
                            temp = node;
                        }
                  }

             }

        }
        bIn.close();
        bOut.close();
        System.out.println("Decompression complete.");


    }

    // ----------------------------------------------------------
    /**
     * Place a description of your method here.
     * @param in
     * @return tree
     * @throws IOException
     */
    private HuffBaseNode rebuildTree(BitInputStream in) throws IOException {

        int bits = in.read(1);
        if (bits == 0) {
            return new HuffInternalNode(rebuildTree(in), rebuildTree(in), 1);
            //return HuffInternalNode
        }

        else if (bits == 1) {
            //read 1 = go right in tree
            bits = in.read(9);
            return new HuffLeafNode(((char)bits), 1);

        }


        return null;
    }

}
