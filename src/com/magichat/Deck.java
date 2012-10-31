package com.magichat;

public class Deck implements Comparable<Deck> {

	private String name;

	private boolean active;

	private Player owner;

	private int id;

	public Deck() {
	}

	public Deck(String name, Player owner) {
		this.name = name;
		this.owner = owner;
	}
	
	public Deck(String name, Player owner, boolean active) {
		this(name, owner);
		this.active = active;
	}

	public Deck(String name, Player owner, boolean active, int id) {
		this(name, owner, active);
		this.id = id;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public Player getOwner() {
		return owner;
	}

	public void setOwner(Player owner) {
		this.owner = owner;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public boolean equals(Object o) {
		Deck d = (Deck) o;

		/*
		 * TODO Resolve this uniqueness if (this.getId() == d.getId()) { return
		 * true; } else {
		 */
		return this.getName().equalsIgnoreCase(d.getName())
				&& this.getOwner().getName()
						.equalsIgnoreCase(d.getOwner().getName());
		// }
	}

	public boolean equalsOwner(Deck d) {
		// This is case insensitive
		return this.getOwner().getName()
				.equalsIgnoreCase(d.getOwner().getName());
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append(getOwner());
		sb.append("'s ");
		sb.append(getName());
		sb.append(" Deck");

		// TODO Remove " (inactive)" from here?
		if (!this.isActive()) {
			sb.append(" (inactive)");
		}

		return sb.toString();
	}

	@Override
	public int compareTo(Deck d) {
		int ownerCmp = owner.compareTo(d.getOwner());
		return (ownerCmp != 0 ? ownerCmp : name.compareTo(d.getName()));
	}
}