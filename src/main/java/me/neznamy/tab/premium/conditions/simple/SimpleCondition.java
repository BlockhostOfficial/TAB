package me.neznamy.tab.premium.conditions.simple;

import me.neznamy.tab.shared.ITabPlayer;

public abstract class SimpleCondition {
	
	public abstract boolean isMet(ITabPlayer p);
	
	public static SimpleCondition compile(String line) {
		SimpleCondition c = null;
		
		//no idea how to make this more efficient
		if ((c = PermissionCondition.compile(line)) != null) return c;
		if ((c = NotEqualsCondition.compile(line)) != null) return c;
		if ((c = EqualsCondition.compile(line)) != null) return c;
		if ((c = MoreThanOrEqualsCondition.compile(line)) != null) return c;
		if ((c = MoreThanCondition.compile(line)) != null) return c;
		if ((c = LessThanOrEqualsCondition.compile(line)) != null) return c;
		if ((c = LessThanCondition.compile(line)) != null) return c;

		return c;
	}
}