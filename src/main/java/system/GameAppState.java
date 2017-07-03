package system;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.BaseAppState;
import com.jme3.math.Vector3f;
import com.simsilica.es.EntityData;
import com.simsilica.es.EntityId;
import component.*;

import java.util.Random;

public class GameAppState extends BaseAppState {

	private static final Integer BOUND = 50;
	private static final Integer DEFENCE_INVADER = 2;
	private static final Integer MAX_NUMBER_OF_INVADERS = 100;
	private static final Integer halfOfBound = BOUND / 2;
	private EntityData ed;
	private SimpleApplication app;

	@Override
	protected void initialize(Application app) {
		this.app = (SimpleApplication) app;
		this.app.setPauseOnLostFocus(true);
		this.app.setDisplayStatView(false);

		this.ed = this.app.getStateManager().getState(EntityDataState.class).getEntityData();

		defineDefender();
		defineInvaders();
		defineArea();
		defineCursor();

	}

	private void defineArea() {
		EntityId gameArea = ed.createEntity();

		this.ed.setComponents(gameArea,
				new Position(new Vector3f(0, 0, 0), new Vector3f()),
				new Model(Model.FIELD));
	}

	private void defineDefender() {
		EntityId defender = ed.createEntity();
		getState(ControlAppState.class).setWatchedEntityDefender(ed.watchEntity(defender, Position.class));

		this.ed.setComponents(defender,
				new Attack(1),
				new Position(new Vector3f(0, -20, 0), new Vector3f()),
				new Model(Model.DEFENDER));
	}

	private void defineInvaders() {
		for (int x = -20; x < 20; x += 4) {
			for (int y = 0; y < 20; y += 4) {
				EntityId invader = ed.createEntity();
				this.ed.setComponents(invader,
						new Defense(2),
						new CollisionShape(1f),
						new Position(new Vector3f(x, y, 0), new Vector3f()),
						new Model(Model.INVADER));
			}
		}
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

		int numberOfInvaders = getState(InvadersAIAppState.class).getNumberOfInvaders();

		if (numberOfInvaders < MAX_NUMBER_OF_INVADERS) {

			EntityId invader = ed.createEntity();
			Random rand = new Random();
			int value = rand.nextInt(BOUND) - halfOfBound;
			int value2 = rand.nextInt(BOUND) - halfOfBound;
			this.ed.setComponents(invader,
					new Defense(DEFENCE_INVADER),
					new CollisionShape(1f),
					new Position(new Vector3f(value, value2, 0), new Vector3f()),
					new Model(Model.INVADER));
		}
	}

}