/*
 * Created on Jan 13, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package edu.internet2.middleware.signet;

import java.util.Date;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import edu.internet2.middleware.signet.choice.ChoiceSet;

/**
 * @author acohen
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
final class LimitImpl implements Limit
{
  private Signet						signet;
  private Subsystem					subsystem;
  private String						subsystemId;
  private String						id;
  private ValueType					valueType;
  private ChoiceSet					choiceSet;
  private String						choiceSetId;
  private String						name;
  private String						helpText;
  private Date							modifyDatetime;
  private Status						status;

  /**
   * Hibernate requires that each persistable entity have a default
   * constructor.
   */
  public LimitImpl()
  {
      super();
  }

  
  LimitImpl
    (Signet							signet,
     Subsystem					subsystem,
     String 						id,
     ValueType					valueType,
     ChoiceSet					choiceSet,
     HTMLLimitRenderer	htmlRenderer,
     String 						name,
     String 						helpText,
     Status							status)
  {
    super();
    this.setSignet(signet);
    this.subsystem = subsystem;
    this.subsystemId = this.subsystem.getId();
    this.id = id;
    this.valueType = valueType;
    this.choiceSet = choiceSet;
    this.choiceSetId = this.choiceSet.getId();
    this.name = name;
    this.helpText = helpText;
    this.status = status;
  }
  
  void setSignet(Signet signet)
  {
    this.signet = signet;
  }
  
  Signet getSignet()
  {
    return this.signet;
  }
  
  /* (non-Javadoc)
   * @see edu.internet2.middleware.signet.Limit#getLimitId()
   */
  public String getId()
  {
    return this.id;
  }
  
  void setId(String id)
  {
    this.id = id;
  }

  /* (non-Javadoc)
   * @see edu.internet2.middleware.signet.Limit#getName()
   */
  public String getName()
  {
    return this.name;
  }
  
  void setName(String name)
  {
    this.name = name;
  }

  /* (non-Javadoc)
   * @see edu.internet2.middleware.signet.Limit#getHelpText()
   */
  public String getHelpText()
  {
    return this.helpText;
  }
  
  void setHelpText(String helpText)
  {
    this.helpText = helpText;
  }
  
  /* (non-Javadoc)
   * @see edu.internet2.middleware.signet.Limit#getHTMLselections(javax.servlet.http.HttpServletRequest)
   */
  public Set getHTMLselections(HttpServletRequest request)
  {
    // TODO Auto-generated method stub
    return null;
  }

  /* (non-Javadoc)
   * @see edu.internet2.middleware.signet.SubsystemPart#getSubsystem()
   */
  public Subsystem getSubsystem()
  throws ObjectNotFoundException
  {
    if ((this.subsystem == null)
        && (this.subsystemId != null)
        && (this.getSignet() != null))
    {
      this.subsystem = this.getSignet().getSubsystem(this.subsystemId);
    }
    
    return this.subsystem;
  }
  
  String getSubsystemId()
  {
    return this.subsystemId;
  }

  /* (non-Javadoc)
   * @see edu.internet2.middleware.signet.SubsystemPart#setSubsystem(edu.internet2.middleware.signet.Subsystem)
   */
  public void setSubsystem(Subsystem subsystem)
  {
      this.subsystem = subsystem;
      this.subsystemId = subsystem.getId();
  }
  
  void setSubsystemId(String subsystemId)
  throws ObjectNotFoundException
  {
    this.subsystemId = subsystemId;
    
    if (this.getSignet() != null)
    {
      this.subsystem = this.getSignet().getSubsystem(subsystemId);
    }
  }

  public ChoiceSet getChoiceSet()
  throws ObjectNotFoundException
  {
    if ((this.choiceSet == null)
        && (this.choiceSetId != null)
        && (this.getSignet() != null))
    {
      this.choiceSet
      	= this.getSignet()
      			.getSubsystem(this.subsystemId)
      				.getChoiceSet(this.choiceSetId);
    }
    
    return this.choiceSet;
  }
  
  String getChoiceSetId()
  {
    return this.choiceSetId;
  }

  public void setChoiceSet(ChoiceSet choiceSet)
  {
      this.choiceSet = choiceSet;
      this.choiceSetId = choiceSet.getId();
  }
  
  void setChoiceSetId(String choiceSetId)
  throws ObjectNotFoundException
  {
    this.choiceSetId = choiceSetId;
    
    if (this.getSignet() != null)
    {
      this.choiceSet
      	= this.getSignet()
      			.getSubsystem(this.subsystemId)
      				.getChoiceSet(choiceSetId);
    }
  }


  /* This method exists only for use by Hibernate.
   */
  public LimitFullyQualifiedId getFullyQualifiedId()
  {
    return new LimitFullyQualifiedId
    	(this.getSubsystemId(), this.getId());
  }
  
  /*
   * This method exists only for use by Hibernate.
   */
  void setFullyQualifiedId(LimitFullyQualifiedId lfqId)
  throws ObjectNotFoundException
  {
    this.subsystemId = lfqId.getSubsystemId();
    this.setId(lfqId.getLimitId());
    
    if (this.getSignet() != null)
    {
      this.subsystem
      	= this.getSignet().getSubsystem(lfqId.getSubsystemId());
    }
  }
  
  void setDataType(String dataType)
  {
    // This method is not yet implemented, and exists only to
    // satisfy Hibernate's mapping to the not-yet-used (but not-null)
    // column in the database.
  }
  
  String getDataType()
  {
    // This method is not yet implemented, and exists only to
    // satisfy Hibernate's mapping to the not-yet-used (but not-null)
    // column in the database.
    return "reserved";
  }
  
  /**
   * @return Returns the date and time this entity was last modified.
   */
  final Date getModifyDatetime()
  {
    return this.modifyDatetime;
  }
  
  /**
   * @param modifyDatetime The modifyDatetime to set.
   */
  final void setModifyDatetime(Date modifyDatetime)
  {
    this.modifyDatetime = modifyDatetime;
  }
  
  /**
   * @return Returns the status.
   */
  public final Status getStatus()
  {
    return status;
  }
  
  /**
   * @param status The status to set.
   */
  public final void setStatus(Status status)
  {
    this.status = status;
  }
  
  void setShape(LimitShape shape)
  {
    // This method is not yet implemented, and exists only to
    // satisfy Hibernate's mapping to the not-yet-used (but not-null)
    // column in the database.
  }
  
  LimitShape getShape()
  {
    // This method is not yet implemented, and exists only to
    // satisfy Hibernate's mapping to the not-yet-used (but not-null)
    // column in the database.
    return LimitShape.CHOICE_SET;
  }
  
  /**
   * @return Returns the valueType.
   */
  ValueType getValueType()
  {
    return this.valueType;
  }
  
  /**
   * @param valueType The valueType to set.
   */
  void setValueType(ValueType valueType)
  {
    this.valueType = valueType;
  }
}
