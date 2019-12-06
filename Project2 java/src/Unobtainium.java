import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Unobtainium extends Sprite{
	public final static String UNOBTAINIUM_PATH = "assets/resources/unobtainium_mine.png";
	/**
	 * the constructor of an unobtanium resource to initialize each component
	 * @param camera a camera owned by the unobtainium
	 * @param x location of unobtainium representing x coordinate
	 * @param y location of unobtainium representing y coordinate
	 * @throws SlickException
	 */
	public Unobtainium(Camera camera, float x, float y) throws SlickException{
		super(camera, x, y);
		image = new Image(UNOBTAINIUM_PATH);
		this.camera = camera;
		camera.followSprite(this);
		resourceNum = 50;
		marker = "unobtainium";
	}

}
