package com.gadarts.cactiwars.systems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.ai.msg.MessageDispatcher;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.utils.Disposable;
import com.gadarts.cactiwars.assets.GameAssetManager;

import java.util.List;

public abstract class GameEntitySystem extends EntitySystem implements Disposable, Telegraph {
	protected final MessageDispatcher dispatcher;
	protected SystemsGlobalData systemsGlobalData;

	public GameEntitySystem( ) {
		this.dispatcher = new MessageDispatcher();
	}

	public abstract List<SystemEvents> getEventsListenList( );

	public void createGlobalData(SystemsGlobalData systemsGlobalData) {
		this.systemsGlobalData = systemsGlobalData;
	}

	@Override
	public boolean handleMessage(Telegram msg) {
		return false;
	}

	public abstract void onGlobalDataReady(GameAssetManager assetsManager);

	public void addListener(GameEntitySystem listener) {
		if (listener.getEventsListenList() == null) return;

		listener.getEventsListenList().forEach(event -> dispatcher.addListener(listener, event.ordinal()));
	}
}
