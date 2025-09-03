package assignment3;

import java.util.ArrayList;
import java.util.Random;
import java.awt.Color;

public class Block {
 private int xCoord;
 private int yCoord;
 private int size; // height/width of the square
 private int level; // the root (outer most block) is at level 0, if block at level i, then child i+1
 private int maxDepth; //deepest level allowed in overall block
 private Color color;

 private Block[] children; // {UR, UL, LL, LR}

 public static Random gen = new Random(2);
 
 
 /*
  * These two constructors are here for testing purposes. 
  */
 public Block() {}
 
 public Block(int x, int y, int size, int lvl, int  maxD, Color c, Block[] subBlocks) {
  this.xCoord=x;
  this.yCoord=y;
  this.size=size;
  this.level=lvl;
  this.maxDepth = maxD;
  this.color=c;
  this.children = subBlocks;
 }
 
 

 /*
  * Creates a random block given its level and a max depth. 
  * 
  * xCoord, yCoord, size, and highlighted should not be initialized
  * (i.e. they will all be initialized by default)
  */
 public Block(int lvl, int maxDepth) {
  /*
   * ADD YOUR CODE HERE
   */
  this.level = lvl;
  this.maxDepth = maxDepth;
  //this.size = (int) ( Math.pow(2, lvl) / 16); //2^(2-0) = 4

  if (lvl < maxDepth){
   double random = gen.nextDouble(); //already between [0,1)
   if (random < Math.exp(-0.25 * level)) {  //subdivide
    this.color = null; //parent block no color
    this.children = new Block[4];

    for (int i = 0; i < 4; i++) {
     this.children[i] = new Block(lvl + 1, maxDepth);
    }
    return;
   }
  }
  //pick random color
  this.children = new Block[0]; //children is empty array
  int colorInt = gen.nextInt(GameColors.BLOCK_COLORS.length);
  this.color = GameColors.BLOCK_COLORS[colorInt];

 }


 /*
  * Updates size and position for the block and all of its sub-blocks, while
  * ensuring consistency between the attributes and the relationship of the 
  * blocks. 
  * 
  *  The size is the height and width of the block. (xCoord, yCoord) are the 
  *  coordinates of the top left corner of the block. 
  */
 public void updateSizeAndPosition (int size, int xCoord, int yCoord) {
  /*
   * ADD YOUR CODE HERE
   */
  if (this.level < this.maxDepth) {
   int division = (int) Math.pow(2, maxDepth - level); //divisible by maxDepth 2 times to get 2 integers
   if (size % division != 0 || size < division || size < 0) {
    throw new IllegalArgumentException("Invalid input, input is negative or cannot be " +
            "divided into 2 integers until the max depth is reached.");
   }
  }
//  if (this.level == 0 && (size <= 0 || xCoord < 0|| yCoord < 0|| size%(division) != 0
//          || xCoord%(division) != 0 || yCoord%(division) != 0)){
//   throw new IllegalArgumentException("Input is negative or it cannot be evenly" +
//           "divided into 2 integers until the max depth is reached. ");
//  }

  this.size = size;
  this.xCoord = xCoord;
  this.yCoord = yCoord;

  if (this.children.length == 4){//if subvided, it will be equal to 4
   int childrenSize =  size/2;

   //{UR, UL, LL, LR}
   this.children[0].updateSizeAndPosition(childrenSize, xCoord + childrenSize, yCoord); //UR
   this.children[1].updateSizeAndPosition(childrenSize, xCoord, yCoord); //UL
   this.children[2].updateSizeAndPosition(childrenSize, xCoord, yCoord+childrenSize); //LL
   this.children[3].updateSizeAndPosition(childrenSize, xCoord + childrenSize, yCoord+childrenSize); //LR

  }

 }

 
 /*
  * Returns a List of blocks to be drawn to get a graphical representation of this block.
  * 
  * This includes, for each undivided Block:
  * - one BlockToDraw in the color of the block
  * - another one in the FRAME_COLOR and stroke thickness 3
  * 
  * Note that a stroke thickness equal to 0 indicates that the block should be filled with its color.
  *  
  * The order in which the blocks to draw appear in the list does NOT matter.
  */
 public ArrayList<BlockToDraw> getBlocksToDraw() {
  /*
   * ADD YOUR CODE HERE
   */
  ArrayList<BlockToDraw> array = new ArrayList<BlockToDraw>(); //empty array

  if (this.children.length == 0){ //leaf

   //add one block to draw in color of the block: Color c, int xCoord, int yCoord, int size, int stroke
   array.add(new BlockToDraw(this.color, this.xCoord, this.yCoord, this.size, 0)); //stroke 0:the block should be filled with its color

   //add one with frame
   array.add(new BlockToDraw(GameColors.FRAME_COLOR, this.xCoord, this.yCoord, this.size, 3));

  }
  else{

   for (int i = 0; i < 4; i++){
    ArrayList<BlockToDraw> children = this.children[i].getBlocksToDraw();
    array.addAll(children);
   }
  }
  return array;
 }

 /*
  * This method is provided and you should NOT modify it. 
  */
 public BlockToDraw getHighlightedFrame() {
  return new BlockToDraw(GameColors.HIGHLIGHT_COLOR, this.xCoord, this.yCoord, this.size, 5);
 }
 
 
 
 /*
  * Return the Block within this Block that includes the given location
  * and is at the given level. If the level specified is lower than 
  * the lowest block at the specified location, then return the block 
  * at the location with the closest level value.
  * 
  * The location is specified by its (x, y) coordinates. The lvl indicates 
  * the level of the desired Block. Note that if a Block includes the location
  * (x, y), and that Block is subdivided, then one of its sub-Blocks will 
  * contain the location (x, y) too. This is why we need lvl to identify 
  * which Block should be returned. 
  * 
  * Input validation: 
  * - this.level <= lvl <= maxDepth (if not throw exception)
  * - if (x,y) is not within this Block, return null.
  */
 public Block getSelectedBlock(int x, int y, int lvl) {
  /*
   * ADD YOUR CODE HERE
   */
  if (lvl < this.level || lvl > this.maxDepth){
   throw new IllegalArgumentException("Invalid level input");
  }
  //check if (x,y) outside of block
  int maxXcoord = this.xCoord + this.size;
  int maxYcoord = this.yCoord + this.size;
  if (x < this.xCoord || x >= maxXcoord || y < this.yCoord || y >= maxYcoord){
   return null;
  }

  //this block is equal to the requested level or this block is a leaf, can do this since (x,y) alr in and same level
  if (this.level == lvl || this.children.length == 0){
   return this;
  }

  for (int i = 0; i < 4; i++){
   Block selectedBlock =  this.children[i].getSelectedBlock(x,y,lvl);
   if (selectedBlock != null){
    return selectedBlock;
   }
  }

  return null;
 }

 
 

 /*
  * Swaps the child Blocks of this Block. 
  * If input is 1, swap vertically. If 0, swap horizontally. 
  * If this Block has no children, do nothing. The swap 
  * should be propagate, effectively implementing a reflection
  * over the x-axis or over the y-axis.
  * 
  */
 public void reflect(int direction) {
  /*
   * ADD YOUR CODE HERE
   */
  if (direction != 0 && direction != 1){
   throw new IllegalArgumentException("Invalid input, the input must be 0(x axis) or 1(y axis)");
  }
  if (this.children.length == 0){ //if leaf nothing to do
   return;
  }

  // 0: UR, 1: UL, 2: LL, 3: LR
  if (direction == 0){ //x direction flip 0 and 3, 1 and 2
   Block temp = this.children[0]; //this.children because is the seperated 4 sides
   this.children[0] = this.children[3];
   this.children[3] = temp;

   temp = this.children[1]; //this.children because is the seperated 4 sides
   this.children[1] = this.children[2];
   this.children[2] = temp;

  }else { //y direction, flip 0,1 and 2,3
   Block temp = this.children[0]; //this.children because is the seperated 4 sides
   this.children[0] = this.children[1];
   this.children[1] = temp;

   temp = this.children[2]; //this.children because is the seperated 4 sides
   this.children[2] = this.children[3];
   this.children[3] = temp;
  }

  for (int i = 0; i < 4; i++){
   this.children[i].reflect(direction);
  }
  //let GUI draw in the new spots, update it
  int half = size/2; //because children
  children[0].updateSizeAndPosition(half, xCoord+half, yCoord);
  children[1].updateSizeAndPosition(half, xCoord, yCoord);
  children[2].updateSizeAndPosition(half, xCoord, yCoord+half);
  children[3].updateSizeAndPosition(half, xCoord+half, yCoord+half);

 }
 

 
 /*
  * Rotate this Block and all its descendants. 
  * If the input is 1, rotate clockwise. If 0, rotate 
  * counterclockwise. If this Block has no children, do nothing.
  */
 public void rotate(int direction) {
  /*
   * ADD YOUR CODE HERE
   */
  if (direction != 0 && direction != 1){
   throw new IllegalArgumentException("Invalid input, the input must be 0(x axis) or 1(y axis)");
  }
  if (this.children.length == 0){ //if leaf nothing to do
   return;
  }

  // 0: UR, 1: UL, 2: LL, 3: LR
  Block temp0 = children[0]; // UR
  Block temp1 = children[1]; // UL
  Block temp2 = children[2]; // LL
  Block temp3 = children[3]; // LR

  //ROTATE LIKE A SQUARE NOT LINE
  if (direction == 0){ //counterclockwise switch
   // UR(0) to UL(1) to  LL(2) to LR(3) to UR(0)

   children[0] = temp3; // LR to UR
   children[1] = temp0; // UR to UL
   children[2] = temp1; // UL to LL
   children[3] = temp2; // LL to LR

  }else { //clockwise
   children[0] = temp1; // UL to UR
   children[1] = temp2; // LL to UL
   children[2] = temp3; // LR to LL
   children[3] = temp0; // UR to LR

  }

  for (int i = 0; i < 4; i++){
   this.children[i].rotate(direction);
  }
  //let GUI draw in the new spots, update it
  int half = size/2; //because children
  children[0].updateSizeAndPosition(half, xCoord+half, yCoord);
  children[1].updateSizeAndPosition(half, xCoord, yCoord);
  children[2].updateSizeAndPosition(half, xCoord, yCoord+half);
  children[3].updateSizeAndPosition(half, xCoord+half, yCoord+half);
 }
 


 /*
  * Smash this Block.
  * 
  * If this Block can be smashed,
  * randomly generate four new children Blocks for it.  
  * (If it already had children Blocks, discard them.)
  * Ensure that the invariants of the Blocks remain satisfied.
  * 
  * A Block can be smashed iff it is not the top-level Block 
  * and it is not already at the level of the maximum depth.
  * 
  * Return True if this Block was smashed and False otherwise.
  * 
  */
 public boolean smash() {
  /*
   * ADD YOUR CODE HERE
   */
  if (this.level == 0 || this.level >= maxDepth){
   return false; //cannot  be smashed
  }

  //can smash
  this.color = null; //not a lead anymore
  this.children = new Block[4];

  for (int i = 0; i < 4; i++){
   this.children[i] = new Block(this.level+1, this.maxDepth);
  }
  //let GUI draw in the new spots, update it
  int half = size/2; //because children
  children[0].updateSizeAndPosition(half, xCoord+half, yCoord);
  children[1].updateSizeAndPosition(half, xCoord, yCoord);
  children[2].updateSizeAndPosition(half, xCoord, yCoord+half);
  children[3].updateSizeAndPosition(half, xCoord+half, yCoord+half);
  return true;

 }
 
 
 /*
  * Return a two-dimensional array representing this Block as rows and columns of unit cells.
  * 
  * Return and array arr where, arr[i] represents the unit cells in row i, 
  * arr[i][j] is the color of unit cell in row i and column j.
  * 
  * arr[0][0] is the color of the unit cell in the upper left corner of this Block.
  */
 public Color[][] flatten() {
  /*
   * ADD YOUR CODE HERE
   */
  int unit = (int) Math.pow(2, maxDepth - level); //how many units each side
  Color[][] array = new Color[unit][unit];

  if (this.children.length == 0){ //if leaf
   for (int i = 0; i < unit; i++){
    for (int j = 0; j < unit; j++){
     array[i][j] = color;
    }
   }
   return array;
  }
  //now do for children
  int half = unit/2;
  int rowAdd = 0;
  int colAdd = 0;

  for (int i = 0; i < 4; i++){
   Color[][] childrenArray = this.children[i].flatten(); //call flatten on child number i

   if (i == 0){ //UR
    rowAdd = 0; //increase row moves down
    colAdd = half; //increase column moves to the right
   }
   if (i == 1){ //UL
    rowAdd = 0; //increase row moves down
    colAdd = 0; //increase column moves to the right
   }
   if (i == 2){ //LL
    rowAdd = half; //increase row moves down
    colAdd = 0; //increase column moves to the right
   }
   if (i == 3){ //LR
    rowAdd = half; //increase row moves down
    colAdd = half; //increase column moves to the right
   }

   for (int row = 0; row < half; row++){
    for (int col = 0; col < half; col++){
     array[row + rowAdd][col + colAdd] = childrenArray[row][col]; //copying each cell from children to parent
    }
   }
  }

  return array;
 }

 
 
 // These two get methods have been provided. Do NOT modify them. 
 public int getMaxDepth() {
  return this.maxDepth;
 }
 
 public int getLevel() {
  return this.level;
 }


 /*
  * The next 5 methods are needed to get a text representation of a block. 
  * You can use them for debugging. You can modify these methods if you wish.
  */
 public String toString() {
  return String.format("pos=(%d,%d), size=%d, level=%d"
    , this.xCoord, this.yCoord, this.size, this.level);
 }

 public void printBlock() {
  this.printBlockIndented(0);
 }

 private void printBlockIndented(int indentation) {
  String indent = "";
  for (int i=0; i<indentation; i++) {
   indent += "\t";
  }

  if (this.children.length == 0) {
   // it's a leaf. Print the color!
   String colorInfo = GameColors.colorToString(this.color) + ", ";
   System.out.println(indent + colorInfo + this);   
  } else {
   System.out.println(indent + this);
   for (Block b : this.children)
    b.printBlockIndented(indentation + 1);
  }
 }
 
 private static void coloredPrint(String message, Color color) {
  System.out.print(GameColors.colorToANSIColor(color));
  System.out.print(message);
  System.out.print(GameColors.colorToANSIColor(Color.WHITE));
 }

 public void printColoredBlock(){
  Color[][] colorArray = this.flatten();
  for (Color[] colors : colorArray) {
   for (Color value : colors) {
    String colorName = GameColors.colorToString(value).toUpperCase();
    if(colorName.length() == 0){
     colorName = "\u2588";
    }else{
     colorName = colorName.substring(0, 1);
    }
    coloredPrint(colorName, value);
   }
   System.out.println();
  }
 }
 
}
