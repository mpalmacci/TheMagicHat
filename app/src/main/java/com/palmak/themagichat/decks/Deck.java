package com.palmak.themagichat.decks;

import com.palmak.themagichat.cards.Card;
import com.palmak.themagichat.players.Player;

import java.util.List;

/**
 * Created by Matthew on 12/3/2014.
 */
public class Deck implements Comparable<Deck> {

    private Long id;
    private String name;
    private boolean active;
    private Player owner;
    private List<Card> cardList;

    public Deck() {
        this(Long.valueOf(0), null, null, false);
    }

    public Deck(String name, Player owner, boolean active) {
        this(Long.valueOf(0), name, owner, active);
    }

    public Deck(Long id, String name, Player owner, boolean active) {
        this(id, name, owner, active, null);
    }

    public Deck(Long id, String name, Player owner, boolean active,
                List<Card> cardList) {
        this.setId(id);
        // this(name, owner);
        this.setName(name);
        this.setOwner(owner);
        this.setActive(active);
        this.setCardList(cardList);
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        if (id != null) this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        if (name != null) this.name = name;
    }

    public Player getOwner() {
        return this.owner;
    }

    public void setOwner(Player owner) {
        if (owner != null) this.owner = owner;
    }

    public boolean isActive() {
        return this.active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<Card> getCardList() {
        return this.cardList;
    }

    public void setCardList(List<Card> cardList) {
        this.cardList = cardList;
    }

    @Override
    public boolean equals(Object o) {
        Deck d = (Deck) o;

        // So long as the deck ids are equal and the id isn't zero, then they
        // are equal
        if (this.getId() == d.getId() && this.getId() != 0) {
            return true;
        } else {
            return this.getName().equalsIgnoreCase(d.getName())
                    && this.getOwner().getName()
                    .equalsIgnoreCase(d.getOwner().getName());
        }
    }

    public boolean equalsOwner(Deck d) {
        // This is case insensitive
        return this.getOwner().getName()
                .equalsIgnoreCase(d.getOwner().getName());
    }

    @Override
    public String toString() {
        if (getName().isEmpty()) {
            // This is here for the UpdateDeck
            return new String();
        }

        StringBuilder sb = new StringBuilder();

        if (this.owner.getName().isEmpty()) {
            sb.append("Unowned ");
        } else {
            sb.append(this.owner.getName());
            sb.append("'s ");
        }

        sb.append(this.name);
        sb.append(" Deck");

        // TODO Remove " (inactive)" from here?
        if (!this.isActive()) {
            sb.append(" (inactive)");
        }

        return sb.toString();
    }

    @Override
    public int compareTo(Deck d) {
        int ownerCmp = this.owner.compareTo(d.getOwner());
        return (ownerCmp != 0 ? ownerCmp : name.compareTo(d.getName()));
    }
}
