import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Metal_mine extends Sprite{
	private final static String METAL_PATH = "assets/resources/metal_mine.png";
	/**
	 * constructor to initialize a metal resource for its properties
	 * @param camera the camera that is owned by the metal mine resource
	 * @param x metal mine x coord
	 * @param y metal mine y coord
	 * @throws SlickException
	 */
	public Metal_mine(Camera camera, float x, float y)throws SlickException {
		super(camera, x, y);
		image = new Image(METAL_PATH);
		this.camera = camera;
		camera.followSprite(this);
		resourceNum = 500;
		marker = "metal";
		this.highlighter = new Image(HIGHLIGHTER_LARGE_PATH);
	}
}