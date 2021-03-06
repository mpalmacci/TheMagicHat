package com.themagichat.players;

import com.themagichat.decks.Deck;

import java.util.List;

public class Player implements Comparable<Player> {

	private int id;
	private String name;
	private int dci;
	private boolean active;
	private boolean self;
	private List<Deck> deckList;

	public Player() {
		this.id = 0;
		this.name = "";
		this.dci = 0;
		this.self = false;
		this.active = false;
	}

	public Player(String name) {
		this();
		this.name = name;
	}

	public Player(String name, boolean active) {
		this(name);
		this.active = active;
	}

	public Player(int id, String name, boolean active) {
		this(name, active);
		this.id = id;
	}

	public Player(int id, String name, boolean active, boolean self) {
		this(name, active);
		this.id = id;
		this.self = self;
	}

	public Player(String name, int dci, boolean active, boolean self) {
		this(name, active);
		this.dci = dci;
		this.self = self;
	}

	public Player(int id, String name, int dci, boolean active, boolean self) {
		this(name, dci, active, self);
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public void setDci(int dci) {
		this.dci = dci;
	}

	public int getDci() {
		return this.dci;
	}

	public void setSelf(boolean self) {
		this.self = self;
	}

	public boolean isSelf() {
		return this.self;
	}

	public void setDeckList(List<Deck> deckList) {
		this.deckList = deckList;
	}

	public List<Deck> getDeckList() {
		return this.deckList;
	}

	@Override
	public String toString() {
		if (this.name.isEmpty()) {
			return "";
		}

		if (!this.active) {
			return this.getName() + " (inactive)";
		}

		return this.getName();
	}

	@Override
	public boolean equals(Object o) {
		Player p = (Player) o;

		return this.getName().equalsIgnoreCase(p.getName());
	}

	@Override
	public int compareTo(Player another) {
		if (this.name.isEmpty() && another.getName().isEmpty()) {
			return 0;
		} else if (this.name.isEmpty()) {
			return -1;
		} else if (another.getName().isEmpty()) {
			return 1;
		}
		return name.compareTo(another.getName());
	}
}
