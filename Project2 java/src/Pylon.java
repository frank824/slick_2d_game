import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Pylon extends Sprite {
	private final static String PYLON_PATH = "assets/buildings/pylon.png";
	private final static String PYLON_ACTIVE_PATH = "assets/buildings/pylon_active.png";
	/**
	 * constructor to initialize a pylon for its properties
	 * @param camera the camera that is owned by the pylon
	 * @param x pylon x coord
	 * @param y pylon y coord
	 * @throws SlickException
	 */
	public Pylon(Camera camera, float x, float y)throws SlickException{
		super(camera, x ,y);
		image = new Image(PYLON_PATH);
		this.camera = camera;
		camera.followSprite(this);
		camera.followSprite(this);
		this.highlighter = new Image(HIGHLIGHTER_LARGE_PATH);
		marker = "pylon";
		activation_stats = "Pylon: not activated";
	}
	private boolean resource_added = false;
	private Sprite sprite = null;
	/**
	 * update details for all pylons on the map
	 */
	public void update(World world) {
		sprite = world.findClosetSprite(this.x, this.y, world.units);
		if(sprite != null) {
			try {
				image = new Image(PYLON_ACTIVE_PATH);
				activation_stats = "Pylon: activated";
				if(resource_added == false) {
					Engineer.addCarryNum(1);
					resource_added = true;
				}
			} catch (SlickException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
