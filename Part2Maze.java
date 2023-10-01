import javalib.impworld.World;
import javalib.impworld.WorldScene;
import javalib.worldimages.*;
import tester.Tester;
import java.awt.*;
import java.util.*;

/* MAZE NOTES PLEASE READ:
 * How to:
 * You can press 'r' to get a new maze either after you won
 * or are bored of the current maze
 * You can press 'd' to see the DFS, which is shown in pink
 *  - once it reaches the end it shows the traces back the correct 
 *    path in dark blue
 * You can press ' b' to see the BFS, yet again also shown in pink 
 *    and traces back correct path
 * You are able to manually traverse the maze using the arrow keys
 *     - your path is shown in light blue and your player is dark green
 *     - you want to reach the purple square at the end
 * If you want to change the size of the maze just scroll all the way to 
 * the bottom, that's where the big bang is. More instructions there.
 *     
 * Bells and Whistles:
 *   WHISTLES:
 * - Allow the user the ability to start a new maze without 
 *   restarting the program.
 *   BELLS:
 * - Allow the user to traverse the maze manually - using the keys to 
 *   select the next move, preventing illegal moves and notifying the 
 *   user of completion of the game.
 * - (Tricky) Construct mazes with a bias in a particular direction 
 *    a preference for horizontal or vertical corridors. 
 *    NOTE:(If you want to change the bias I made some examples by the bigbang,
 *    if you want to see them, simply uncomment them and plug in the Maze name
 *    infront of the bigbang)
 *   
 * Side Note: We weren't able to figure out how to smoothly make the
 * path so it is just squares, sorry
 * 
 * Enjoy :)
 * 
 * 
 */

//represents the Vertex class
class Vertex {
  int x;
  int y;
  //determines if the right should be made
  boolean makeRight;
  //determines if the down should be made
  boolean makeBottom;
  //determines if Vertex has been visited
  boolean visited;
  //the previous Vertex
  Vertex previous;
  Vertex left;
  Vertex right;
  Vertex top;
  Vertex bottom;
  ArrayList<Edge> arrayEdge = new ArrayList<Edge>();

  Vertex(int x, int y) {
    this.x = x;
    this.y = y;
    //this makes the maze edges
    this.makeRight = true;
    this.makeBottom = true;
  }

  //draws a right maze wall
  WorldImage drawEdgeRight() {
    return new LineImage(new Posn(0, Maze.cellSize), Color.black)
        .movePinhole(- Maze.cellSize, Maze.cellSize / - 1.8);
  }

  //draws a bottom maze wall
  WorldImage drawEdgeBottom() {
    return new LineImage(new Posn(Maze.cellSize, 0), Color.black)
        .movePinhole(Maze.cellSize / -1.8, - Maze.cellSize);
  }

  //draws the path rect
  WorldImage draw(int wid, Color color) {
    return new RectangleImage(Maze.cellSize - 3, Maze.cellSize - 3,
        OutlineMode.SOLID, color).movePinhole(-wid * Maze.cellSize / wid / 2,
            - wid * Maze.cellSize / wid / 2);
  }

  //finds the previous cell
  void findPrevious() {
    if (this.top != null && !this.top.makeBottom && this.top.previous 
        == null) {
      this.previous = this.top;
    }
    else if (this.left != null && !this.left.makeRight && this.left.previous 
        == null) {
      this.previous = this.left;
    }
    else if (this.bottom != null && !this.makeBottom && this.bottom.previous 
        == null) {
      this.previous = this.bottom;
    }
    else if (this.right != null && !this.makeRight && this.right.previous 
        == null) {
      this.previous = this.right;
    }
  }

  //computes this x times cell Size
  public int xVertTCell() {
    return this.x * Maze.cellSize;
  }

  //computes this y times cell Size
  public int yVertTCell() {
    return this.y * Maze.cellSize;
  }

  //computes this x times cell Size
  public int xPreviousTCell() {
    return this.previous.xVertTCell();
  }

  //computes this y times cell Size
  public int yPreviousTCell() {
    return this.previous.yVertTCell();
  }

  //determines if this previous is not null
  public boolean notNullPrev() {
    return this.previous != null;
  }

  //determines if this to x == from x
  public boolean xFromEqual(Vertex from) {
    return this.x == from.x;
  }

  //EFFECT: sets this.makeBottom to false
  public void bottomFalse() {
    this.makeBottom = false;
  }

  //EFFECT: sets this.makeRight to false
  public void rightFalse() {
    this.makeRight = false;

  }

  //determines if this to y == from y
  public boolean yFromEqual(Vertex from) {
    return this.y == from.y;
  }

  //EFFECT: sets this right vertex to given inputs
  public void setRight(ArrayList<ArrayList<Vertex>> vert2D, 
      int i, int j) {
    this.right = vert2D.get(i).get(j + 1);
  }

  //EFFECT: sets this left vertex to given inputs
  public void setLeft(ArrayList<ArrayList<Vertex>> vert2D, 
      int i, int j) {
    this.left = vert2D.get(i).get(j - 1);
  }

  //EFFECT: sets this up vertex to given inputs
  public void setUp(ArrayList<ArrayList<Vertex>> vert2D, 
      int i, int j) {
    this.top = vert2D.get(i - 1).get(j);
  }

  //EFFECT: sets this bottom vertex to given inputs
  public void setBottom(ArrayList<ArrayList<Vertex>> vert2D, 
      int i, int j) {
    this.bottom = vert2D.get(i + 1).get(j);
  }

  //determines if this left is not null
  public boolean leftNotNull() {
    return this.left.notNullPrev();
  }

  //determines if this top is not null
  public boolean topNotNull() {
    return this.top.notNullPrev();
  }

  //EFFECT: sets this previous to this left
  public void previousELeft() {
    this.previous = this.left;
  }

  //EFFECT: sets this previous to this top
  public void previousETop() {
    this.previous = this.top;
  }

  //EFFECT: sets this previous to given Vertex
  public void previousENext(Vertex next) {
    this.previous = next;
  }

  //determines if this top is true
  public boolean isTopBottom(boolean bol) {
    return this.top.isBottom(bol);
  }

  //determines if this not makeBottom and bol is true
  boolean isBottom(boolean bol) {
    return !this.makeBottom && bol;
  }

  //determines if this left is true
  public boolean isLeftMakeRight(boolean bol) {
    return this.left.isRight(bol);
  }

  //determines if this not makeRight and bol is true
  boolean isRight(boolean bol) {
    return !this.makeRight && bol;
  }

  //EFFECT: Sets this visited to true
  public void wasVisited() {
    this.visited = true;
  }
}

//represents the player class
class Player {
  Vertex playerVert;

  Player(Vertex playerVert) {
    this.playerVert = playerVert;
  }

  // Checks if each key input results in a valid move
  boolean isValid(String move) {
    if (move.equals("up") && this.playerVert.top != null) {
      return !this.playerVert.top.makeBottom;
    }
    else if (move.equals("down") && this.playerVert.bottom != null) {
      return !this.playerVert.makeBottom;
    }
    else if (move.equals("left") && this.playerVert.left != null) {
      return !this.playerVert.left.makeRight;
    }
    else if (move.equals("right") && this.playerVert.right != null) {
      return !this.playerVert.makeRight;
    }
    else {
      return false;
    }
  }

  //draws the player
  WorldImage drawPlayer() {
    return new RectangleImage(Maze.cellSize - 3, Maze.cellSize - 3,
        OutlineMode.SOLID, Color.GREEN.darker().darker())
        .movePinhole(- Maze.cellSize / 1.8, - Maze.cellSize / 1.8);
  }

  //computes this players vert x times cellSize
  public int playerXTCell() {
    return this.playerVert.xVertTCell();
  }

  //computes this players vert y times cellSize
  public int playerYTCell() {
    return this.playerVert.yVertTCell();
  }

  //determines if this playerVert equals given vert
  public boolean vertEBoard(Vertex vert) {
    return this.playerVert == vert;
  }

  //EFFECT: Sets player vert visited to true
  public void playerVertVisited() {
    this.playerVert.wasVisited();
  }

  //EFFECT: sets this playerVert to right player Vert
  public void playerVertRight() {
    this.playerVert = this.playerVert.right;  
  }

  //EFFECT: sets this playerVert to left player Vert
  public void playerVertLeft() {
    this.playerVert = this.playerVert.left;  
  }

  //EFFECT: sets this playerVert to top player Vert
  public void playerVertUp() {
    this.playerVert = this.playerVert.top;  
  }

  //EFFECT: sets this playerVert to bottom player Vert
  public void playerVertBottom() {
    this.playerVert = this.playerVert.bottom;  
  }
}

//represents the edge class
class Edge {
  Vertex from;
  Vertex to;
  int weight;

  Edge(Vertex from, Vertex to, int weight) {
    this.from = from;
    this.to = to;
    this.weight = weight;
  }

  //determines if this to and this from x's are equal
  public boolean isToFromX() {
    return this.to.xFromEqual(this.from);
  }

  //determines if this to and this from y's are equal
  public boolean isToFromY() {
    return this.to.yFromEqual(this.from);
  }
}

//represents the WeightComparator class that compares 
// edge weight
class WeightComparator implements Comparator<Edge> {

  //computes the edge weight difference
  public int compare(Edge edge1, Edge edge2) {
    return edge1.weight - edge2.weight;
  }
}

//represents the maze world
class Maze extends World {
  int width;
  int height;
  Player player;
  Vertex endVert;
  boolean isComplete;
  Random rand = new Random();
  HashMap<Vertex, Vertex> map = new HashMap<Vertex, Vertex>();
  ArrayList<Edge> arrayEdge = new ArrayList<Edge>();
  ArrayList<Edge> arrayEdge1 = new ArrayList<Edge>();
  ArrayList<Vertex> mazePath = new ArrayList<Vertex>();
  WorldScene mazeScene = new WorldScene(this.height * Maze.cellSize, 
      this.width * Maze.cellSize);
  ArrayList<ArrayList<Vertex>> board;
  int bias;
  static int cellSize = 15;

  Maze(int width, int height) {
    this.width = width;
    this.height = height;
    this.bias = 100;
    this.board = this.makeGrid(width, height);
    this.createEdges(this.board);
    this.createMap(board);
    this.kruskals();
    this.player = new Player(board.get(0).get(0));
    this.endVert = this.board.get(height - 1).get(width - 1);
    this.makeMaze();
    this.isComplete = false;
  }

  //this is if you want to bias the maze
  //ie more veritcal or horizontal
  Maze(int width, int height, int bias) {
    this.width = width;
    this.height = height;
    this.bias = bias;
    this.board = this.makeGrid(width, height);
    this.createEdges(this.board);
    this.createMap(board);
    this.kruskals();
    this.player = new Player(board.get(0).get(0));
    this.endVert = this.board.get(height - 1).get(width - 1);
    this.makeMaze();
    this.isComplete = false;
  }

  //makes the maze 
  WorldScene makeMaze() {
    //draw ending rect
    this.mazeScene.placeImageXY(board.get(this.height - 1).get(this.width - 1)
        .draw(this.width, new Color(88, 24, 161)),
        (width - 1) * cellSize, (height - 1) * cellSize);
    //draws 
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        Vertex boardGetIGetJ = board.get(i).get(j);
        this.makeBottom(this.board.get(i).get(j));
        this.makeRight(this.board.get(i).get(j));
        if (boardGetIGetJ.visited) {
          this.mazeScene.placeImageXY(board.get(i).get(j).draw(this.width,
              new Color(89, 153, 255)), j * cellSize + 1, 
              i * cellSize + 1);
        }
        if (boardGetIGetJ.makeRight) {
          this.mazeScene.placeImageXY(board.get(i).get(j).drawEdgeRight(),
              (Maze.cellSize * j),
              (Maze.cellSize * i));
        }
        if (boardGetIGetJ.makeBottom) {
          this.mazeScene.placeImageXY(board.get(i).get(j).drawEdgeBottom(),
              (Maze.cellSize * j),
              (Maze.cellSize * i));
        }
      }
    }
    //draws the player(green rect)
    this.mazeScene.placeImageXY(player.drawPlayer(), this.player.playerXTCell(), 
        this.player.playerYTCell());
    return mazeScene;
  }

  //makes the games WorldScene
  public WorldScene makeScene() {
    TextImage winText = new TextImage("You Win! :)", this.height + 5, Color.BLACK);
    if (mazePath.size() > 1) {
      this.findEnd();
    }
    else if (mazePath.size() > 0) {
      this.drawEnd();
    }
    else if (this.isComplete && this.endVert.notNullPrev()) {
      this.traceBackPath();
    }
    //if player solves the maze
    if (this.player.vertEBoard(this.board.get(height - 1).get(width - 1))) {
      this.mazeScene.placeImageXY(winText, width * cellSize / 2, 
          height * cellSize / 2);
    }
    return mazeScene;
  }

  //EFFECT: updates the maze scene with the end scene
  void drawEnd() {
    Vertex next = mazePath.remove(0);
    if (this.endVert.isLeftMakeRight(this.endVert.leftNotNull())) {
      this.endVert.previousELeft();
    }
    else if (this.endVert.isTopBottom(this.endVert.topNotNull())) {
      this.endVert.previousETop();
    }
    else {
      this.endVert.previousENext(next);
    }
    this.isComplete = true;
  }

  // Changes if the right wall should be rendered for the given vertex
  // Effect: Changes the makeRight field of the vertex
  void makeRight(Vertex v) {
    for (Edge edge : this.arrayEdge1) {
      if (edge.isToFromY()) {
        edge.from.rightFalse();
      }
    }
  }

  // Changes whether the bottom wall should be rendered for the given vertex
  // Effect: Changes the makeRight field of the vertex
  void makeBottom(Vertex v) {
    for (Edge edge : this.arrayEdge1) {
      if (edge.isToFromX()) {
        edge.from.bottomFalse();
      }
    }
  }

  //creates the grid for each cell in the maze
  ArrayList<ArrayList<Vertex>> makeGrid(int width, int height) {
    ArrayList<ArrayList<Vertex>> board = new ArrayList<ArrayList<Vertex>>();
    for (int i = 0; i < height; i++) {
      board.add(new ArrayList<Vertex>());
      ArrayList<Vertex> arrayVert = board.get(i);
      for (int j = 0; j < width; j++) {
        arrayVert.add(new Vertex(j, i));
      }
    }
    this.connectVerts(board);
    this.createEdges(board);
    this.createMap(board);
    return board;
  }

  //creates the grid for each cell in the maze, but with a string input
  // this is for testing
  ArrayList<ArrayList<Vertex>> makeGrid(int width, int height, String s) {
    ArrayList<ArrayList<Vertex>> board = new ArrayList<ArrayList<Vertex>>();
    for (int i = 0; i < height; i++) {
      board.add(new ArrayList<Vertex>());
      ArrayList<Vertex> vert = board.get(i);
      for (int j = 0; j < width; j++) {
        vert.add(new Vertex(j, i));
      }
    }
    this.connectVerts(board);
    return board;
  }

  //creates the arraylist of edges in the maze game
  ArrayList<Edge> createEdges(ArrayList<ArrayList<Vertex>> vert2D) {
    for (int i = 0; i < vert2D.size(); i++) {
      for (int j = 0; j < vert2D.get(i).size(); j++) {
        if (j < vert2D.get(i).size() - 1) {
          Vertex getIJ = vert2D.get(i).get(j);
          this.arrayEdge.add(new Edge(getIJ, getIJ.right, 
              this.rand.nextInt(this.bias)));
        }
        if (i < vert2D.size() - 1) {
          Vertex getIJ = vert2D.get(i).get(j);
          this.arrayEdge.add(new Edge(getIJ, getIJ.bottom,
              this.rand.nextInt(100)));
        }
      }
    }
    Collections.sort(this.arrayEdge, new WeightComparator());
    return  this.arrayEdge;
  }

  // creates an initial hashmap where each node is linked to itself
  // map = HashMap<Vertex, Vertex>();
  HashMap<Vertex, Vertex> createMap(ArrayList<ArrayList<Vertex>> vertex) {
    for (int i = 0; i < vertex.size(); i++) {
      for (int j = 0; j < vertex.get(i).size(); j++) {
        this.map.put(vertex.get(i).get(j), vertex.get(i).get(j));
      }
    }
    return this.map;
  }

  //EFFECT: connects the verts in given vert list
  void connectVerts(ArrayList<ArrayList<Vertex>> vert2D) {
    for (int i = 0; i < this.height; i++) {
      for (int j = 0; j < this.width; j++) {
        if (j + 1 < this.width) {
          vert2D.get(i).get(j).setRight(vert2D, i, j);
          //right = vert2D.get(i).get(j + 1);
        }
        if (j - 1 >= 0) {
          vert2D.get(i).get(j).setLeft(vert2D, i, j);
        }
        if (i + 1 < this.height) {
          vert2D.get(i).get(j).setBottom(vert2D, i, j);
        }
        if (i - 1 >= 0) {
          vert2D.get(i).get(j).setUp(vert2D, i, j);
        }
      }
    }
  }

  //makes kruskal algorithm
  ArrayList<Edge> kruskals() {
    int n = 0;
    while (this.arrayEdge1.size() < this.arrayEdge.size() 
        && n < this.arrayEdge.size()) {
      Edge e = arrayEdge.get(n);
      if (this.find(this.find(e.from)).equals(this.find(this.find(e.to)))) {
        // do nothing
      }
      else {
        arrayEdge1.add(e);
        switchVert(this.find(e.from), this.find(e.to));
      }
      n += 1;
    }
    // Adds all the arrayEdge for each vertex
    //has to be in this method because it uses e (Edge)
    for (int i = 0; i < this.height; i ++) {
      for (int j = 0; j < this.width; j ++) {
        for (Edge e : this.arrayEdge1) {
          if (this.board.get(i).get(j).equals(e.from) 
              || this.board.get(i).get(j).equals(e.to)) {
            this.board.get(i).get(j).arrayEdge.add(e);
          }
        }
      }
    }
    return this.arrayEdge1;
  }

  //EFFECT: changes the hashMap values with given Verts
  void switchVert(Vertex vert1, Vertex vert2) {
    this.map.put(this.find(vert1), this.find(vert2));
  }

  //finds the representative of this node
  Vertex find(Vertex vert) {
    if (vert.equals(this.map.get(vert))) {
      return vert;
    }
    else {
      return this.find(this.map.get(vert));
    }
  }

  //EFFECT: changes the world based on key clicks
  public void onKeyEvent(String ke) {
    //this restarts the maze/ makes a new maze
    if (ke.equals("r")) {
      this.mazeScene = this.getEmptyScene();
      this.board = this.makeGrid(width, height);
      this.createEdges(this.board);
      this.createMap(board);
      this.kruskals();
      this.player = new Player(board.get(0).get(0));
      this.endVert = this.board.get(this.height - 1).get(this.width - 1);
      this.makeMaze();
    }
    else if (ke.equals("right") && player.isValid("right")) {
      player.playerVertVisited();
      player.playerVertRight();
    }
    else if (ke.equals("left") && player.isValid("left")) {
      player.playerVertVisited();
      player.playerVertLeft();
    }
    else if (ke.equals("up") && player.isValid("up")) {
      player.playerVertVisited();
      player.playerVertUp();
    }
    else if (ke.equals("down") && player.isValid("down")) {
      player.playerVertVisited();
      player.playerVertBottom();
    }
    //this displays the DFS
    else if (ke.equals("d")) {
      this.endVert = this.board.get(this.height - 1).get(this.width - 1);
      this.mazePath = new Paths().mazeDFS(this.board.get(0).get(0), 
          this.board.get(this.height - 1).get(this.width - 1));
    }
    //this displays the BFS
    else if (ke.equals("b")) {
      this.endVert = this.board.get(this.height - 1).get(this.width - 1);
      this.mazePath = new Paths().mazeBFS(this.board.get(0).get(0), 
          this.board.get(this.height - 1).get(this.width - 1));
    }
    this.makeMaze();
  }

  //EFFECT: Updates the mazePath with pink squares 
  void findEnd() {
    Vertex next = mazePath.remove(0);
    this.mazeScene.placeImageXY(next.draw(this.width,
        //draws the BFS and DFS in pink
        new Color(255, 153, 255)), next.x * cellSize, next.y * cellSize);
  }

  //EFFECT: updates the maze mazeScene and draws back the correct mazePath
  void traceBackPath() {
    if (this.endVert.x == this.width - 1 && this.endVert.y == this.height - 1) {
      this.mazeScene.placeImageXY(this.endVert.draw(this.width,
          Color.BLUE.darker()), this.endVert.xVertTCell(),
          this.endVert.yVertTCell());
    }
    this.mazeScene.placeImageXY(this.endVert.previous.draw(this.width,
        Color.BLUE.darker()), this.endVert.xPreviousTCell(),
        this.endVert.yPreviousTCell());
    this.endVert = this.endVert.previous;
  }
}

//represents the ICollection interface
interface ICollection<T> {

  //EFFECT: adds the given item to this ICollection
  void add(T item);

  //removes a T from this ICollection
  T remove();

  //returns the size of this ICollection
  int size();
}

//represents the Queue class
class Queue<T> implements ICollection<T> {
  Deque<T> dequeT;

  Queue() {
    this.dequeT = new Deque<T>();
  }

  //EFFECT: adds the given item to this Queue
  public void add(T item) {
    this.dequeT.addAtTail(item);
  }

  //removes an item from this Queue
  public T remove() {
    return this.dequeT.removeFromHead();
  }

  //computes the size of this Queue
  public int size() {
    return this.dequeT.size();
  }
}

//represents the Stack class
class Stack<T> implements ICollection<T> {
  Deque<T> dequeT;

  Stack() {
    this.dequeT = new Deque<T>();
  }

  //EFFECT: adds the given item to a Stack
  public void add(T item) {
    this.dequeT.addAtHead(item);
  }

  //removes and item to a Stack
  public T remove() {
    return this.dequeT.removeFromHead();
  }

  //computes the size of this Stack
  public int size() {
    return this.dequeT.size();
  }
}

//represents the paths class
class Paths {
  ArrayList<Vertex> allVertices;

  Paths() { }

  //finds the BFS maze path
  ArrayList<Vertex> mazeBFS(Vertex from, Vertex to) {
    return this.findMazePath(from, to, new Queue<Vertex>());
  }

  //finds the DFS maze path
  ArrayList<Vertex> mazeDFS(Vertex from, Vertex to) {
    return this.findMazePath(from, to, new Stack<Vertex>());
  }

  //finds the mazePath using ICollection
  ArrayList<Vertex> findMazePath(Vertex from, Vertex to, 
      ICollection<Vertex> worklist) {
    ArrayList<Vertex> mazePath = new ArrayList<Vertex>();

    worklist.add(from);
    while (worklist.size() > 0) {
      Vertex vert = worklist.remove();
      if (vert == to) {
        return mazePath;
      }
      else if (mazePath.contains(vert)) {
        //do nothing
      }
      else {
        for (Edge e : vert.arrayEdge) {
          worklist.add(e.from);
          worklist.add(e.to);
          if (mazePath.contains(e.from)) {
            vert.previous = e.from;
          }
          else if (mazePath.contains(e.to)) {
            vert.previous = e.to;
          }
        }
        mazePath.add(vert);
      }
    }
    return mazePath;
  }
}

//represents the examples class
class ExamplesMaze {
  Maze mazeEx = new Maze(2, 3);
  Maze mazeEx1 = new Maze(1, 1);
  Paths paths = new Paths();
  Paths paths1 = new Paths();
  Vertex vert00 = new Vertex(0, 0);
  Vertex vert01 = new Vertex(0, 1);
  Vertex vert10 = new Vertex(1, 0);
  Vertex vert11 = new Vertex(1, 1);
  Vertex vert12 = new Vertex(1, 2);
  Vertex vert02 = new Vertex(0, 2);
  Edge edge1 = new Edge(vert00, vert10, 1);
  Edge edge2 = new Edge(vert00, vert01, 2);
  Edge edge3 = new Edge(vert10, vert11, 3);
  Edge edge4 = new Edge(vert01, vert11, 4);
  Edge edge5 = new Edge(vert01, vert02, 5);
  Edge edge6 = new Edge(vert11, vert12, 6);

  //initializes the maze examples
  void initMaze() {
    mazeEx.board = mazeEx.makeGrid(2, 3, "test");
    mazeEx1.board = mazeEx1.makeGrid(1, 1, "test");
    mazeEx.board.get(0).get(0).makeRight = false;
    mazeEx.board.get(0).get(1).makeRight = true;
    mazeEx.board.get(1).get(0).makeRight = false;
    mazeEx.board.get(1).get(1).makeRight = false;
    mazeEx.board.get(2).get(0).makeRight = false;
    mazeEx.board.get(2).get(1).makeRight = false;
    mazeEx.map.put(mazeEx.board.get(0).get(0), mazeEx.board.get(0).get(0));
    mazeEx.map.put(mazeEx.board.get(0).get(1), mazeEx.board.get(0).get(1));
    mazeEx.map.put(mazeEx.board.get(1).get(0), mazeEx.board.get(1).get(0));
    mazeEx.map.put(mazeEx.board.get(1).get(1), mazeEx.board.get(1).get(1));
    mazeEx.map.put(mazeEx.board.get(2).get(0), mazeEx.board.get(2).get(0));
    mazeEx.map.put(mazeEx.board.get(2).get(1), mazeEx.board.get(2).get(1));
    mazeEx.board.get(0).get(0).makeBottom = false;
    mazeEx.board.get(0).get(1).makeBottom = false;
    mazeEx.board.get(1).get(0).makeBottom = false;
    mazeEx.board.get(1).get(1).makeBottom = false;
    mazeEx.board.get(2).get(0).makeBottom = true;
    mazeEx.board.get(2).get(1).makeBottom = true;
    mazeEx.arrayEdge = new ArrayList<Edge>(Arrays.asList(
        edge1, edge2, edge3, edge4, edge5, edge6,
        new Edge(vert02, vert12, 7)));
    mazeEx.arrayEdge1 = new ArrayList<Edge>(Arrays.asList(
        edge1, edge2, edge3, edge5, edge6));
    mazeEx.player = new Player(mazeEx.board.get(0).get(0));
    mazeEx.isComplete = false;
    mazeEx.mazePath = new ArrayList<Vertex>();
    mazeEx.endVert = mazeEx.board.get(2).get(1);

  }

  //tests the makeGrid method
  void testMakeGrid(Tester t) {
    this.initMaze();
    t.checkExpect(mazeEx.board, new ArrayList<ArrayList<Vertex>>(Arrays.asList(
        new ArrayList<Vertex>(Arrays.asList(mazeEx.board.get(0)
            .get(0), mazeEx.board.get(0).get(1))),
        new ArrayList<Vertex>(Arrays.asList(mazeEx.board.get(1)
            .get(0), mazeEx.board.get(1).get(1))),
        new ArrayList<Vertex>(Arrays.asList(mazeEx.board.get(2)
            .get(0), mazeEx.board.get(2).get(1))))));
  }

  //tests connectVerts method
  void testConnectVerts(Tester t) {
    this.initMaze();
    t.checkExpect(mazeEx.board.get(0).get(0).right, mazeEx.board.get(0).get(1));
    t.checkExpect(mazeEx.board.get(0).get(0).bottom, mazeEx.board.get(1).get(0));
    t.checkExpect(mazeEx.board.get(0).get(0).top, null);
    t.checkExpect(mazeEx.board.get(0).get(0).left, null);
  }

  //tests the createMap method
  void testCreateMap(Tester t) {
    this.initMaze();
    t.checkExpect(mazeEx.map.get(mazeEx.board.get(0).get(0)), 
        mazeEx.board.get(0).get(0));
    t.checkExpect(mazeEx.map.get(mazeEx.board.get(0).get(1)), 
        mazeEx.board.get(0).get(1));
    t.checkExpect(mazeEx.map.get(mazeEx.board.get(1).get(0)), 
        mazeEx.board.get(1).get(0));
    t.checkExpect(mazeEx.map.get(mazeEx.board.get(1).get(1)), 
        mazeEx.board.get(1).get(1));
    t.checkExpect(mazeEx.map.get(mazeEx.board.get(2).get(0)), 
        mazeEx.board.get(2).get(0));
    t.checkExpect(mazeEx.map.get(mazeEx.board.get(2).get(1)), 
        mazeEx.board.get(2).get(1));
  }

  //tests the kruskals method
  void testKruskals(Tester t) {
    this.initMaze();
    mazeEx.makeGrid(mazeEx.width, mazeEx.height);
    t.checkExpect(mazeEx.arrayEdge1.get(0), new Edge(mazeEx.arrayEdge1
        .get(0).from, mazeEx.arrayEdge1.get(0).to, 1));
    t.checkExpect(mazeEx.arrayEdge1.get(1), new Edge(mazeEx.arrayEdge1
        .get(1).from, mazeEx.arrayEdge1.get(1).to, 2));
    t.checkExpect(mazeEx.arrayEdge1.get(2), new Edge(mazeEx.arrayEdge1
        .get(2).from, mazeEx.arrayEdge1.get(2).to, 3));
    t.checkExpect(mazeEx.arrayEdge1.get(3), new Edge(mazeEx.arrayEdge1
        .get(3).from, mazeEx.arrayEdge1.get(3).to, 5));
    t.checkExpect(mazeEx.arrayEdge1.get(4), new Edge(mazeEx.arrayEdge1
        .get(4).from, mazeEx.arrayEdge1.get(4).to, 6));
  }

  //tests switchVert method
  void testswitchVert(Tester t) {
    this.initMaze();
    mazeEx.switchVert(mazeEx.board.get(0).get(0), mazeEx.board.get(0).get(1));
    t.checkExpect(mazeEx.find(mazeEx.board.get(0).get(0)), mazeEx.board.get(0).get(1));
    mazeEx.switchVert(mazeEx.board.get(0).get(1), mazeEx.board.get(1).get(1));
    t.checkExpect(mazeEx.find(mazeEx.board.get(0).get(1)), mazeEx.board.get(1).get(1));
    mazeEx.switchVert(mazeEx.board.get(2).get(0), mazeEx.board.get(0).get(1));
    t.checkExpect(mazeEx.find(mazeEx.board.get(0).get(0)), mazeEx.board.get(1).get(1));
  }

  //tests the find method
  void testFind(Tester t) {
    this.initMaze();
    t.checkExpect(mazeEx.find(mazeEx.board.get(0).get(0)), 
        mazeEx.board.get(0).get(0));
    t.checkExpect(mazeEx.find(mazeEx.board.get(1).get(0)), 
        mazeEx.board.get(1).get(0));
    t.checkExpect(mazeEx.find(mazeEx.board.get(0).get(1)), 
        mazeEx.board.get(0).get(1));
    t.checkExpect(mazeEx.find(mazeEx.board.get(2).get(0)), 
        mazeEx.board.get(2).get(0));
  }

  //tests the onKeyEvent method
  void testOnKeyEvent(Tester t) {
    this.initMaze();
    mazeEx.onKeyEvent("right");
    t.checkExpect(mazeEx.player.playerVert, mazeEx.board.get(0).get(1));
    mazeEx.onKeyEvent("down");
    t.checkExpect(mazeEx.player.playerVert, mazeEx.board.get(1).get(1));
    mazeEx.onKeyEvent("up");
    t.checkExpect(mazeEx.player.playerVert, mazeEx.board.get(0).get(1));
    mazeEx.onKeyEvent("left");
    t.checkExpect(mazeEx.player.playerVert, mazeEx.board.get(0).get(0));
    mazeEx.onKeyEvent("d");
    t.checkExpect(mazeEx.mazePath, 
        new ArrayList<Vertex>(Arrays.asList(mazeEx.board.get(0).get(0))));
    mazeEx.onKeyEvent("b");
    t.checkExpect(mazeEx.mazePath, 
        new ArrayList<Vertex>(Arrays.asList(mazeEx.board.get(0).get(0))));
    //unable to test "r" because it gives a null pointer
    //mazeEx.onKeyEvent("r");
  }

  //tests isValid method
  void testIsValid(Tester t) {
    this.initMaze();
    t.checkExpect(mazeEx.player.isValid("right"), true);
    t.checkExpect(mazeEx.player.isValid("left"), false);
    t.checkExpect(mazeEx.player.isValid("up"), false);
    t.checkExpect(mazeEx.player.isValid("down"), true);
    t.checkExpect(mazeEx.player.isValid("d"), false);
    t.checkExpect(mazeEx.player.isValid("b"), false);
    t.checkExpect(mazeEx.player.isValid("r"), false);
  }

  //tests the makeRight method
  void testmakeRight(Tester t) {
    this.initMaze();
    mazeEx.makeRight(mazeEx.board.get(0).get(0));
    t.checkExpect(mazeEx.board.get(0).get(0).makeRight, false);
    mazeEx.makeRight(mazeEx.board.get(0).get(1));
    t.checkExpect(mazeEx.board.get(0).get(1).makeRight, true);
    mazeEx.makeRight(mazeEx.board.get(1).get(0));
    t.checkExpect(mazeEx.board.get(1).get(0).makeRight, false);
    mazeEx.makeRight(mazeEx.board.get(1).get(1));
    t.checkExpect(mazeEx.board.get(1).get(1).makeRight, false);
    mazeEx.makeRight(mazeEx.board.get(2).get(0));
    t.checkExpect(mazeEx.board.get(2).get(0).makeRight, false);
  }

  //tests the makeBottom method
  void testmakeBottom(Tester t) {
    this.initMaze();
    mazeEx.makeBottom(mazeEx.board.get(0).get(0));
    t.checkExpect(mazeEx.board.get(0).get(0).makeBottom, false);
    mazeEx.makeBottom(mazeEx.board.get(0).get(1));
    t.checkExpect(mazeEx.board.get(0).get(1).makeBottom, false);
    mazeEx.makeBottom(mazeEx.board.get(2).get(0));
    t.checkExpect(mazeEx.board.get(2).get(0).makeBottom, true);
    mazeEx.makeBottom(mazeEx.board.get(1).get(0));
    t.checkExpect(mazeEx.board.get(1).get(0).makeBottom, false);
    mazeEx.makeBottom(mazeEx.board.get(1).get(1));
    t.checkExpect(mazeEx.board.get(1).get(1).makeBottom, false);
    mazeEx.makeBottom(mazeEx.board.get(2).get(1));
    t.checkExpect(mazeEx.board.get(2).get(1).makeBottom, true);
  }

  //tests the add method for Queue
  void testAdd(Tester t) {
    Queue<Vertex> queueVert = new Queue<Vertex>();
    t.checkExpect(queueVert.size(), 0);
    queueVert.add(new Vertex(0, 0));
    t.checkExpect(queueVert.size(), 1);
    queueVert.add(new Vertex(0, 0));
    t.checkExpect(queueVert.size(), 2);
    queueVert.add(new Vertex(0, 1));
    t.checkExpect(queueVert.size(), 3);
  }

  //tests the size method
  void testSize(Tester t) {
    Queue<Vertex> queueVert = new Queue<Vertex>();
    Stack<Vertex> stackVert = new Stack<Vertex>();
    t.checkExpect(queueVert.size(), 0);
    queueVert.add(new Vertex(0, 0));
    t.checkExpect(queueVert.size(), 1);
    queueVert.add(new Vertex(1, 0));
    t.checkExpect(queueVert.size(), 2);
    queueVert.add(new Vertex(0, 1));
    t.checkExpect(queueVert.size(), 3);
    t.checkExpect(stackVert.size(), 0);
    stackVert.add(new Vertex(1, 0));
    t.checkExpect(stackVert.size(), 1);
    stackVert.add(new Vertex(1, 1));
    t.checkExpect(stackVert.size(), 2);
    stackVert.add(new Vertex(0, 0));
    t.checkExpect(stackVert.size(), 3);
  }

  //tests the remove method for Queue
  void testRemoveQueue(Tester t) {
    Queue<Vertex> queueVert = new Queue<Vertex>();
    queueVert.add(new Vertex(0, 0));
    t.checkExpect(queueVert.remove(), new Vertex(0, 0));
    queueVert.add(new Vertex(1, 0));
    t.checkExpect(queueVert.remove(), new Vertex(1, 0));
    queueVert.add(new Vertex(1, 1));
    t.checkExpect(queueVert.remove(), new Vertex(1, 1));
  }

  //tests add method for stack
  void testAddStack(Tester t) {
    Stack<Vertex> stack = new Stack<Vertex>();
    t.checkExpect(stack.size(), 0);
    stack.add(new Vertex(0, 0));
    t.checkExpect(stack.size(), 1);
    stack.add(new Vertex(0, 0));
    t.checkExpect(stack.size(), 2);
    stack.add(new Vertex(0, 0));
    t.checkExpect(stack.size(), 3);
  }

  //tests the mazeDFS method
  void testmazeDFS(Tester t) {
    this.initMaze();
    t.checkExpect(paths.mazeDFS(mazeEx.board.get(0).get(0), 
        mazeEx.board.get(2).get(1)),
        new ArrayList<Vertex>(Arrays.asList(mazeEx.board.get(0).get(0))));
    t.checkExpect(paths1.mazeDFS(mazeEx.board.get(0).get(0), 
        mazeEx.board.get(2).get(1)),
        new ArrayList<Vertex>(Arrays.asList(mazeEx.board.get(0).get(0))));
  }

  //tests the mazeDFS method
  void testmazeBFS(Tester t) {
    this.initMaze();
    t.checkExpect(paths.mazeBFS(mazeEx.board.get(0).get(0), 
        mazeEx.board.get(2).get(1)),
        new ArrayList<Vertex>(Arrays.asList(mazeEx.board.get(0).get(0))));
    t.checkExpect(paths1.mazeBFS(mazeEx.board.get(0).get(0), 
        mazeEx.board.get(2).get(1)),
        new ArrayList<Vertex>(Arrays.asList(mazeEx.board.get(0).get(0))));
  }

  //tests the findMazePath method
  void testFindMazePath(Tester t) {
    this.initMaze();
    t.checkExpect(paths.findMazePath(mazeEx.board.get(0).get(0), 
        mazeEx.board.get(2).get(1), new Stack<Vertex>()),
        new ArrayList<Vertex>(Arrays.asList(mazeEx.board.get(0).get(0))));
    t.checkExpect(paths.findMazePath(mazeEx.board.get(0).get(0), 
        mazeEx.board.get(1).get(1), new Queue<Vertex>()),
        new ArrayList<Vertex>(Arrays.asList(mazeEx.board.get(0).get(0))));
    t.checkExpect(paths1.findMazePath(mazeEx.board.get(0).get(0), 
        mazeEx.board.get(2).get(1), new Stack<Vertex>()),
        new ArrayList<Vertex>(Arrays.asList(mazeEx.board.get(0).get(0))));
    t.checkExpect(paths1.findMazePath(mazeEx.board.get(0).get(0), 
        mazeEx.board.get(1).get(1), new Queue<Vertex>()),
        new ArrayList<Vertex>(Arrays.asList(mazeEx.board.get(0).get(0))));
  }

  //tests the xVertTCell method
  void testXVertTCell(Tester t) {
    this.initMaze();
    t.checkExpect(this.vert00.xVertTCell(), 0);
    t.checkExpect(this.vert01.xVertTCell(), 0);
    t.checkExpect(this.vert10.xVertTCell(), 15);
    t.checkExpect(this.vert02.xVertTCell(), 0);
    t.checkExpect(this.vert11.xVertTCell(), 15);
  }

  //tests the yVertTCell method
  void testYVertTCell(Tester t) {
    this.initMaze();
    this.vert00.previous = vert00;
    t.checkExpect(this.vert00.yVertTCell(), 0);
    t.checkExpect(this.vert01.yVertTCell(), 15);
    t.checkExpect(this.vert10.yVertTCell(), 0);
    t.checkExpect(this.vert02.yVertTCell(), 30);
    t.checkExpect(this.vert11.yVertTCell(), 15);
  }

  //tests the xPreviousTCell method
  void testXPreviousTCell(Tester t) {
    this.initMaze();
    this.vert00.previous = vert00;
    t.checkExpect(this.vert00.xPreviousTCell(), 0);
    this.vert01.previous = vert00;
    t.checkExpect(this.vert01.xPreviousTCell(), 0);
    this.vert10.previous = vert00;
    t.checkExpect(this.vert10.xPreviousTCell(), 0);
    this.vert02.previous = vert00;
    t.checkExpect(this.vert02.xPreviousTCell(), 0);
    this.vert11.previous = vert00;
    t.checkExpect(this.vert11.xPreviousTCell(), 0);
  }

  //tests the yPreviousTCell method
  void testYPreviousTCell(Tester t) {
    this.initMaze();
    this.vert00.previous = vert00;
    t.checkExpect(this.vert00.yPreviousTCell(), 0);
    this.vert01.previous = vert00;
    t.checkExpect(this.vert01.yPreviousTCell(), 0);
    this.vert10.previous = vert00;
    t.checkExpect(this.vert10.yPreviousTCell(), 0);
    this.vert02.previous = vert00;
    t.checkExpect(this.vert02.yPreviousTCell(), 0);
    this.vert11.previous = vert00;
    t.checkExpect(this.vert11.yPreviousTCell(), 0);
  }

  //tests the notNullPrev method
  void testNotNullPrev(Tester t) {
    this.initMaze();
    this.vert00.previous = vert00;
    t.checkExpect(this.vert00.notNullPrev(), true);
    this.vert01.previous = vert01;
    t.checkExpect(this.vert00.notNullPrev(), true);
    this.vert02.previous = vert02;
    t.checkExpect(this.vert02.notNullPrev(), true);
  }

  //tests the xFromEqual method
  void testXFromEqual(Tester t) {
    this.initMaze();
    t.checkExpect(this.vert00.xFromEqual(vert00), true);
    t.checkExpect(this.vert00.xFromEqual(vert11), false);
    t.checkExpect(this.vert01.xFromEqual(vert01), true);
    t.checkExpect(this.vert02.xFromEqual(vert00), true);
    t.checkExpect(this.vert11.xFromEqual(vert10), true);
  }

  //tests the yFromEqual method
  void testYFromEqual(Tester t) {
    this.initMaze();
    t.checkExpect(this.vert00.yFromEqual(vert00), true);
    t.checkExpect(this.vert01.yFromEqual(vert00), false);
    t.checkExpect(this.vert01.yFromEqual(vert01), true);
    t.checkExpect(this.vert02.yFromEqual(vert00), false);
    t.checkExpect(this.vert11.yFromEqual(vert10), false);
  }

  //tests the bottomFalse method
  void testBottomFalse(Tester t) {
    this.initMaze();
    this.vert00.bottomFalse();
    t.checkExpect(this.vert00.makeBottom, false);
    this.vert01.bottomFalse();
    t.checkExpect(this.vert01.makeBottom, false);
    this.vert10.bottomFalse();
    t.checkExpect(this.vert10.makeBottom, false); 
  }

  //tests the rightFalse method
  void testRightFalse(Tester t) {
    this.initMaze();
    this.vert00.rightFalse();
    t.checkExpect(this.vert00.makeRight, false);
    this.vert01.rightFalse();
    t.checkExpect(this.vert01.makeRight, false);
    this.vert10.rightFalse();
    t.checkExpect(this.vert10.makeRight, false); 
  }

  //tests the setRight method
  void testSetRight(Tester t) {
    this.initMaze();
    //this.right = vert2D.get(i).get(j + 1);
    this.vert00.setRight(mazeEx.board, 1, 0);
    t.checkExpect(this.vert00.right, mazeEx.board.get(1).get(1));
    this.vert02.setRight(mazeEx.board, 2, 0);
    t.checkExpect(this.vert02.right, mazeEx.board.get(2).get(1));
  }

  //tests the setLeft method
  void testSetLeft(Tester t) {
    this.initMaze();
    //this.left = vert2D.get(i).get(j - 1);
    this.vert00.setLeft(mazeEx.board, 0, 1);
    t.checkExpect(this.vert00.left, mazeEx.board.get(0).get(0));
    this.vert01.setLeft(mazeEx.board, 1, 1);
    t.checkExpect(this.vert01.left, mazeEx.board.get(1).get(0));
    this.vert02.setLeft(mazeEx.board, 2, 2);
    t.checkExpect(this.vert02.left, mazeEx.board.get(2).get(1));
  }

  //tests the setUp method
  void testSetUp(Tester t) {
    this.initMaze();
    //this.top = vert2D.get(i - 1).get(j);
    this.vert00.setUp(mazeEx.board, 1, 1);
    t.checkExpect(this.vert00.top, mazeEx.board.get(0).get(1));
    this.vert01.setUp(mazeEx.board, 2, 1);
    t.checkExpect(this.vert11.top, null);
  }

  //tests the setBottom method
  void testSetBottom(Tester t) {
    this.initMaze();
    // this.bottom = vert2D.get(i + 1).get(j);
    this.vert00.setBottom(mazeEx.board, 1, 1);
    t.checkExpect(this.vert00.bottom, mazeEx.board.get(2).get(1));
    this.vert02.setBottom(mazeEx.board, 0, 0);
    t.checkExpect(this.vert02.bottom, mazeEx.board.get(1).get(0));
  }

  //tests the leftNotNull method
  void testLeftNotNull(Tester t) {
    this.initMaze();
    Maze mEx2 = new Maze(40, 40);
    t.checkExpect(this.mazeEx.endVert.leftNotNull(), false);
    t.checkExpect(mEx2.endVert.leftNotNull(), false);
  }

  //tests the topNotNull method
  void testTopNotNull(Tester t) {
    this.initMaze();
    Maze mEx2 = new Maze(40, 40);
    t.checkExpect(this.mazeEx.endVert.topNotNull(), false);
    t.checkExpect(mEx2.endVert.leftNotNull(), false);
  }

  //tests the isToFromX method
  void testIsToFromX(Tester t) {
    this.initMaze();
    t.checkExpect(this.edge1.isToFromX(), false);
    t.checkExpect(this.edge2.isToFromX(), true);
    t.checkExpect(this.edge3.isToFromX(), true);
    t.checkExpect(this.edge4.isToFromX(), false);
    t.checkExpect(this.edge5.isToFromX(), true);
    t.checkExpect(this.edge6.isToFromX(), true);
  }

  //tests the isToFromY method
  void testIsToFromY(Tester t) {
    this.initMaze();
    t.checkExpect(this.edge1.isToFromY(), true);
    t.checkExpect(this.edge2.isToFromY(), false);
    t.checkExpect(this.edge3.isToFromY(), false);
    t.checkExpect(this.edge4.isToFromY(), true);
    t.checkExpect(this.edge5.isToFromY(), false);
    t.checkExpect(this.edge6.isToFromY(), false);
  }

  //tests the previousELeft method
  void testPreviousELeft(Tester t) {
    this.initMaze();
    this.vert00.previousELeft();
    t.checkExpect(this.vert00.previous, this.vert00.left); 
    this.vert01.previousELeft();
    t.checkExpect(this.vert01.previous, this.vert01.left);
    this.vert10.previousELeft();
    t.checkExpect(this.vert10.previous, this.vert10.left);
    this.vert02.previousELeft();
    t.checkExpect(this.vert02.previous, this.vert02.left);
    this.vert12.previousELeft();
    t.checkExpect(this.vert12.previous, this.vert12.left);
  }

  //tests the previousETop method
  void testPreviousETop(Tester t) {
    this.initMaze();
    this.vert00.previousETop();
    t.checkExpect(this.vert00.previous, this.vert00.top); 
  }

  //tests the previousENext method
  void testPreviousENext(Tester t) {
    this.initMaze();
    this.vert00.previousENext(this.vert01);
    t.checkExpect(this.vert00.previous, this.vert01); 
  }

  //tests the isBottom method
  void testIsBottom(Tester t) {
    this.initMaze();
    Vertex newVert = new Vertex(3,3);
    t.checkExpect(newVert.isBottom(true), false); 
    t.checkExpect(newVert.isBottom(false), false); 
    Vertex newVert1 = new Vertex(1,3);
    t.checkExpect(newVert1.isBottom(true), false); 
    t.checkExpect(newVert1.isBottom(false), false); 
  }

  //tests the isRight method
  void testIsRight(Tester t) {
    this.initMaze();
    Vertex newVert = new Vertex(3,3);
    t.checkExpect(newVert.isRight(true), false); 
    t.checkExpect(newVert.isRight(false), false); 
    Vertex newVert1 = new Vertex(1,3);
    t.checkExpect(newVert1.isRight(true), false); 
    t.checkExpect(newVert1.isRight(false), false); 
  }

  //tests the vertEBoard method
  void testVertEBoard(Tester t) {
    this.initMaze();
    Vertex v1 = new Vertex(0,0);
    Player p1 = new Player(new Vertex(0, 0));
    p1.vertEBoard(v1);
    t.checkExpect(p1.playerVert, v1); 
  }

  //tests the playerVertVisited method
  void testPlayerVertVisited(Tester t) {
    this.initMaze();
    t.checkExpect(this.mazeEx.player.playerVert.visited, false); 
    this.mazeEx.player.playerVertVisited();
    t.checkExpect(this.mazeEx.player.playerVert.visited, true); 
    Player p1 = new Player(vert01);
    t.checkExpect(p1.playerVert.visited, false); 
    p1.playerVertVisited();
    t.checkExpect(p1.playerVert.visited, true); 
  }

  //tests the wasVisited method
  void testWasVisited(Tester t) {
    this.initMaze();
    t.checkExpect(this.mazeEx.player.playerVert.visited, false);
    this.mazeEx.player.playerVert.wasVisited();
    t.checkExpect(this.mazeEx.player.playerVert.visited, true); 
    Player p1 = new Player(vert00);
    t.checkExpect(p1.playerVert.visited, false);
    p1.playerVert.wasVisited();
    t.checkExpect(p1.playerVert.visited, true); 
  }

  //tests the playerVertRight method
  void testPlayerVertRight(Tester t) {
    this.initMaze();
    Vertex v11 = new Vertex(1, 1);
    v11.right = new Vertex(0, 1);
    Player p1 = new Player(v11);
    p1.playerVertRight();
    t.checkExpect(p1.playerVert, new Vertex(0, 1)); 
    Vertex v21 = new Vertex(2, 1);
    v21.right = new Vertex(1, 1);
    Player p2 = new Player(v21);
    p2.playerVertRight();
    t.checkExpect(p2.playerVert, new Vertex(1, 1)); 
  }

  //tests the playerVertLeft method
  void testPlayerVertLeft(Tester t) {
    this.initMaze();
    Vertex v11 = new Vertex(1, 1);
    Player p1 = new Player(v11);
    v11.left = new Vertex(0, 1);
    p1.playerVertLeft();
    t.checkExpect(p1.playerVert, new Vertex(0, 1)); 
    Vertex v21 = new Vertex(2, 1);
    Player p2 = new Player(v21);
    v21.left = new Vertex(1, 1);
    p2.playerVertLeft();
    t.checkExpect(p2.playerVert, new Vertex(1, 1)); 
  }

  //tests the playerVertUp method
  void testPlayerVertUp(Tester t) {
    this.initMaze();
    Vertex vM11 = new Vertex(1, - 1);
    vM11.top = new Vertex(0, 1);
    Player p1 = new Player(vM11);
    p1.playerVertUp();
    t.checkExpect(p1.playerVert, new Vertex(0, 1));
  }

  //tests the playerVertBottom method
  void testPlayerVertBottom(Tester t) {
    this.initMaze();
    Vertex vM11 = new Vertex(1, - 1);
    vM11.bottom = new Vertex(2, 1);
    Player p1 = new Player(vM11);
    p1.playerVertBottom();
    t.checkExpect(p1.playerVert, new Vertex(2, 1));
    Vertex v11 = new Vertex(1, 1);
    v11.bottom = new Vertex(1, 0);
    Player p2 = new Player(v11);
    p2.playerVertBottom();
    t.checkExpect(p2.playerVert, new Vertex(1, 0));
  }

  //tests the drawEdgeRight method
  void testDrawEdgeRight(Tester t) {
    this.initMaze();
    t.checkExpect(this.vert00.drawEdgeRight(), new LineImage(new 
        Posn(0, Maze.cellSize), Color.black).movePinhole(
            - Maze.cellSize, Maze.cellSize / - 1.8));
    t.checkExpect(this.vert01.drawEdgeRight(), new LineImage(new 
        Posn(0, Maze.cellSize), Color.black).movePinhole(
            - Maze.cellSize, Maze.cellSize / - 1.8));
    t.checkExpect(this.vert10.drawEdgeRight(), new LineImage(new 
        Posn(0, Maze.cellSize), Color.black).movePinhole(
            - Maze.cellSize, Maze.cellSize / - 1.8));
    t.checkExpect(this.vert02.drawEdgeRight(), new LineImage(new 
        Posn(0, Maze.cellSize), Color.black).movePinhole(
            - Maze.cellSize, Maze.cellSize / - 1.8));
    t.checkExpect(this.vert12.drawEdgeRight(), new LineImage(new 
        Posn(0, Maze.cellSize), Color.black).movePinhole(
            - Maze.cellSize, Maze.cellSize / - 1.8));
  }

  //tests the drawEdgeBottom method
  void testDrawEdgeBottom(Tester t) {
    this.initMaze();
    t.checkExpect(this.vert00.drawEdgeBottom(), new LineImage(
        new Posn(Maze.cellSize, 0), Color.black)
        .movePinhole(Maze.cellSize / -1.8, - Maze.cellSize));
    t.checkExpect(this.vert01.drawEdgeBottom(), new LineImage(
        new Posn(Maze.cellSize, 0), Color.black)
        .movePinhole(Maze.cellSize / -1.8, - Maze.cellSize));
    t.checkExpect(this.vert10.drawEdgeBottom(), new LineImage(
        new Posn(Maze.cellSize, 0), Color.black)
        .movePinhole(Maze.cellSize / -1.8, - Maze.cellSize));
    t.checkExpect(this.vert02.drawEdgeBottom(), new LineImage(
        new Posn(Maze.cellSize, 0), Color.black)
        .movePinhole(Maze.cellSize / -1.8, - Maze.cellSize));
    t.checkExpect(this.vert12.drawEdgeBottom(), new LineImage(
        new Posn(Maze.cellSize, 0), Color.black)
        .movePinhole(Maze.cellSize / -1.8, - Maze.cellSize));
  }

  //tests the draw method
  void testDraw(Tester t) {
    this.initMaze();
    t.checkExpect(this.vert00.draw(1, Color.BLUE), new 
        RectangleImage(Maze.cellSize - 3, Maze.cellSize - 3,
            OutlineMode.SOLID, Color.BLUE).movePinhole(-1 * Maze.cellSize / 1 / 2,
                -1 * Maze.cellSize / 1 / 2));
    t.checkExpect(this.vert00.draw(4, Color.RED), new 
        RectangleImage(Maze.cellSize - 3, Maze.cellSize - 3,
            OutlineMode.SOLID, Color.RED).movePinhole(-4 * Maze.cellSize / 4 / 2,
                -4 * Maze.cellSize / 4 / 2));
    t.checkExpect(this.vert01.draw(1, Color.BLUE), new 
        RectangleImage(Maze.cellSize - 3, Maze.cellSize - 3,
            OutlineMode.SOLID, Color.BLUE).movePinhole(-1 * Maze.cellSize / 1 / 2,
                -1 * Maze.cellSize / 1 / 2));
    t.checkExpect(this.vert01.draw(4, Color.RED), new 
        RectangleImage(Maze.cellSize - 3, Maze.cellSize - 3,
            OutlineMode.SOLID, Color.RED).movePinhole(-4 * Maze.cellSize / 4 / 2,
                -4 * Maze.cellSize / 4 / 2));
    t.checkExpect(this.vert10.draw(1, Color.BLUE), new 
        RectangleImage(Maze.cellSize - 3, Maze.cellSize - 3,
            OutlineMode.SOLID, Color.BLUE).movePinhole(-1 * Maze.cellSize / 1 / 2,
                -1 * Maze.cellSize / 1 / 2));
    t.checkExpect(this.vert10.draw(4, Color.RED), new 
        RectangleImage(Maze.cellSize - 3, Maze.cellSize - 3,
            OutlineMode.SOLID, Color.RED).movePinhole(-4 * Maze.cellSize / 4 / 2,
                -4 * Maze.cellSize / 4 / 2));
    t.checkExpect(this.vert02.draw(1, Color.BLUE), new 
        RectangleImage(Maze.cellSize - 3, Maze.cellSize - 3,
            OutlineMode.SOLID, Color.BLUE).movePinhole(-1 * Maze.cellSize / 1 / 2,
                -1 * Maze.cellSize / 1 / 2));
    t.checkExpect(this.vert02.draw(4, Color.RED), new 
        RectangleImage(Maze.cellSize - 3, Maze.cellSize - 3,
            OutlineMode.SOLID, Color.RED).movePinhole(-4 * Maze.cellSize / 4 / 2,
                -4 * Maze.cellSize / 4 / 2));
  }

  //tests the makeMaze method
  void testMakeMaze(Tester t) {
    this.initMaze();
    WorldScene testScene = new WorldScene(0, 0);
    testScene.placeImageXY(this.mazeEx.board.get(mazeEx.height - 1).get(mazeEx.width - 1)
        .draw(mazeEx.width, new Color(88, 24, 161)),
        (mazeEx.width - 1) * Maze.cellSize, (mazeEx.height - 1) * Maze.cellSize);
    for (int i = 0; i < mazeEx.height; i++) {
      for (int j = 0; j < mazeEx.width; j++) {
        Vertex boardGetIGetJ = mazeEx.board.get(i).get(j);
        mazeEx.makeBottom(mazeEx.board.get(i).get(j));
        mazeEx.makeRight(mazeEx.board.get(i).get(j));
        if (boardGetIGetJ.visited) {
          t.checkExpect(boardGetIGetJ.visited, true);
          testScene.placeImageXY(mazeEx.board.get(i).get(j).draw(mazeEx.width,
              new Color(89, 153, 255)), j * Maze.cellSize + 1, 
              i * Maze.cellSize + 1);
        }
        if (boardGetIGetJ.makeRight) {
          t.checkExpect(boardGetIGetJ.makeRight, true);
          testScene.placeImageXY(mazeEx.board.get(i).get(j).drawEdgeRight(),
              (Maze.cellSize * j),
              (Maze.cellSize * i));
        }
        if (boardGetIGetJ.makeBottom) {
          t.checkExpect(boardGetIGetJ.makeBottom, true);
          testScene.placeImageXY(mazeEx.board.get(i).get(j).drawEdgeBottom(),
              (Maze.cellSize * j),
              (Maze.cellSize * i));
        }
      }
    }
    t.checkExpect(this.mazeEx.makeMaze(), testScene);
    //test for bigger mazeEx
    Maze testMaze = new Maze(10, 10);
    WorldScene testScene1 = new WorldScene(0, 0);
    testScene1.placeImageXY(testMaze.board.get(testMaze.height - 1).get(testMaze.width - 1)
        .draw(testMaze.width, new Color(88, 24, 161)),
        (testMaze.width - 1) * Maze.cellSize, (testMaze.height - 1) * Maze.cellSize);
    for (int i = 0; i < testMaze.height; i++) {
      for (int j = 0; j < testMaze.width; j++) {
        Vertex boardGetIGetJ = testMaze.board.get(i).get(j);
        testMaze.makeBottom(testMaze.board.get(i).get(j));
        testMaze.makeRight(testMaze.board.get(i).get(j));
        if (boardGetIGetJ.visited) {
          t.checkExpect(boardGetIGetJ.visited, true);
          testScene1.placeImageXY(testMaze.board.get(i).get(j).draw(testMaze.width,
              new Color(89, 153, 255)), j * Maze.cellSize + 1, 
              i * Maze.cellSize + 1);
        }
        if (boardGetIGetJ.makeRight) {
          t.checkExpect(boardGetIGetJ.makeRight, true);
          testScene1.placeImageXY(testMaze.board.get(i).get(j).drawEdgeRight(),
              (Maze.cellSize * j),
              (Maze.cellSize * i));
        }
        if (boardGetIGetJ.makeBottom) {
          t.checkExpect(boardGetIGetJ.makeBottom, true);
          testScene1.placeImageXY(testMaze.board.get(i).get(j).drawEdgeBottom(),
              (Maze.cellSize * j),
              (Maze.cellSize * i));
        }
      }
    }
    t.checkExpect(testMaze.makeMaze(), testScene1);
  }

  //tests the makeScene method
  void testMakeScene(Tester t) {
    this.initMaze();
    TextImage winText = new TextImage("You Win! :)", mazeEx.height + 5, Color.BLACK);
    if (mazeEx.mazePath.size() > 1) {
      mazeEx.findEnd();
    }
    else if (mazeEx.mazePath.size() > 0) {
      mazeEx.drawEnd();
    }
    else if (mazeEx.isComplete && mazeEx.endVert.notNullPrev()) {
      mazeEx.traceBackPath();
    }
    if (mazeEx.player.vertEBoard(mazeEx.board.get(mazeEx.height - 1).get(mazeEx.width - 1))) {
      mazeEx.mazeScene.placeImageXY(winText, mazeEx.width * Maze.cellSize / 2, 
          mazeEx.height * Maze.cellSize / 2);
    }
    this.mazeEx.isComplete = true;
    t.checkExpect(mazeEx.makeScene().height, 0);
    t.checkExpect(mazeEx.makeScene().width, 0);
    t.checkExpect(mazeEx.mazePath.size(), 0);
    t.checkExpect(mazeEx.player.vertEBoard(mazeEx.board.get(mazeEx.height - 1)
        .get(mazeEx.width - 1)), false);
    t.checkExpect(mazeEx.isComplete, true);
    t.checkExpect(mazeEx.endVert.notNullPrev(), false);

  }

  //runs the maze game
  void testBigBang(Tester t) {
    // the first int is the width, the second int is the height
    Maze mazeWorld = new Maze(100, 60);
    //Maze mazeWorldVerticalBias = new Maze(100, 60, 1000);
    //Maze mazeWorldHorizontalBias = new Maze(100, 60, 10);
    mazeWorld.bigBang(mazeWorld.width * Maze.cellSize, mazeWorld.height 
        * Maze.cellSize, .1);

  }
}
