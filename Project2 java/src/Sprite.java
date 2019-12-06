import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public abstract class Sprite {	
	public final static String HIGHLIGHTER_PATH = "assets/highlight.png";
	public final static String HIGHLIGHTER_LARGE_PATH = "assets/highlight_large.png";	
	public final static int TRAINING_TIME = 5000;
	public double x;
	public double y;
	public double targetX;
	public double targetY;
	public String marker;
	public boolean isMoving = false;
	public boolean forward = true;
	public boolean backward = false;
	public float time = 0;
	public boolean isSelected = false;
	public boolean carrying = false;
	public static int carry_once;
	public double speed;
	public Camera camera;
	public Image image;
	public Image highlighter;
	public String target_coord;
	public int resourceNum;
	public int timerCom = 0;
	public String activation_stats;
	public boolean finished_building = false;
	public boolean isMining = false;
	
	public void selectedStats() {
		isSelected = true;
	}
	/**
	 * get the x coordinate of a sprite
	 * @return the x coordinate of a sprite
	 */
	public double getX() { return x; }
	/**
	 * get the y coordinate of a sprite
	 * @return the y coordinate of a sprite
	 */
	public double getY() { return y; }
	/**
	 * constructor to initialize a sprite for each component
	 * @param camera the camera owned by a sprite
	 * @param x the x coordinate of a sprite
	 * @param y the y coordinate of a sprite
	 * @throws SlickException
	 */
	public Sprite(Camera camera, float x, float y) throws SlickException {
		this.x = x;
		this.y = y;
		this.targetX = x;
		this.targetY = y;
	}
	/**
	 * update world
	 * @param world
	 */
	public void update(World world) {}
	/**
	 * keep camera original position
	 */
	public void resetCamera() {
		targetX = x;
		targetY = y;
	}
	/**
	 * initialize resource initial amount
	 * @param n the initial resource amount of a resource 
	 */
	public void setResourceNum(int n){
		resourceNum = n;
	}
	/**
	 * get the real time resource number left
	 * @return the current resource amount of a resource
	 */
	public int getResourceNum(){
		return resourceNum;
	}
	/**
	 * set the camera to the target coordinates
	 * @param x current x coordinate of an object
	 * @param y current y coordinate of an object
	 */
	public void setCamera(double x, double y) {
		this.targetX = x;
		this.targetY = y;
	}

	/**
	 * come back and forth when we are in a loop of mining
	 * @param centre nearest command center
	 * @param resource nearest resource to mine
	 */
	public void move(Sprite centre, Sprite resource) {
		if(forward) {
			setCamera(centre.getX(), centre.getY());
			if(World.distance(x, y, targetX, targetY) <= speed) {
				resetCamera();
				forward = false;
				backward = true;
			}
		}else {
			setCamera(resource.getX(), resource.getY());
			if(World.distance(x, y, targetX, targetY) <= speed) {
				resetCamera();
				forward = true;
				backward = false;
			}
		}
	}
	/**
	 * we have to reduce each mine's left resource after each mining process
	 * @param n each time a resource is reduced from its total capital after mining each time
	 */
	public void reduceMineNum(int n) {
		resourceNum -= n;
	}
	/**
	 * render the sprite
	 */
	public void render() {
		if(isSelected) {
			highlighter.drawCentered((int)camera.globalXToScreenX(x), (int)camera.globalYToScreenY(y));
		}
		image.drawCentered((int)camera.globalXToScreenX(x),(int)camera.globalYToScreenY(y));
		
	}
}

	

