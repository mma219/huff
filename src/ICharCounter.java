/**
 * An interface for initializing and retrieving chunk/character
 * counts.
 * @author Owen Astrachan
 */

import java.io.InputStream;
import java.io.IOException;
// -------------------------------------------------------------------------
/**
 *  Write a one-sentence summary of your class here.
 *  Follow it with additional details about its purpose, what abstraction
 *  it represents, and how to use it.
 *
 *  @author ryanmorgan
 *  @author
 *  @version Apr 9, 2016
 */
public interface ICharCounter {

    /**
     * Returns the count associated with specified character.
     * @param ch is the chunk/character for which count is requested
     * @return count of specified chunk
     * @throws some kind of exception if ch isn't a valid chunk/character
     */
    public int getCount(int ch);

    /**
     * Initialize state by counting bits/chunks in a stream
     * @param stream is source of data
     * @return count of all chunks/read
     * @throws IOException if reading fails
     */
    public int countAll(InputStream stream) throws IOException;

    /**
     * Update state to record one occurrence of specified chunk/character.
     * @param i is the chunk being recorded
     */
    public void add(int i);

    /**
     * Set the value/count associated with a specific character/chunk.
     * @param i is the chunk/character whose count is specified
     * @param value is # occurrences of specified chunk
     */
    public void set(int i, int value);

    /**
     * All counts cleared to zero.
     */
    public void clear();
}