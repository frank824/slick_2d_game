import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
/**
 * 
 * @author yichaoyao
 *
 */
public class Builder extends Sprite{
	private final static String BUILDER_PATH = "assets/units/builder.png";
	/**
	 * constructor for builder to initialize all the components
	 * @param camera the camera that is for the builder
	 * @param x builder position x coord
	 * @param y builder position y coord
	 * @throws SlickException 
	 */
	public Builder(Camera camera, float x, float y)throws SlickException {
		super(camera, x, y);
		image = new Image(BUILDER_PATH);
		speed = 0.1;
		this.camera = camera;
		camera.followSprite(this);
		this.highlighter = new Image(HIGHLIGHTER_PATH);
		marker = "builder";
	}
	private Sprite factory;
	private final static int build_factory_time = 10000;
	private final static int factory_cost = 100;
	private boolean factoryBuild = false;
	/**
	 * update detail for each builder on the map
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
			
			if(input.isKeyPressed(Input.KEY_1) && world.total_metal - factory_cost >= 0) {
				timerCom = 0; 
				factoryBuild = true; 
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
			factoryBuild = false;
		}
		try {
			if(factoryBuild && timerCom>=build_factory_time) {
				factory = new Factory(world.camera, (float)this.x, (float)this.y);
				world.factories.add(factory);
				world.total_metal -= factory_cost;
				factoryBuild = false;
			}
		}catch (SlickException e) {
			e.printStackTrace();
		}
		
		
	}
}
