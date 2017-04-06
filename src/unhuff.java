import java.io.FileInputStream;
import java.io.FileNotFoundException;

// -------------------------------------------------------------------------
/**
 *  Huff class for running the decompression. Code for compression + decompression is in the FileCompressor class.
 *
 *  @author Matt Addessi, Ryan Morgan
 *  @version May 8, 2016
 */
public class unhuff
{
    public static void main(String[] args) {
        FileCompressor huffModel = new FileCompressor();
        String filename = args[0];

        BitInputStream bits3 = null;

        try
        {
            bits3 = new BitInputStream(new FileInputStream(filename));
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        BitOutputStream out1 = new BitOutputStream("UNHUFFED"+filename);
        huffModel.uncompress(bits3, out1);
    }

}
