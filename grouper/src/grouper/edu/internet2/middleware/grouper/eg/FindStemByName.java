/*
  Copyright 2006 University Corporation for Advanced Internet Development, Inc.
  Copyright 2006 The University Of Chicago

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

package edu.internet2.middleware.grouper.eg;
import  edu.internet2.middleware.grouper.*;
import  edu.internet2.middleware.subject.*;

/**
 * Example: Find a {@link Stem} by name within the Groups Registry.
 * @author  blair christensen.
 * @version $Id: FindStemByName.java,v 1.1 2006-08-04 19:02:11 blair Exp $
 * @since   1.0.1
 */
public class FindStemByName {

  // MAIN //
  public static void main(String args[]) {
    try {
      GrouperSession s = GrouperSession.start(
        SubjectFinder.findById(
          "GrouperSystem", "application", InternalSourceAdapter.ID
        )
      );
    
      try {
        String  name  = "etc";
        Stem    ns    = StemFinder.findByName(s, name);
        EgLog.info(FindStemByName.class, "Found Stem by name: " + name);
      }
      catch (StemNotFoundException eNSNF) {
        EgLog.error(FindStemByName.class, "Failed to find Stem by name: " + eNSNF.getMessage());
        System.exit(1);
      }

      s.stop();
    }
    catch (Exception e) {
      EgLog.error(FindStemByName.class, "UNEXPECTED ERROR: " + e.getMessage());
      System.exit(1);
    }
    System.exit(0);
  } // public static void main(args[])

} // public class FindStemByName

