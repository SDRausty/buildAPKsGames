package ru.o2genum.coregame.game;

public class Dot
{
	public enum Type {Enemy, Health, 
		Shield, // Protects the core 
		}

	public Type type;
	public VectorF speed;
	public float energy; // Max 1.0F
	public VectorF coords;
	public static float maxRadius;
}
