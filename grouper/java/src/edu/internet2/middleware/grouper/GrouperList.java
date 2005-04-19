/*
 * Copyright (C) 2004-2005 University Corporation for Advanced Internet Development, Inc.
 * Copyright (C) 2004-2005 The University Of Chicago
 * All Rights Reserved. 
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 *  * Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *  * Neither the name of the University of Chicago nor the names
 *    of its contributors nor the University Corporation for Advanced
 *   Internet Development, Inc. may be used to endorse or promote
 *   products derived from this software without explicit prior
 *   written permission.
 *
 * You are under no obligation whatsoever to provide any enhancements
 * to the University of Chicago, its contributors, or the University
 * Corporation for Advanced Internet Development, Inc.  If you choose
 * to provide your enhancements, or if you choose to otherwise publish
 * or distribute your enhancements, in source code form without
 * contemporaneously requiring end users to enter into a separate
 * written license agreement for such enhancements, then you thereby
 * grant the University of Chicago, its contributors, and the University
 * Corporation for Advanced Internet Development, Inc. a non-exclusive,
 * royalty-free, perpetual license to install, use, modify, prepare
 * derivative works, incorporate into the software or other computer
 * software, distribute, and sublicense your enhancements or derivative
 * works thereof, in binary and source code form.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND WITH ALL FAULTS.  ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 * BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE, AND NON-INFRINGEMENT ARE DISCLAIMED AND the
 * entire risk of satisfactory quality, performance, accuracy, and effort
 * is with LICENSEE. IN NO EVENT SHALL THE COPYRIGHT OWNER, CONTRIBUTORS,
 * OR THE UNIVERSITY CORPORATION FOR ADVANCED INTERNET DEVELOPMENT, INC.
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR
 * BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OR DISTRIBUTION OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package edu.internet2.middleware.grouper;


import  edu.internet2.middleware.grouper.*;
import  java.io.Serializable;
import  java.util.*;
import  net.sf.hibernate.*;
import  org.apache.commons.lang.builder.EqualsBuilder;
import  org.apache.commons.lang.builder.HashCodeBuilder;
import  org.apache.commons.lang.builder.ToStringBuilder;


/** 
 * Class modeling a {@link Grouper} list value.
 * <p />
 *
 * @author  blair christensen.
 * @version $Id: GrouperList.java,v 1.58 2005-04-19 18:30:49 blair Exp $
 */
public class GrouperList implements Serializable {

  /*
   * PRIVATE INSTANCE VARIABLES
   */

  // Transient
  private transient List            elements = new ArrayList();
  private transient Group           g;
  private transient GrouperMember   m;
  private transient Group           via;

  // And persistent
  private String chainKey;
  private String groupField;
  private String groupKey;
  private String listKey;
  private String memberKey;
  private String viaKey;


  /*
   * CONSTRUCTORS
   */

  /**
   * Null-argument constructor for Hibernate.
   */
  public GrouperList() {
    // Nothing
  }

  /*
   * Create a new {@link GrouperList} object.
   * TODO Refactor
   */
  protected GrouperList(Group g, GrouperMember m, String list) {
    // TODO See if it exists?
    if (this.getListKey() == null) {
      this.setListKey( new GrouperUUID().toString() );
    }
    if (g == null) {
      throw new RuntimeException("GrouperList: null group");
    }
    if (m == null) {
      throw new RuntimeException("GrouperList: null member");
    }
    if (list == null) {
      throw new RuntimeException("GrouperList: null list");
    }
    this.g          = g;
    this.groupKey   = g.key();
    this.m          = m;
    this.memberKey  = m.key();
    this.groupField = list;
    this.via        = null;
    GrouperList.validate(this);
  }

  /*
   * Create a new {@link GrouperList} object.
   */
  protected GrouperList(
              GrouperSession s, Group g, GrouperMember m, String list
            ) 
  {
    this(g, m, list);
    this.load(s);
  }

  /*
   * Create a new {@link GrouperList} object for an effective mship.
   */
  protected GrouperList(
              GrouperSession s, Group g, GrouperMember m, 
              String list, List chain
            ) 
  {
    this(g, m, list);
    // TODO Is this right?
    if (chain.size() < 1) {
      throw new RuntimeException(
                  "Cannot create eff mship with empty chain"
                );
    }
    if (chain.size() > 0) {
      MemberVia   mv = (MemberVia) chain.get(0);
      GrouperList gl = (GrouperList) mv.toList(s);
      // TODO This does no good in a constructor.  Make this method a
      // static creator method?
      if (!gl.member().typeID().equals("group")) {
        throw new RuntimeException(
          "An effective membership must only contain groups as members"
        );
      }
      this.elements = chain;
      this.via      = gl.member().toGroup();
      this.viaKey   = gl.member().toGroup().key();
    }
    GrouperList.validate(this);
  }


  /*
   * PROTECTED CLASS METHODS
   */

  /*
   * @return true if list value exists
   */
  protected static boolean exists(GrouperSession s, GrouperList gl) {
    Query   q;
    String  qry;
    String  qryEff  = "GrouperList.by.group.and.member.and.list.and.is.eff"; 
    String  qryImm  = "GrouperList.by.group.and.member.and.list.and.is.imm"; 
    boolean rv      = false;
    if (gl.via() == null) { 
      try {
        q = s.dbSess().session().getNamedQuery(qryImm);
        qry = qryImm;
      } catch (HibernateException e) {
        throw new RuntimeException(
                    "Unable to get query " + qryImm + ": " + e
                  );
      }
    } else {
      try {
        q = s.dbSess().session().getNamedQuery(qryEff);
        qry = qryEff;
      } catch (HibernateException e) {
        throw new RuntimeException(
                    "Unable to get query " + qryEff + ": " + e
                  );
      }
    } 
    q.setString(0, gl.group().key());
    q.setString(1, gl.member().key());
    q.setString(2, gl.groupField());
    try {
      List vals = q.list();
      if (vals.size() == 1) {
        GrouperList lv = (GrouperList) vals.get(0);
        rv = true;
      }
    } catch (HibernateException e) {
      throw new RuntimeException(
                  "Error retrieving results for " + qry + ": " + e
                );
    }
    return rv;
  }

  /*
   * Properly load a list of GrouperList objects.
   */
  protected static List load(GrouperSession s, List vals) {
    List loaded = new ArrayList();
    Iterator iter = vals.iterator();
    while (iter.hasNext()) {
      GrouperList gl = (GrouperList) iter.next();
      gl.load(s);
      loaded.add(gl);
    }
    return loaded;
  }

  /*
   * Delete a {@link GrouperList} object from the groups registry.
   */
  protected static void delete(GrouperSession s, GrouperList gl) {
    // TODO Refactor into smaller components
    // TODO Why can't I just s.dbSess().session().delete(gl)?
    Query q;
    if (gl.via() != null) {
      try {
        // TODO Why can't I use delete() with a parameterized query?
        q = s.dbSess().session().createQuery(
              "FROM GrouperList AS gl WHERE " +
              "gl.groupKey    = ? AND "       +
              "gl.memberKey   = ? AND "       +
              "gl.groupField  = ? AND "       +
              "gl.viaKey      = ?"
            );
        q.setString(0, gl.group().key());
        q.setString(1, gl.member().key());
        q.setString(2, gl.groupField());
        q.setString(3, gl.via().key());
      } catch (HibernateException e) {
        throw new RuntimeException("Unable to create query: " + e);
      }
    } else {
      try {
        // TODO Why can't I use delete() with a parameterized query?
        q = s.dbSess().session().createQuery(
              "FROM GrouperList AS gl WHERE " +
              "gl.groupKey    = ? AND "       +
              "gl.memberKey   = ? AND "       +
              "gl.groupField  = ? AND "       +
              "gl.viaKey      IS NULL"
            );
        q.setString(0, gl.group().key());
        q.setString(1, gl.member().key());
        q.setString(2, gl.groupField());
      } catch (HibernateException e) {
        throw new RuntimeException("Unable to create query: " + e);
      }
    }

    try {
      List vals = q.list();
      if (vals.size() == 1) {
        GrouperList del = (GrouperList) vals.get(0);
        try {
          s.dbSess().session().delete(del);
        } catch (HibernateException e) {
          throw new RuntimeException(
                      "Error deleting list value: " + e
                    );
        }
      } else {
/* TODO Later, later...
        throw new RuntimeException(
                    "Wrong number of values to delete: " + vals.size()
                  );
*/
      }
    } catch (HibernateException e) {
      throw new RuntimeException(
                  "Error finding values: " + e
                );
    }

  }

  /*
   * Save a {@link GrouperList} object to the groups registry.
   */
  protected static void save(GrouperSession s, GrouperList gl) {
    GrouperList.validate(gl);
    if (gl.getListKey() == null) {
      gl.setListKey( new GrouperUUID().toString() );
    }
    try {
      s.dbSess().session().save(gl);
    } catch (HibernateException e) {
      throw new RuntimeException("Error saving list value: " + e);
    }
  }

  /*
   * Basic validation of {@link GrouperList} object.
   */
  protected static void validate(GrouperList gl) {
    if (gl == null) {
      throw new RuntimeException("list is null");
    }
    if (gl.group() == null) {
      throw new RuntimeException("list group is null");
    }
    if (gl.member() == null) {
      throw new RuntimeException("list member is null");
    }
    if (gl.groupField() == null) {
      throw new RuntimeException("list field is null");
    }
    if ( (gl.chain().size() > 0) && (gl.via() == null) ) {
      throw new RuntimeException(
        "list has via chain but no via group"
      );
    }
    if ( (gl.chain().size() == 0) && (gl.via() != null) ) {
      throw new RuntimeException(
        "list has via group but no via chain"
      );
    }
  }


  /*
   * PUBLIC INSTANCE METHODS
   */

  /**
   * Retrieve this list's via chain.
   * <p />
   * @return  List of {@link MemberVia} objects.
   */
  public List chain() {
    return this.elements;
  }

  /**
   * Compares the specified object with this list value for equality.
   * <p />
   * @param o Object to be compared for equality with this list value.
   * @return  True if the specified object is equal to this list value.
   */
  public boolean equals(Object o) {
     return EqualsBuilder.reflectionEquals(this, o);
   }

  /**
   * Returns the {@link Group} object referenced by this 
   * {@link GrouperList} object.
   * <p />
   * @return  A {@link Group} object.
   */
  public Group group() {
    return this.g;
  }

  /**
   * Return the group field for this list value.
   * <p />
   * @return  Field name.
   */
  public String groupField() {
    return this.getGroupField();
  }

  /**
   * Returns the hash code value for this list value.
   * <p />
   * @return  The hash code value for this list value.
   */
  public int hashCode() {
     return HashCodeBuilder.reflectionHashCode(this);
   }

  /**
   * Returns the {@link GrouperMember} object referenced by this 
   * {@link GrouperList} object.
   * <p />
   *
   * @return  A {@link GrouperMember} object.
   */
  public GrouperMember member() {
    return this.m;
  }

  /**
   * Return a string representation of this object.
   * <p />
   * @return String representation of this object.
   */
  public String toString() {
    String via    = ""; 
    if (this.via() != null) {
      via = this.via().name();
    }
    return new ToStringBuilder(this)          .
      append("group",   this.group().name() ) .
      append("field",   this.groupField()   ) . 
      append("member",  this.member()       ) .
      append("chain",   this.chain().size() ) .
      append("via",     via                 ) .
      toString();
  }

  /**
   * If this object represents an effective membership, returning the
   * {@link Group} object that caused the effective membership.
   * <p />
   * @return  A {@link Group} object.
   */
  public Group via() {
    return this.via;
  }


  /*
   * PROTECTED INSTANCE METHODS
   */


  /*
   * Set this object's chainKey.
   */
  protected void chainKey(String key) {
    this.setChainKey(key);
  }

  /*
   * @return This object's groupKey.
   */
  protected String groupKey() {
    return this.groupKey;
  }

  /*
   * Return - and possibly assign - a listKey.
   * @return This object's listKey.
   */
  protected String key() {
    if (this.getListKey() == null) {
      this.setListKey( new GrouperUUID().toString());
    }
    return this.getListKey();
  }

  /*
   * Properly load a GrouperList object.
   */
  protected void load(GrouperSession s) {
    GrouperSession.validate(s);
    if (this.g == null) {
      if (this.groupKey == null) {
        throw new RuntimeException("Unable to load group as key is null");
      }
      this.g = Group.loadByKey(s, this.groupKey);
    }
    if (this.m == null) {
      if (this.memberKey == null) {
        throw new RuntimeException("Unable to load member as key is null");
      }
      this.m = GrouperMember.loadByKey(s, this.memberKey);
    }
    if (this.via == null) {
      if (this.viaKey != null) {
        this.via = Group.loadByKey(s, this.viaKey);
      }
    }
    if (this.chainKey != null) {
      if (this.elements.size() == 0) {
        this.elements = MemberVia.load(s, this.chainKey);
      }
    }
    GrouperList.validate(this);
  }

  /*
   * @return This object's memberKey.
   */
  protected String memberKey() {
    return this.memberKey;
  }

  /*
   * @return This object's viaKey.
   */
  protected String viaKey() {
    return this.viaKey;
  }


  /*
   * HIBERNATE
   */

  private String getChainKey() {
    return this.chainKey;
  }

  private void setChainKey(String chainKey) {
    this.chainKey = chainKey;
  }

  private String getGroupKey() {
    return this.groupKey;
  }

  private void setGroupKey(String groupKey) {
    this.groupKey = groupKey;
  }

  private String getGroupField() {
    return this.groupField;
  }

  private void setGroupField(String groupField) {
    this.groupField = groupField;
  }

  private String getListKey() {
    return this.listKey;
  }

  private void setListKey(String listKey) {
    this.listKey = listKey;
  }

  private String getMemberKey() {
    return this.memberKey;
  }

  private void setMemberKey(String memberKey) {
    this.memberKey = memberKey;
  }

  private String getViaKey() {
    return this.viaKey;
  }

  private void setViaKey(String viaKey) {
    this.viaKey = viaKey;
  }

}

