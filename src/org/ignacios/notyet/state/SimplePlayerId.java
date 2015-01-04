package org.ignacios.notyet.state;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import static com.google.common.base.Preconditions.checkNotNull;

public class SimplePlayerId implements PlayerId {
    private final String playerId;

    public SimplePlayerId(String playerId) {
        this.playerId = checkNotNull(playerId);
    }

    @Override
    public String getPlayerId() {
        return playerId;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("playerId", playerId)
                .toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        final SimplePlayerId other = (SimplePlayerId) obj;
        return Objects.equal(this.playerId, other.playerId);
    }
}
