package ru.o2genum.coregame.game;

public class Core
{
	public float health; // Max 1.0F
	public float shieldEnergy; // Max 1.0F
	public float angle;
	public VectorF coords;
	public float maxRadius;
	public float shieldRadius;
	public static final float SHIELD_WIDTH = 4.0F;
	public static final float GAP_ANGLE = 130.0F;
	// Core's and core shield's sizes are managed by World
}

