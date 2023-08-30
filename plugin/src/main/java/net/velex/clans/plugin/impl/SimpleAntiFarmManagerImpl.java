package net.velex.clans.plugin.impl;

import net.velex.clans.api.model.AntiFarmManagerModel;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SimpleAntiFarmManagerImpl implements AntiFarmManagerModel {
	@Override
	public void start() {
	
	}
	
	@Override
	public void stop() {
	
	}
	
	@Override
	public boolean isFarming(final @NotNull Player alpha, final @NotNull Player bravoTarget) {
		return false;
	}
}
