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

package edu.internet2.middleware.grouper;
import  junit.framework.*;
import  org.apache.commons.logging.*;

/**
 * @author  blair christensen.
 * @version $Id: TestGroup34.java,v 1.6 2008-06-24 06:07:03 mchyzer Exp $
 */
public class TestGroup34 extends TestCase {

  private static final Log LOG = LogFactory.getLog(TestGroup34.class);

  public TestGroup34(String name) {
    super(name);
  }

  protected void setUp () {
    LOG.debug("setUp");
    RegistryReset.reset();
  }

  protected void tearDown () {
    LOG.debug("tearDown");
  }

  public void testGetTypesAndRemovableTypesWithCustomTypeAsNonRootSubject() {
    LOG.info("testGetTypesAndRemovableTypesWithCustomTypeAsNonRootSubject");
    try {
      R         r       = R.populateRegistry(1, 1, 1);
      GroupType custom  = GroupType.createType(r.rs, "custom");
      Group     gA      = r.getGroup("a", "a");
      gA.addType(custom);
      GrouperSession.start( r.getSubject("a") );
      T.amount("types", 2, gA.getTypes().size());
      T.amount("removable types", 0, gA.getRemovableTypes().size());
      r.rs.stop();
    }
    catch (Exception e) {
      T.e(e);
    }
  } // public void testGetTypesAndRemovableTypesWithCustomTypeAsNonRootSubject()

}

