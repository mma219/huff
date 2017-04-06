import java.io.IOException;
import java.io.InputStream;

// -------------------------------------------------------------------------
/**
 *  Write a one-sentence summary of your class here.
 *  Follow it with additional details about its purpose, what abstraction
 *  it represents, and how to use it.
 *
 *  @author Matt Addessi, Ryan Morgan
 *  @version Apr 9, 2016
 */
public class CharCounter implements ICharCounter
{
    private int[] chars = new int[256];

    @Override
    public int getCount(int ch)
    {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int countAll(InputStream stream)
        throws IOException
    {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void add(int i)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void set(int i, int value)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void clear()
    {
        // TODO Auto-generated method stub

    }

}
