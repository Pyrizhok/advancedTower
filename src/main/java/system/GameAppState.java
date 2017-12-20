package system;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.BaseAppState;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jvpichowski.jme3.es.bullet.components.BoxShape;
import com.jvpichowski.jme3.es.bullet.components.Friction;
import com.jvpichowski.jme3.es.bullet.components.RigidBody;
import com.jvpichowski.jme3.es.bullet.components.WarpPosition;
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
		initFloor();

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
//				new Attack(1),
				new Defense(3),
				new CollisionShape(1),
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

	public void initFloor() {
		AssetManager assetManager = this.app.getAssetManager();
		Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		mat.setColor("Color", ColorRGBA.Brown);
		EntityId floorEntity = ed.createEntity();
		ed.setComponents(floorEntity,
				new WarpPosition(new Vector3f(0, -0.1f, 0), Quaternion.DIRECTION_Z.clone()),
				new RigidBody(false, 0),
				new Friction(0.6f),
				new BoxShape(new Vector3f(10f, 0.1f, 5f)),
				new BoxComponent(new Vector3f(10f, 0.1f, 5f)),
				new MaterialComponent(mat));
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