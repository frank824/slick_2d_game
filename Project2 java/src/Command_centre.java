import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
/**
 * 
 * @author yichaoyao
 *
 */
public class Command_centre extends Sprite {
	
	private final static String COMMANDCENTRE_PATH = "assets/buildings/command_centre.png";
	private Sprite scout;
	private Sprite builder;
	private Sprite engineer;
	// private Timer timer = new Timer();
	 
	private static final int scout_cost = 5;
	private static final int builder_cost = 10;
	private static final int engineer_cost = 20;
	
	private boolean scoutBuild = false; 
	private boolean builderBuild = false; 
	private boolean engineerBuild = false; 
	
	private int scout_build_time = 0;
	private int builder_build_time = 0;
	private int engineer_build_time = 0;
	/**
	 * constructor to initialize the command centre for each component
	 * @param camera the camera owned by the command_centre
	 * @param x the current x coord of the command centre
	 * @param y the current y coord of the command centre
	 * @throws SlickException
	 */
	public Command_centre(Camera camera, float x, float y)throws SlickException{
		super(camera, x, y);
		image = new Image(COMMANDCENTRE_PATH);
		this.camera = camera;
		camera.followSprite(this);
		this.highlighter = new Image(HIGHLIGHTER_LARGE_PATH);
		marker = "command_centre";
	}
	
	//private boolean building = false;
	/**
	 * update details for all current selected command centre
	 */
	public void update(World world) {
		scout_build_time += world.getDelta();
		builder_build_time += world.getDelta();
		engineer_build_time += world.getDelta();
		if(isSelected) {
			Input input = world.getInput();
			if(input.isKeyPressed(Input.KEY_1) && world.total_metal - scout_cost >= 0) {
				scout_build_time = 0; 
				scoutBuild = true; 
				builderBuild = false; 
				engineerBuild = false; 
			}
			
			if(input.isKeyPressed(Input.KEY_2) && world.total_metal - builder_cost >= 0) {
				builder_build_time = 0; 
				scoutBuild = false; 
				builderBuild = true; 
				engineerBuild = false; 
			} 
			
			if(input.isKeyPressed(Input.KEY_3) && world.total_metal - engineer_cost >= 0) {
				engineer_build_time = 0; 
				scoutBuild = false; 
				builderBuild = false; 
				engineerBuild = true; 
			} 
		}
		try {
			if (scoutBuild && scout_build_time>=TRAINING_TIME) {
				scout = new Scout(this.camera, (float)this.x, (float)this.y);
				world.units.add(scout);
				world.total_metal -= scout_cost;
				scoutBuild = false;
			}
			if (builderBuild && builder_build_time>=TRAINING_TIME) {
				builder = new Builder(this.camera, (float)this.x, (float)this.y);
				world.units.add(builder);
				world.total_metal -= builder_cost;
				builderBuild = false; 
			}
			if (engineerBuild && engineer_build_time>=TRAINING_TIME) {
				engineer = new Engineer(this.camera, (float)this.x, (float)this.y);
				world.units.add(engineer);
				world.total_metal -= engineer_cost;
				engineerBuild = false; 
			}
		}catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}
}
	
