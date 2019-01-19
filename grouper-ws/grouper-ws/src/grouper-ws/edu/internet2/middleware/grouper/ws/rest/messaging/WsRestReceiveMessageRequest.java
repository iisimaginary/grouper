/*******************************************************************************
 * Copyright 2016 Internet2
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
/**
 * @author vsachdeva
 * $Id$
 */
package edu.internet2.middleware.grouper.ws.rest.messaging;

import edu.internet2.middleware.grouper.ws.coresoap.WsParam;
import edu.internet2.middleware.grouper.ws.coresoap.WsSubjectLookup;
import edu.internet2.middleware.grouper.ws.rest.WsRequestBean;
import edu.internet2.middleware.grouper.ws.rest.method.GrouperRestHttpMethod;


/**
 * request bean in body of rest request
 */
public class WsRestReceiveMessageRequest implements WsRequestBean {
  
  
  /**
   * @see edu.internet2.middleware.grouper.ws.rest.WsRequestBean#retrieveRestHttpMethod()
   */
  @Override
  public GrouperRestHttpMethod retrieveRestHttpMethod() {
    return GrouperRestHttpMethod.GET;
  }
  
  /** queue or topic name **/
  private String queueOrTopicName;
  
  /** messaging system name **/
  private String messageSystemName;
  
  /** routing key - valid for rabbitmq only **/
  private String routingKey;
  
  /** exchange type - valid for rabbitmq only **/
  private String exchangeType;
  
  /** the millis to block waiting for messages, max of 20000 (optional) **/
  private String blockMillis;
  
  /** max number of messages to receive at once, though can't be more than the server maximum (optional) **/
  private String maxMessagesToReceiveAtOnce;
  
  /** create queue/topic if doesn't exist already. **/
  private String autocreateObjects;
  
  /** 
   * @return queueOrTopicName
   */
  public String getQueueOrTopicName() {
    return this.queueOrTopicName;
  }

  /**
   * @param queueOrTopicName1
   */
  public void setQueueOrTopicName(String queueOrTopicName1) {
    this.queueOrTopicName = queueOrTopicName1;
  }
  
  /**
   * @return routingKey
   */
  public String getRoutingKey() {
    return this.routingKey;
  }

  /**
   * @param routingKey1
   */
  public void setRoutingKey(String routingKey1) {
    this.routingKey = routingKey1;
  }
  
  /**
   * @return exchangeType
   */
  public String getExchangeType() {
    return this.exchangeType;
  }

  /**
   * @param exchangeType1
   */
  public void setExchangeType(String exchangeType1) {
    this.exchangeType = exchangeType1;
  }

  /**
   * @return messageSystemName
   */
  public String getMessageSystemName() {
    return this.messageSystemName;
  }

  /**
   * @param messageSystemName1
   */
  public void setMessageSystemName(String messageSystemName1) {
    this.messageSystemName = messageSystemName1;
  }

  /**
   * @return the millis to block waiting for messages, max of 20000 (optional)
   */
  public String getBlockMillis() {
    return this.blockMillis;
  }

  /**
   * @param blockMillis1 - the millis to block waiting for messages, max of 20000 (optional)
   */
  public void setBlockMillis(String blockMillis1) {
    this.blockMillis = blockMillis1;
  }

  /**
   * @return max number of messages to receive at once, though can't be more than the server maximum (optional)
   */
  public String getMaxMessagesToReceiveAtOnce() {
    return this.maxMessagesToReceiveAtOnce;
  }

  /**
   * @param maxMessagesToReceiveAtOnce1 - max number of messages to receive at once, though can't be more than the server maximum (optional)
   */
  public void setMaxMessagesToReceiveAtOnce(String maxMessagesToReceiveAtOnce1) {
    this.maxMessagesToReceiveAtOnce = maxMessagesToReceiveAtOnce1;
  }
  
  /**
   * create queue/topic if doesn't exist already.
   * @return autocreateObjects
   */
  public String isAutocreateObjects() {
    return this.autocreateObjects;
  }

  /**
   * create queue/topic if doesn't exist already.
   * @param autocreateObjects1
   */
  public void setAutocreateObjects(String autocreateObjects1) {
    this.autocreateObjects = autocreateObjects1;
  }

  /** is the version of the client.  Must be in GrouperWsVersion, e.g. v1_3_000 */
  private String clientVersion;
  
  /**
   * is the version of the client.  Must be in GrouperWsVersion, e.g. v1_3_000
   * @return version
   */
  public String getClientVersion() {
    return this.clientVersion;
  }

  /**
   * is the version of the client.  Must be in GrouperWsVersion, e.g. v1_3_000
   * @param clientVersion1
   */
  public void setClientVersion(String clientVersion1) {
    this.clientVersion = clientVersion1;
  }

  /** if acting as someone else */
  private WsSubjectLookup actAsSubjectLookup;
  
  /**
   * if acting as someone else
   * @return act as subject
   */
  public WsSubjectLookup getActAsSubjectLookup() {
    return this.actAsSubjectLookup;
  }

  /**
   * if acting as someone else
   * @param actAsSubjectLookup1
   */
  public void setActAsSubjectLookup(WsSubjectLookup actAsSubjectLookup1) {
    this.actAsSubjectLookup = actAsSubjectLookup1;
  }

  /** optional: reserved for future use */
  private  WsParam[] params;

  /**
   * optional: reserved for future use
   * @return params
   */
  public WsParam[] getParams() {
    return this.params;
  }

  /**
   * optional: reserved for future use
   * @param params1
   */
  public void setParams(WsParam[] params1) {
    this.params = params1;
  }

}
