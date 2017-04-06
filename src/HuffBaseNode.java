
// -------------------------------------------------------------------------
/**
 *  Write a one-sentence summary of your class here.
 *  Follow it with additional details about its purpose, what abstraction
 *  it represents, and how to use it.
 *
 *  @author ryanmorgan
 *  @version Apr 18, 2016
 */

/** Huffman tree node implementation: Base class */
interface HuffBaseNode {
  boolean isLeaf();
  int weight();
}
