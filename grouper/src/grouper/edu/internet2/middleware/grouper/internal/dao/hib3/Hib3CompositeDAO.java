/*
  Copyright (C) 2004-2007 University Corporation for Advanced Internet Development, Inc.
  Copyright (C) 2004-2007 The University Of Chicago

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
*/

package edu.internet2.middleware.grouper.internal.dao.hib3;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;

import edu.internet2.middleware.grouper.Composite;
import edu.internet2.middleware.grouper.Group;
import edu.internet2.middleware.grouper.Member;
import edu.internet2.middleware.grouper.exception.CompositeNotFoundException;
import edu.internet2.middleware.grouper.hibernate.AuditControl;
import edu.internet2.middleware.grouper.hibernate.ByObject;
import edu.internet2.middleware.grouper.hibernate.GrouperTransactionType;
import edu.internet2.middleware.grouper.hibernate.HibernateHandler;
import edu.internet2.middleware.grouper.hibernate.HibernateHandlerBean;
import edu.internet2.middleware.grouper.hibernate.HibernateMisc;
import edu.internet2.middleware.grouper.hibernate.HibernateSession;
import edu.internet2.middleware.grouper.internal.dao.CompositeDAO;
import edu.internet2.middleware.grouper.internal.dao.GrouperDAOException;

/**
 * Basic Hibernate <code>Composite</code> DAO interface.
 * @author  blair christensen.
 * @version $Id: Hib3CompositeDAO.java,v 1.14 2009-08-18 23:11:38 shilen Exp $
 */
public class Hib3CompositeDAO extends Hib3DAO implements CompositeDAO {

  // PRIVATE CLASS CONSTANTS //
  private static final String KLASS = Hib3CompositeDAO.class.getName();


  /**
   * @see edu.internet2.middleware.grouper.internal.dao.CompositeDAO#findAsFactorOrHasMemberOfFactor(java.lang.String)
   */
  public Set<Composite> findAsFactorOrHasMemberOfFactor(String groupId) {
    return HibernateSession.byHqlStatic().createQuery(
        "select c from Composite as c, GroupSet as gs, Field as f where gs.memberGroupId = :group and ("
            + " c.leftFactorUuid = gs.ownerGroupId or c.rightFactorUuid = gs.ownerGroupId )" 
            + " and gs.fieldId = f.uuid and gs.fieldId = :field")
        .setCacheable(false)
        .setCacheRegion(KLASS + ".findAsFactorOrHasMemberOfFactor")
        .setString("group", groupId)
        .setString("field", Group.getDefaultList().getUuid())
        .listSet(Composite.class);
  }

  /**
   * @since   @HEAD@
   */
  public Set<Composite> findAsFactor(Group _g) 
    throws  GrouperDAOException
  {
    return HibernateSession.byHqlStatic()
      .createQuery("from Composite as c where (" 
          + " c.leftFactorUuid = :left or c.rightFactorUuid = :right "
          + ")")
          .setCacheable(false)
          .setCacheRegion(KLASS + ".FindAsFactor")
          .setString( "left",  _g.getUuid() )
          .setString( "right", _g.getUuid() )
          .listSet(Composite.class);
  } // public Set findAsFactor(_g)
  
  public Set<Composite> findAsFactor(String groupId) throws GrouperDAOException {
    return HibernateSession.byHqlStatic().createQuery(
        "from Composite as c where ("
            + " c.leftFactorUuid = :left or c.rightFactorUuid = :right " + ")")
        .setCacheable(false).setCacheRegion(KLASS + ".FindAsFactor")
        .setString("left", groupId)
        .setString("right", groupId)
        .listSet(Composite.class);
  }

  /**
   */
  public Composite findAsOwner(Group _g, boolean throwExceptionIfNotFound) 
    throws  CompositeNotFoundException,
            GrouperDAOException {
    Composite dto = HibernateSession.byHqlStatic()
      .createQuery("from Composite as c where c.factorOwnerUuid = :uuid")
      .setCacheable(false)
      .setCacheRegion(KLASS + ".FindAsOwner")
      .setString( "uuid", _g.getUuid() ).uniqueResult(Composite.class);
    if (dto == null) {
      if (throwExceptionIfNotFound) { 
        throw new CompositeNotFoundException();
      }
      return null;
    }
    return dto;
  } // public Composite findAsOwner(_g)

  /**
   * @param uuid 
   * @return the composite
   * @throws CompositeNotFoundException 
   * @throws GrouperDAOException 
   * @since   @HEAD@
   */
  public Composite findByUuid(String uuid, boolean throwExceptionIfNotFound) 
    throws  CompositeNotFoundException,
            GrouperDAOException {
    Composite dto = HibernateSession.byHqlStatic()
    .createQuery("from Composite as c where c.uuid = :uuid")
    .setCacheable(false)
    .setCacheRegion(KLASS + ".FindByUuid")
    .setString( "uuid", uuid ).uniqueResult(Composite.class);

    if (dto == null) {
      if (throwExceptionIfNotFound) { 
        throw new CompositeNotFoundException();
      }
      return null;
    }
    return dto;
  } // public Composite findByUuid(uuid)


  /**
   * @since   @HEAD@
   */
  public Set<Composite> getAllComposites()
    throws  GrouperDAOException
  {
     Set<Composite> composites = HibernateSession.byHqlStatic()
      .createQuery("from Composite as c")
          .setCacheable(false)
          .setCacheRegion(KLASS + ".GetAllComposites")
          .listSet(Composite.class);
    return composites;
  }

  /**
   * @since   @HEAD@
   */
  public void update(final Set toAdd, final Set toDelete, final Set modGroups, final Set modStems) 
    throws  GrouperDAOException {
    
    HibernateSession.callbackHibernateSession(
        GrouperTransactionType.READ_WRITE_OR_USE_EXISTING, AuditControl.WILL_NOT_AUDIT,
        new HibernateHandler() {

          public Object callback(HibernateHandlerBean hibernateHandlerBean)
              throws GrouperDAOException {
            HibernateSession hibernateSession = hibernateHandlerBean.getHibernateSession();
            
            ByObject byObject = hibernateSession.byObject();
            HibernateMisc misc = hibernateSession.misc();

            byObject.delete(toDelete);
            misc.flush();
            
            byObject.save(toAdd);
            misc.flush();

            byObject.update(modGroups);
            misc.flush();

            byObject.update(modStems);
            return null;
          }
      
    });
  } // public void update(toAdd, toDelete, modGroups, modStems)

  
  /**
   * @see edu.internet2.middleware.grouper.internal.dao.CompositeDAO#save(edu.internet2.middleware.grouper.Composite)
   */
  public void save(Composite c) {
    HibernateSession.byObjectStatic().save(c);
  }

  /**
   * @see edu.internet2.middleware.grouper.internal.dao.CompositeDAO#delete(edu.internet2.middleware.grouper.Composite)
   */
  public void delete(Composite c) {
    HibernateSession.byObjectStatic().delete(c);
  }
  
  // PROTECTED CLASS METHODS //

  // @since   @HEAD@
  protected static void reset(HibernateSession hibernateSession) 
    throws  HibernateException
  {
    hibernateSession.byHql().createQuery("delete from Composite").executeUpdate();
  } 

  /**
   * find all composites by creator
   * @param member
   * @return the composites
   */
  public Set<Composite> findByCreator(Member member) {
    if (member == null || StringUtils.isBlank(member.getUuid())) {
      throw new RuntimeException("Need to pass in a member");
    }
    Set<Composite> composites = HibernateSession.byHqlStatic()
      .createQuery("from Composite as c where c.creatorUuid = :uuid")
      .setCacheable(false)
      .setCacheRegion(KLASS + ".FindByCreator")
      .setString( "uuid", member.getUuid() ).listSet(Composite.class);
    return composites;
  }

} 

