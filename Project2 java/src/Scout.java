import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public class Scout extends Sprite{
	private final static String SCOUT_PATH = "assets/units/scout.png";
	/**
	 * constructor to initialize scouts for each component
	 * @param camera the camera that is owned by a scout
	 * @param x the x coordinate of the a scout
	 * @param y the y coordinate of the a scout
	 * @throws SlickException
	 */
	public Scout(Camera camera, float x, float y)throws SlickException{
		super(camera, x, y);
		image = new Image(SCOUT_PATH);
		speed = 0.3;
		this.camera = camera;
		camera.followSprite(this);
		this.highlighter = new Image(HIGHLIGHTER_PATH);
		marker = "scout";
	}
	/**
	 * update details for sprites on the map 
	 */
	public void update(World world) {
		if(isSelected) {
			Input input = world.getInput();
			
			if(input.isMousePressed(Input.MOUSE_RIGHT_BUTTON)) {
				targetX = camera.screenXToGlobalX(input.getMouseX());
				targetY = camera.screenYToGlobalY(input.getMouseY());
				isMoving = false;
			}
		}
		if(World.distance(x, y, targetX, targetY) <= speed) {
			isMoving = false;
			resetCamera();
		} else {
			double theta = Math.atan2(targetY - y, targetX - x);
			double dx = (double)Math.cos(theta)*world.getDelta()*speed;
			double dy = (double)Math.sin(theta)*world.getDelta()*speed;
			if(world.isPositionFree(x + dx, y + dy)) {
				isMoving = true;
				x += dx;
				y += dy;
			} else {
				resetCamera();
			}
		}
		
	}


}
