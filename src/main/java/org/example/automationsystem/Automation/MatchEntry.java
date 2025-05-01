package org.example.automationsystem.Automation;


import java.util.Objects;

public class MatchEntry {
    private final String match;
    private final int index;

    public MatchEntry(String match, int index) {
        this.match = match;
        this.index = index;
    }

    public String getMatch() {
        return match;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public String toString() {
        return String.format("\"%s\" at index %d", match, index);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MatchEntry)) return false;
        MatchEntry that = (MatchEntry) o;
        return index == that.index && match.equals(that.match);
    }

    @Override
    public int hashCode() {
        return Objects.hash(match, index);
    }
}
