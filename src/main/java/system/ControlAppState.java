package system;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.*;
import com.jme3.math.Vector3f;
import com.simsilica.es.*;
import component.*;

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
	private static final Integer BULLET_ATTACK_POWER = 1;
	private static final Integer BULLET_COLLISION_SHAPE = 1;
	private static final Integer BULLET_SPEED = 20;
	private static final Integer BULLET_DECAY_DELTA_MILLIS = 20000;


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
		if (name.equals(SHOOT) && !isPressed) {
			watchedEntityDefender.applyChanges();
			watchedEntityCursor.applyChanges();
			EntityId bullet = ed.createEntity();
			Vector3f directionVector = locationCursor.subtract(locationDefender).normalize();
			ed.setComponents(bullet,
					new Model(Model.BULLET),
					new Attack(BULLET_ATTACK_POWER),
					new CollisionShape(BULLET_COLLISION_SHAPE),
					new Position(new Vector3f(locationDefender.getX(), locationDefender.getY(), locationDefender.getZ()), directionVector),
					new Direction(directionVector),
					new Speed(BULLET_SPEED),
					new Decay(BULLET_DECAY_DELTA_MILLIS));

		}
	};

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
				new KeyTrigger(KeyInput.KEY_SPACE),
				new MouseButtonTrigger(MouseInput.BUTTON_LEFT));

		this.app.getInputManager().addListener(actionListener, SHOOT);
	}

	@Override
	public void update(float tpf) {
	}

	@Override
	public void cleanup() {
	}
}