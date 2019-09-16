package com.iluwatar.leaderelection.bully;

import com.iluwatar.leaderelection.AbstractInstance;
import com.iluwatar.leaderelection.Message;
import com.iluwatar.leaderelection.MessageManager;

/**
 * The MIT License
 * Copyright (c) 2014-2016 Ilkka Seppälä
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

/**
 * Impelemetation with bully algorithm. Each instance should have a sequential id and is able to
 * communicate with other instances in the system. Initially the instance with smallest (or largest)
 * ID is selected to be the leader. All the other instances send heartbeat message to leader periodically
 * to check its health. If one certain instance finds the server done, it will send an election message
 * to all the instances of which the ID is larger. If the target instance is alive, it will return an
 * alive message (in this sample return true) and then send election message with its ID. If not,
 * the original instance will send leader message to all the other instances.
  */
public class BullyInstance extends AbstractInstance {

  /**
   * Constructor of BullyInstance.
   */
  public BullyInstance(MessageManager messageManager, int localId, int leaderId) {
    super(messageManager, localId, leaderId);
  }

  @Override
  protected void handleElectionMessage(Message message) {}

  @Override
  protected void handleHeartbeatInvokeMessage() {
    boolean isLeaderAlive = messageManager.sendHeartbeatMessage(leaderId);
    if (isLeaderAlive) {
      System.out.println("Instance " + localId + "- Leader is alive.");
      messageManager.sendHeartbeatInvokeMessage(localId);
    } else {
      System.out.println("Instance " + localId + "- Leader is not alive. Start election.");
      boolean electionResult = messageManager.sendElectionMessage(localId, String.valueOf(localId));
      if (electionResult) {
        System.out.println("Instance " + localId + "- Succeed in election. Start leader notification.");
        messageManager.sendLeaderMessage(localId, localId);
      }
    }
  }

  @Override
  protected void handleElectionInvokeMessage() {
    System.out.println("Instance " + localId + "- Start election.");
    boolean electionResult = messageManager.sendElectionMessage(localId, String.valueOf(localId));
    if (electionResult) {
      System.out.println("Instance " + localId + "- Succeed in election. Start leader notification.");
      messageManager.sendLeaderMessage(localId, localId);
    }
  }

  @Override
  protected void handleLeaderMessage(Message message) {
    leaderId = Integer.valueOf(message.getContent());
    System.out.println("Instance " + localId + " - Leader update done.");
  }

  @Override
  protected void handleLeaderInvokeMessage() {

  }

  @Override
  protected void handleHeartbeatMessage(Message message) {

  }

}
