package system;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.collision.CollisionResults;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.*;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import com.simsilica.es.*;
import component.*;
import configurations.Constants;
import gui.GameHudController;

public class ControlAppState extends AbstractAppState {

	private static final String DEFENDER_MOVE_RIGHT = "DEFENDER_MOVE_RIGHT";
	private static final String DEFENDER_MOVE_LEFT = "DEFENDER_MOVE_LEFT";
	private static final String DEFENDER_MOVE_UP = "DEFENDER_MOVE_UP";
	private static final String DEFENDER_MOVE_DOWN = "DEFENDER_MOVE_DOWN";
	private static final String SHOOT = "SHOOT";
	private static final String CURSOR_MOVE_RIGHT = "CURSOR_MOVE_RIGHT";
	private static final String CURSOR_MOVE_LEFT = "CURSOR_MOVE_LEFT";
	private static final String CURSOR_MOVE_UP = "CURSOR_MOVE_UP";
	private static final String CURSOR_MOVE_DOWN = "CURSOR_MOVE_DOWN";
	private static final String MOUSE_LEFT_BUTTON_CLICK = "MOUSE_LEFT_BUTTON_CLICK";
	private static final Integer BULLET_ATTACK_POWER = 1;
	private static final Integer BULLET_COLLISION_SHAPE = 1;
	private static final Integer BULLET_SPEED = 20;
	private static final Integer BULLET_DECAY_DELTA_MILLIS = 20000;


	private InputManager inputManager;
	private EntityData ed;
	private SimpleApplication app;
	private Vector3f locationDefender;
	private Vector3f locationCursor;
	private WatchedEntity watchedEntityDefender;
	private WatchedEntity watchedEntityCursor;
	private float speedDefender = 1f;
	private float speedCursor = 1f;

	private final AnalogListener analogListener = (String name, float value, float tpf) -> {
		watchedEntityDefender.applyChanges();
		watchedEntityCursor.applyChanges();

		if (name.equals(DEFENDER_MOVE_LEFT)) {
			watchedEntityDefender.set(new Position(new Vector3f(locationDefender.getX() - speedDefender, locationDefender.getY(), 0), new Vector3f()));
		}
		if (name.equals(DEFENDER_MOVE_RIGHT)) {
			watchedEntityDefender.set(new Position(new Vector3f(locationDefender.getX() + speedDefender, locationDefender.getY(), 0), new Vector3f()));
		}
		if (name.equals(DEFENDER_MOVE_UP)) {
			watchedEntityDefender.set(new Position(new Vector3f(locationDefender.getX(), locationDefender.getY() + speedDefender, 0), new Vector3f()));
		}
		if (name.equals(DEFENDER_MOVE_DOWN)) {
			watchedEntityDefender.set(new Position(new Vector3f(locationDefender.getX(), locationDefender.getY() - speedDefender, 0), new Vector3f()));
		}
		if (name.equals(CURSOR_MOVE_RIGHT)) {
			watchedEntityCursor.set(new Position(new Vector3f(locationCursor.getX() + speedCursor, locationCursor.getY(), 0), new Vector3f()));
		}
		if (name.equals(CURSOR_MOVE_LEFT)) {
			watchedEntityCursor.set(new Position(new Vector3f(locationCursor.getX() - speedCursor, locationCursor.getY(), 0), new Vector3f()));
		}
		if (name.equals(CURSOR_MOVE_UP)) {
			watchedEntityCursor.set(new Position(new Vector3f(locationCursor.getX(), locationCursor.getY() + speedCursor, 0), new Vector3f()));
		}
		if (name.equals(CURSOR_MOVE_DOWN)) {
			watchedEntityCursor.set(new Position(new Vector3f(locationCursor.getX(), locationCursor.getY() - speedCursor, 0), new Vector3f()));
		}
		locationDefender = watchedEntityDefender.get(Position.class).getLocation();
		locationCursor = watchedEntityCursor.get(Position.class).getLocation();
	};
	private final ActionListener actionListener = (String name, boolean isPressed, float tpf) -> {
 		watchedEntityDefender.applyChanges();
		watchedEntityCursor.applyChanges();
		if (name.equals(SHOOT) && !isPressed) {
			EntityId bullet = ed.createEntity();
			Vector3f directionVectorNormalized = locationCursor.subtract(locationDefender).normalize();
			Vector3f bulletCreationPosition = locationDefender.add(directionVectorNormalized.mult(3f));
			ed.setComponents(bullet,
					new Model(Model.BULLET),
					new Attack(BULLET_ATTACK_POWER),
					new CollisionShape(BULLET_COLLISION_SHAPE),
					new Position(bulletCreationPosition, directionVectorNormalized),
					new Direction(directionVectorNormalized, Constants.ON_GETTING_TO_STRATEGY.CONTINUE_MOVEMENT_TO, null),
					new Speed(BULLET_SPEED),
					new Decay(BULLET_DECAY_DELTA_MILLIS));

		}
		if (name.equals(MOUSE_LEFT_BUTTON_CLICK) && !isPressed) {
			createRay();
		}
	};

	private void createRay() {
		CollisionResults results = new CollisionResults();
		Vector2f click2d = inputManager.getCursorPosition().clone();
		Camera camera = this.app.getStateManager().getState(CameraState.class).getCamera();
		Vector3f click3d = camera.getWorldCoordinates(
				click2d, 0f).clone();
		Vector3f dir = camera.getWorldCoordinates(
				click2d, 1f).subtractLocal(click3d).normalizeLocal();
		Ray ray = new Ray(click3d, dir);
		Node shootables = new Node("Shootables");
		shootables.collideWith(ray, results);
		System.out.printf("collide with : " + results.size());
		EntityId bullet = ed.createEntity();
		ed.setComponents(bullet,
				new Model(Model.BULLET),
				new Attack(BULLET_ATTACK_POWER),
				new CollisionShape(BULLET_COLLISION_SHAPE),
				new Position(click3d, dir),
				new Direction(dir, Constants.ON_GETTING_TO_STRATEGY.CONTINUE_MOVEMENT_TO, null),
				new Speed(BULLET_SPEED),
				new Decay(BULLET_DECAY_DELTA_MILLIS));
	}

	public WatchedEntity getWatchedEntityCursor() {
		return watchedEntityCursor;
	}

	public void setWatchedEntityCursor(WatchedEntity watchedEntityCursor) {
		this.watchedEntityCursor = watchedEntityCursor;
	}

	public WatchedEntity getWatchedEntityDefender() {
		return watchedEntityDefender;
	}

	public void setWatchedEntityDefender(WatchedEntity watchedEntityDefender) {
		this.watchedEntityDefender = watchedEntityDefender;
	}

	@Override
	public void initialize(AppStateManager stateManager, Application app) {

		super.initialize(stateManager, app);
		this.app = (SimpleApplication) app;
		ed = this.app.getStateManager().getState(EntityDataState.class).getEntityData();
		inputManager = app.getInputManager();

		watchedEntityCursor.applyChanges();
		locationCursor = watchedEntityCursor.get(Position.class).getLocation();
		watchedEntityDefender.applyChanges();
		locationDefender = watchedEntityDefender.get(Position.class).getLocation();


		this.app.getInputManager().addMapping(DEFENDER_MOVE_LEFT, new KeyTrigger(KeyInput.KEY_A));
		this.app.getInputManager().addMapping(DEFENDER_MOVE_RIGHT, new KeyTrigger(KeyInput.KEY_D));
		this.app.getInputManager().addMapping(DEFENDER_MOVE_UP, new KeyTrigger(KeyInput.KEY_W));
		this.app.getInputManager().addMapping(DEFENDER_MOVE_DOWN, new KeyTrigger(KeyInput.KEY_S));

		this.app.getInputManager().addMapping(CURSOR_MOVE_RIGHT, new KeyTrigger(KeyInput.KEY_RIGHT));
		this.app.getInputManager().addMapping(CURSOR_MOVE_LEFT, new KeyTrigger(KeyInput.KEY_LEFT));
		this.app.getInputManager().addMapping(CURSOR_MOVE_UP, new KeyTrigger(KeyInput.KEY_UP));
		this.app.getInputManager().addMapping(CURSOR_MOVE_DOWN, new KeyTrigger(KeyInput.KEY_DOWN));


		this.app.getInputManager().addListener(analogListener
				, DEFENDER_MOVE_LEFT
				, DEFENDER_MOVE_RIGHT
				, DEFENDER_MOVE_UP
				, DEFENDER_MOVE_DOWN
				, CURSOR_MOVE_RIGHT
				, CURSOR_MOVE_LEFT
				, CURSOR_MOVE_UP
				, CURSOR_MOVE_DOWN
		);

		this.app.getInputManager().addMapping(SHOOT,
				new KeyTrigger(KeyInput.KEY_SPACE));

		this.app.getInputManager().addMapping(MOUSE_LEFT_BUTTON_CLICK,
				new MouseButtonTrigger(MouseInput.BUTTON_LEFT));

		this.app.getInputManager().addListener(actionListener, SHOOT, MOUSE_LEFT_BUTTON_CLICK);
	}

	@Override
	public void update(float tpf) {
	}

	@Override
	public void cleanup() {
	}
}