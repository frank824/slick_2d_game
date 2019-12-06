import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public class Truck extends Sprite{
	private static final String TRUCK_PATH = "assets/units/truck.png";
	private Sprite cmd;
	private boolean cmdBuild = false;
	private static final int build_cmd_time = 15000;
	private boolean finished_building = false;
	/**
	 * the constructor of a truck to initialize components
	 * @param camera a camera owned by a truck
	 * @param x the x coordinate of a truck
	 * @param y the y coordinate of a truck
	 * @throws SlickException
	 */
	public Truck(Camera camera, float x, float y)throws SlickException{
		super(camera, x ,y);
		image = new Image(TRUCK_PATH);
		speed = 0.25;
		this.camera = camera;
		camera.followSprite(this);
		this.highlighter = new Image(HIGHLIGHTER_PATH);
		marker = "truck";
	}
	/**
	 * update the truck status on the map
	 */
	public void update(World world) {
		timerCom += world.getDelta();
		if(isSelected) {
			Input input = world.getInput();
			if(input.isMousePressed(Input.MOUSE_RIGHT_BUTTON)) {
				targetX = camera.screenXToGlobalX(input.getMouseX());
				targetY = camera.screenYToGlobalY(input.getMouseY());
				isMoving = false;
			}
		
			if(input.isKeyPressed(Input.KEY_1)) {
				timerCom = 0;
				cmdBuild = true;
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
		
		if(isMoving) {
			cmdBuild = false;
		}
		
		
		try {
			if(cmdBuild && timerCom>=build_cmd_time) {
				cmd = new Command_centre(world.camera, (float)this.x, (float)this.y);
				world.command_centres.add(cmd);
				finished_building = true;
			}
		}catch (SlickException e) {
			e.printStackTrace();
		}
	}
	/**
	 * get the status of whether a truck has finished building a command centre
	 * @return building status
	 */
	public boolean getFinishBuilding() {
		return finished_building;
	}
}
