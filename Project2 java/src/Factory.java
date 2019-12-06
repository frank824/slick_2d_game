import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public class Factory extends Sprite {
	private final static String FACTORY_PATH = "assets/buildings/factory.png";
	
	private Sprite truck;
	private boolean truckBuild = false;
	private final static int truck_cost = 150;
	/**
	 * constructor to initialize a factory for each component
	 * @param camera the camera owned by the sprites
	 * @param x the x coordinate of the factory location
	 * @param y the y coordinate of the factory location
	 * @throws SlickException
	 */
	public Factory(Camera camera, float x, float y)throws SlickException{
		super(camera, x, y);
		image = new Image(FACTORY_PATH);
		this.camera = camera;
		camera.followSprite(this);
		this.highlighter = new Image(HIGHLIGHTER_LARGE_PATH);
		marker = "factory";
	}
	/**
	 * update details for selected factory
	 */
	public void update(World world) {
		timerCom += world.getDelta();
		if(isSelected) {
			Input input = world.getInput();
			if(input.isKeyPressed(Input.KEY_1) && world.total_metal - truck_cost >= 0) {
				timerCom = 0; 
				truckBuild = true; 
			}
		}
		try {
			if (truckBuild && timerCom>=TRAINING_TIME) {
				truck = new Truck(world.camera, (float)this.x, (float)this.y);
				world.units.add(truck);
				world.total_metal -= truck_cost;
				truckBuild = false; 
			}
		}catch (SlickException e) {
			e.printStackTrace();
		}
	}
}
