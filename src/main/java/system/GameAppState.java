package system;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.BaseAppState;
import com.jme3.math.Vector3f;
import com.simsilica.es.EntityData;
import com.simsilica.es.EntityId;
import component.*;

public class GameAppState extends BaseAppState {

	public static final Integer BOUND = 50;
	public static final Integer DEFENCE_INVADER = 2;
	public static final Integer MAX_NUMBER_OF_INVADERS = 10;
	private EntityData ed;
	private SimpleApplication app;

	@Override
	protected void initialize(Application app) {
		this.app = (SimpleApplication) app;
		this.app.setPauseOnLostFocus(true);
		this.app.setDisplayStatView(false);

		this.ed = this.app.getStateManager().getState(EntityDataState.class).getEntityData();

		defineArea();
		defineDefender();
		defineCursor();

	}

	private void defineArea() {
		EntityId gameArea = ed.createEntity();
		this.ed.setComponents(gameArea,
				new Position(new Vector3f(0, 0, 0), new Vector3f()),
				new Model(Model.AREA));
	}

	private void defineDefender() {
		EntityId defender = ed.createEntity();
		getState(ControlAppState.class).setWatchedEntityDefender(ed.watchEntity(defender, Position.class));

		this.ed.setComponents(defender,
				new Attack(1),
				new Position(new Vector3f(0, -20, 0), new Vector3f()),
				new Model(Model.DEFENDER));
	}

	private void defineCursor() {
		EntityId cursor = ed.createEntity();
		this.ed.setComponents(cursor,
				new Position(new Vector3f(), new Vector3f()),
				new Model(Model.CURSOR));
		getState(ControlAppState.class).setWatchedEntityCursor(ed.watchEntity(cursor, Position.class));
	}

	@Override
	protected void cleanup(Application app) {

	}

	@Override
	protected void onEnable() {

	}

	@Override
	protected void onDisable() {

	}

	@Override
	public void update(float tpf) {
	}

}