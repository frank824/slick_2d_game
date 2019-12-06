import org.newdawn.slick.Input;
/**
 * This class should be used to restrict the game's view to a subset of the entire world.
 * 
 * You are free to make ANY modifications you see fit.
 * These classes are provided simply as a starting point. You are not strictly required to use them.
 */

public class Camera {
	private double x = 300;
	private double y = 300;
	private Sprite target;
	public static boolean cameraFree = false;
	private int delta;
	private double camera_speed = 0.4;
	private double dx;
	private double dy;
	/**
	 * follow the selected sprite 
	 * @param target the current followd object
	 */
	public void followSprite(Sprite target) {
		this.target = target;
	}
	/**
	 * transform global x coord to local x
	 * @param x the global variable of x coords
	 * @return the screen variable of x coords
	 */
	public double globalXToScreenX(double x) {
		return x - this.x;
	}
	/**
	 * transform global y coord to local y
	 * @param y the global variable of y coords
	 * @return the screen variable of y coords
	 */
	public double globalYToScreenY(double y) {
		return y - this.y;
	}
	/**
	 * transform local x to global x
	 * @param x the x coord of the current screen camera
	 * @return x coord of the whole map location of camera
	 */
	public double screenXToGlobalX(double x) {
		return x + this.x;
	}
	/**
	 * transform local y to global y
	 * @param y the y coord of the current screen camera
	 * @return y coord of the whole map location of camera
	 */
	public double screenYToGlobalY(double y) {
		return y + this.y;
	}
	/**
	 * update camera changes on the map
	 * @param world
	 */
	public void update(World world) {
		Input input = world.getInput();
		delta = world.getDelta();
		if(input.isKeyDown(Input.KEY_A)){
			cameraFree = true;
			dx = delta*camera_speed;
			x -= dx;
			x = Math.max(x, 0);
		}
		if(input.isKeyDown(Input.KEY_D)) {
			cameraFree = true;
			dx = delta*camera_speed;
			x += dx;
			x = Math.min(x, world.getMapWidth() - App.WINDOW_WIDTH);
		}
		if(input.isKeyDown(Input.KEY_W)) {
			cameraFree = true;
			dy = delta*camera_speed;
			y -= dy;
			y = Math.max(y, 0);
		}
		if(input.isKeyDown(Input.KEY_S)) {
			cameraFree = true;
			dy = delta*camera_speed;
			y += dy;
			y = Math.min(y, world.getMapHeight() - App.WINDOW_HEIGHT);
		}
		
		if(cameraFree == false) {
			double targetX = target.getX() - App.WINDOW_WIDTH / 2;
			double targetY = target.getY() - App.WINDOW_HEIGHT / 2;
			
			x = Math.min(targetX, world.getMapWidth() -	 App.WINDOW_WIDTH);
			x = Math.max(x, 0);
			y = Math.min(targetY, world.getMapHeight() - App.WINDOW_HEIGHT);
			y = Math.max(y, 0);
		}
	}
}
