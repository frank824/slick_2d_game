import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import org.newdawn.slick.Input;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

/**
 * This class should be used to contain all the different objects in your game world, and schedule their interactions.
 * 
 * You are free to make ANY modifications you see fit.
 * These classes are provided simply as a starting point. You are not strictly required to use them.
 */
public class World {
	private static final String MAP_PATH = "assets/main.tmx";
	private static final String SOLID_PROPERTY = "solid";
	private static final String FILENAME = "assets/objects.csv";
	public ArrayList<Sprite> units = new ArrayList<>();
	public ArrayList<Sprite> command_centres = new ArrayList<>();
	public ArrayList<Sprite> pylons = new ArrayList<>();
	public ArrayList<Sprite> factories = new ArrayList<>();
	public ArrayList<Sprite> sprites = new ArrayList<>();
	public ArrayList<Sprite> resources = new ArrayList<>();
	private TiledMap map;
	public Camera camera = new Camera();
	Sprite tempResource = null;
	private Input lastInput;
	public int lastDelta;
	private static String sprite_type;
	private static float initial_x;
	private static float initial_y;
	private static String sprite_name; 
	private double mouseX;
	private double mouseY;
	private String resource_nums;
	private String instructions;
	
	public int total_metal = 500;
	public int total_unob = 0;
	
	public Input getInput() {
		return lastInput;
	}
	public int getDelta() {
		return lastDelta;
	}
	/**
	 * check whether the tile is free or occupied
	 * @param x each tile has its x coordinate
	 * @param y each tile has its y coordinate
	 * @return the status of the current position
	 */
	public boolean isPositionFree(double x, double y) {
		int tileId = map.getTileId(worldXToTileX(x), worldYToTileY(y), 0);
		return !Boolean.parseBoolean(map.getTileProperty(tileId, SOLID_PROPERTY, "false"));
	}
	/**
	 * get the width of the map
	 * @return tile width of the map
	 */
	public double getMapWidth() {
		return map.getWidth() * map.getTileWidth();
	}
	/**
	 * get the height of the map
	 * @return the tile height of the mao
	 */
	public double getMapHeight() {
		return map.getHeight() * map.getTileHeight();
	}
	/**
	 * the constructor for world to create new map and load objects from csv file
	 * @throws SlickException
	 */
	public World() throws SlickException {
		map = new TiledMap(MAP_PATH);
		readFile(FILENAME);
		
	}
	/**
	 * update all object status per frame
	 * @param input any input type reader 
	 * @param delta each frame time difference
	 */
	public void update(Input input, int delta) {
		lastInput = input;
		lastDelta = delta;
		
		if(input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
			for(Sprite c: command_centres) {
				c.isSelected = false;
			}
			for(Sprite c: pylons) {
				c.isSelected = false;
			}
			for(Sprite c: factories) {
				c.isSelected = false;
			}
			for(Sprite c: units) {
				c.isSelected = false;
			}
			mouseX = input.getMouseX();
			mouseY = input.getMouseY();
			//find units first
			Sprite sprite = null;
			sprite = findClosetSprite(camera.screenXToGlobalX(mouseX),
					camera.screenYToGlobalY(mouseY), units);
			if(sprite != null) {
				sprite.isSelected = true;
				Camera.cameraFree = false;
				camera.followSprite(sprite);
			}else {
				sprite = findClosetSprite(camera.screenXToGlobalX(mouseX),
						camera.screenYToGlobalY(mouseY), command_centres);
				if(sprite != null) {
					sprite.isSelected = true;
					Camera.cameraFree = false;
					camera.followSprite(sprite);
				}else {
					sprite = findClosetSprite(camera.screenXToGlobalX(mouseX),
							camera.screenYToGlobalY(mouseY), factories);
					if(sprite != null) {
						sprite.isSelected = true;
						Camera.cameraFree = false;
						camera.followSprite(sprite);
					}else {
						sprite = findClosetSprite(camera.screenXToGlobalX(mouseX),
								camera.screenYToGlobalY(mouseY), pylons);
						if(sprite != null) {
							sprite.isSelected = true;
							Camera.cameraFree = false;
							camera.followSprite(sprite);
						}
					}
				}
			}
			
		}
		for(Sprite s:units) {
			s.update(this);
		}
		for(Sprite s:command_centres) {
			s.update(this);
		}
		for(Sprite r:pylons) {
			r.update(this);
		}
		for(Sprite f:factories) {
			f.update(this);
		}
		for(Sprite r:resources) {
			r.update(this);
		}
		camera.update(this);
	}
	
	/**
	 * find the closet sprite to my input value location (x,y)
	 * @param x any input x coordinate
	 * @param y any input y coordinate
	 * @param sprites a list that contains all the sprite
	 * @return array list that contains different sprite depending on the input array list
	 */
	public Sprite findClosetSprite(double x, double y, ArrayList<Sprite> sprites) {
		Sprite sprite = null;
		double dis = Integer.MAX_VALUE;
		double result;
		for(Sprite s:sprites) {
			result = distance(s.getX(),s.getY(),x,y);
			if(result<dis) {
				dis = result;
				sprite = s;
			}
		}
		if(dis<32 && dis > 0 ) {
			return sprite;
		}
		return null;
	}
	/**
	 * render the graph each frame
	 * @param g graphics class object
	 */
	public void render(Graphics g) {
		map.render((int)camera.globalXToScreenX(0),
				   (int)camera.globalYToScreenY(0));
		//render command_centres
		for(Sprite c: command_centres) {
			if(c.isSelected) {
				sprite_name = String.format("%s", c.marker);  
				instructions = String.format("Press 1: scout\nPress 2: builder\nPress 3: engineer");
				g.drawString(sprite_name, 32, 100);
				g.drawString(instructions, 32, 120);
			}
			c.render();
		}
		//render pylons
		for(Sprite p: pylons) {
			if(p.isSelected) {  
				g.drawString(p.activation_stats, 32, 100);
			}
			p.render();
		}
		//render factories
		for(Sprite f: factories) {
			if(f.isSelected) {
				sprite_name = String.format("%s", f.marker);  
				instructions = String.format("Press 1: truck");
				g.drawString(sprite_name, 32, 100);
				g.drawString(instructions, 32, 120);
			}
			f.render();
		}
		//render resources
		for(int i =0; i<resources.size(); i++) {
			Sprite r = resources.get(i);
			if(r.resourceNum <= 0) {
				resources.remove(r);
			}
			
			r.render();
		}
		//render units
		//for(Sprite u: units) {
		for (int i =0; i<units.size(); i++) {
			Sprite u = units.get(i);
			if(u.isSelected) {
				sprite_name = String.format("%s", u.marker);  
				g.drawString(sprite_name, 32, 100);
				if(u.marker.contentEquals("builder")) {
					instructions = String.format("Press 1: factory");
					g.drawString(instructions, 32, 120);
				}else if(u.marker.contentEquals("truck")){
					instructions = String.format("Press 1: command_centre");
					g.drawString(instructions, 32, 120);
				}
			}
			if (u instanceof Truck) {
				Truck b = (Truck)u;
				if(b.getFinishBuilding()) {
					units.remove(u);
				}
			}
			u.render();
		}
		resource_nums = String.format("Metal: %d\nUnobtainium: %d", total_metal, total_unob);  
		g.drawString(resource_nums, 32, 32);
		
	}
	// This should probably be in a separate static utilities class, but it's a bit excessive for one method.
	/**
	 * find the distance of two points
	 * @param x1 the x coordinate of first point
	 * @param y1 the y coordinate of first point
	 * @param x2 the x coordinate of second point
	 * @param y2 the y coordinate of second point
	 * @return the distance between two specified points
	 */
	public static double distance(double x1, double y1, double x2, double y2) {
		return (double)Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
	}
	
	private int worldXToTileX(double x) {
		return (int)(x / map.getTileWidth());
	}
	private int worldYToTileY(double y) {
		return (int)(y / map.getTileHeight());
	}
	/**
	 * read the file from a scv file in order to render a whole map
	 * @param filename name of the csv file
	 */
	public void readFile(String filename) {
		// Open the file
		try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
			String line;			
			// Loop over every line of the file
			while ((line = reader.readLine()) != null) {		
				// Split the line into parts
				String[] parts = line.split(",");
				sprite_type = parts[0];
				initial_x = Float.parseFloat(parts[1]);
				initial_y = Float.parseFloat(parts[2]);
				if(sprite_type.equals("command_centre")) {
					Sprite sprite = new Command_centre(camera, initial_x, initial_y);
					sprites.add(sprite);
					command_centres.add(sprite);
				}else if(sprite_type.equals("pylon")){
					Sprite sprite = new Pylon(camera, initial_x, initial_y);
					sprites.add(sprite);
					pylons.add(sprite);
				}else if(sprite_type.equals("factory")){
					Sprite sprite = new Factory(camera, initial_x, initial_y);
					sprites.add(sprite);
					factories.add(sprite);
				}else if(sprite_type.equals("builder")) {
					Sprite sprite = new Builder(camera, initial_x, initial_y);
					sprites.add(sprite);
					units.add(sprite);
				}else if(sprite_type.equals("engineer")) {
					Sprite sprite = new Engineer(camera, initial_x, initial_y);
					sprites.add(sprite);
					units.add(sprite);
				}else if(sprite_type.equals("scout")) {
					Sprite sprite = new Scout(camera, initial_x, initial_y);
					sprites.add(sprite);
					units.add(sprite);
				}else if(sprite_type.equals("truck")) {
					Sprite sprite = new Truck(camera, initial_x, initial_y);
					sprites.add(sprite);
					units.add(sprite);
				}else if(sprite_type.equals("unobtainium_mine")) {
					Sprite sprite = new Unobtainium(camera, initial_x, initial_y);
					resources.add(sprite);
				}else if(sprite_type.equals("metal_mine")) {
					Sprite sprite = new Metal_mine(camera, initial_x, initial_y);
					resources.add(sprite);
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
}
