import java.util.ArrayList;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Input;
/**
 * 
 * @author yichaoyao
 *
 */
public class Engineer extends Sprite {
	private final static String Engineer_PATH = "assets/units/engineer.png";
	private final int command_centre = 1;
	private final int resource_mark = 0;
	private Sprite tempResource;
	private boolean isMining = false;
	private Sprite centre;
	private Sprite resource;
	private final int WAITING_TIME = 5000;
	private boolean isCarrying = false;
	/**
	 * constructor to initialize the engineer for each component
	 * @param camera the camera that is owned by engineer
	 * @param x engineer x coordinate
	 * @param y engineer y coordinate
	 * @throws SlickException
	 */
	public Engineer(Camera camera, float x, float y)throws SlickException {
		super(camera, x, y);
		image = new Image(Engineer_PATH);
		speed = 0.1;
		this.camera = camera;
		camera.followSprite(this);
		this.highlighter = new Image(HIGHLIGHTER_PATH);
		marker = "engineer";
		carry_once = 2;
	}
	/**
	 * update details for each engineer on the map
	 */
	public void update(World world) {
		if(isSelected) {
			Input input = world.getInput();
			if(input.isMousePressed(Input.MOUSE_RIGHT_BUTTON)) {
				targetX = camera.screenXToGlobalX(input.getMouseX());
				targetY = camera.screenYToGlobalY(input.getMouseY());
				isMoving = false;
				isMining = false;
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
		centre = findObject(x, y, command_centre, world.command_centres);
		resource = findObject(x, y, resource_mark, world.resources);
		if(resource != null && !isMining) {
			if(time < WAITING_TIME) {
				time += world.getDelta();
			}else {
				isMoving = true;
				isMining = true;
				tempResource = resource;
			}
		}
		if(resource == null && !isMining) {
			time = 0;
		}
	
		if(isMining && tempResource.resourceNum > 0) {
	
			if(forward && World.distance(x, y, centre.getX(), centre.getY())>10 && tempResource.marker.contentEquals("metal")) {
				isCarrying = true;
			}else if(forward && World.distance(x, y, centre.getX(), centre.getY())>10 && tempResource.marker.contentEquals("unobtainium")){
			    isCarrying = true;
			}
			move(centre, tempResource);
			if(isCarrying && World.distance(x, y,centre.getX(), centre.getY())<10 && tempResource.marker.contentEquals("metal")) {
				world.total_metal += Engineer.carry_once;
				tempResource.reduceMineNum(carry_once);
				isCarrying = false;
			}
			else if(isCarrying && World.distance(x, y, centre.getX(), centre.getY())<10 && tempResource.marker.contentEquals("unobtainium")) {
				world.total_unob += Engineer.carry_once;
				tempResource.reduceMineNum(Engineer.carry_once);
				isCarrying = false;
			}
		}
	}
	/**
	 * update the carry limit for each engineer corresponding to each pylon activation
	 * @param m is a value that is added to engineer carrying limit each time a pylon is activated
	 */
	public static void addCarryNum(int m){
		carry_once += m;
	}
	
	private Sprite findObject(double x, double y, int type_search, ArrayList<Sprite> sprites) {
		Sprite sprite = null;
		double dist = Integer.MAX_VALUE;
		double result;
		for(Sprite s:sprites) {
			result = World.distance(s.getX(),s.getY(),x,y);
			if(result<dist) {
				dist = result;
				sprite = s;
			}
		}
		if(type_search == resource_mark) {
			if(dist<32 & dist > 0) {
				return sprite;
			}
			return null;
		}
		if(type_search == command_centre) {
			return sprite;
		}
		return null;
	}
}
