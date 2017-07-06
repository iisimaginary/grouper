/**
 * Copyright 2014 Internet2
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
 */
/*
 * Copyright (C) 2006-2007 blair christensen.
 * All Rights Reserved.
 *
 * You may use and distribute under the same terms as Grouper itself.
 */

package edu.internet2.middleware.grouper.app.gsh;
import edu.internet2.middleware.grouper.GrouperSession;
import edu.internet2.middleware.grouper.misc.GrouperInfo;
import bsh.CallStack;
import bsh.Interpreter;

/**
 * Get version information.
 * <p/>
 * @author  blair christensen.
 * @version $Id: version.java,v 1.3 2008-11-08 03:42:33 mchyzer Exp $
 * @since   0.0.1
 */
public class version {

  // PUBLIC CLASS METHODS //

  /**
   * Get version information.
   * <p/>
   * @param   i           BeanShell interpreter.
   * @param   stack       BeanShell call stack.
   * @return  {@link GrouperShell} version.
   * @since   0.0.1
   */
  public static String invoke(Interpreter i, CallStack stack) {
    GrouperShell.setOurCommand(i, true);
    return invoke(null);
  }

  /**
   * Get version information.
   * <p/>
   * @param   grouperSession
   * @return  {@link GrouperShell} version.
   */
  public static String invoke(GrouperSession grouperSession) {
    GrouperInfo.grouperInfo(System.out, true);
    return "";
  }
} // public class version

