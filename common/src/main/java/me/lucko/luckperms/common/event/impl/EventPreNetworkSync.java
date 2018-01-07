/*
 * This file is part of LuckPerms, licensed under the MIT License.
 *
 *  Copyright (c) lucko (Luck) <luck@lucko.me>
 *  Copyright (c) contributors
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */

package me.lucko.luckperms.common.event.impl;

import me.lucko.luckperms.api.event.sync.PreNetworkSyncEvent;
import me.lucko.luckperms.common.event.AbstractEvent;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.annotation.Nonnull;

public class EventPreNetworkSync extends AbstractEvent implements PreNetworkSyncEvent {

    private final AtomicBoolean cancellationState;
    private final UUID syncId;

    public EventPreNetworkSync(AtomicBoolean cancellationState, UUID syncId) {
        this.cancellationState = cancellationState;
        this.syncId = syncId;
    }

    @Nonnull
    @Override
    public AtomicBoolean getCancellationState() {
        return this.cancellationState;
    }

    @Nonnull
    @Override
    public UUID getSyncId() {
        return this.syncId;
    }

    @Override
    public String toString() {
        return "EventPreNetworkSync(cancellationState=" + this.getCancellationState() + ", syncId=" + this.getSyncId() + ")";
    }
}
