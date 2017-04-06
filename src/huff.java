import java.io.FileInputStream;
import java.io.FileNotFoundException;

// -------------------------------------------------------------------------
/**
 *  Huff class for running the compression. Code for compression + decompression is in the FileCompressor class.
 *
 *  @author Matt Addessi, Ryan Morgan
 *  @version May 8, 2016
 */
public class huff
{
    public static void main(String[] args) {
        BitInputStream bits = null;
        try
        {
            bits = new BitInputStream(new FileInputStream(args[1]));
        }
        catch (FileNotFoundException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        boolean force = true;
        if(args[0].equals("true")) {
            force = true;
        }
        if(args[0].equals("false")) {
            force = false;
        }
        String filename = args[1];
        FileCompressor huffModel = new FileCompressor();
        huffModel.initialize(bits);
        huffModel.showCounts();
        huffModel.showCodings();
        //String filepath = args[1];
        try
        {
            bits = new BitInputStream(new FileInputStream(filename));
        }
        catch (FileNotFoundException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        huffModel.write(bits, ("HUFFED"+filename), force);

    }

}
