/*
 * $Header: /home/hagleyj/i2mi/grouper-misc/grouperClient/src/ext/edu/internet2/middleware/grouperClientExt/org/apache/commons/httpclient/Header.java,v 1.1 2008-11-30 10:57:19 mchyzer Exp $
 * $Revision: 1.1 $
 * $Date: 2008-11-30 10:57:19 $
 *
 * ====================================================================
 *
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package edu.internet2.middleware.grouperInstallerExt.org.apache.commons.httpclient;

/**
 * <p>An HTTP header.</p>
 *
 * @author <a href="mailto:remm@apache.org">Remy Maucherat</a>
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author <a href="mailto:oleg@ural.ru">Oleg Kalnichevski</a>
 * @version $Revision: 1.1 $ $Date: 2008-11-30 10:57:19 $
 */
public class Header extends NameValuePair {

    // ----------------------------------------------------------- Constructors

    /**
     * Autogenerated header flag.
     */
    private boolean isAutogenerated = false;
    
    /**
     * Default constructor.
     */
    public Header() {
        this(null, null);
    }

    /**
     * Constructor with name and value
     *
     * @param name the header name
     * @param value the header value
     */
    public Header(String name, String value) {
        super(name, value);
    }

    /**
     * Constructor with name and value
     *
     * @param name the header name
     * @param value the header value
     * @param isAutogenerated <tt>true</tt> if the header is autogenerated,
     *  <tt>false</tt> otherwise.
     * 
     * @since 3.0
     */
    public Header(String name, String value, boolean isAutogenerated) {
        super(name, value);
        this.isAutogenerated = isAutogenerated;
    }

    // --------------------------------------------------------- Public Methods

    /**
     * Returns a {@link String} representation of the header.
     *
     * @return stringHEAD
     */
    public String toExternalForm() {
        return ((null == getName() ? "" : getName()) 
            + ": " 
            + (null == getValue() ? "" : getValue()) 
            + "\r\n");
    }

    /**
     * Returns a {@link String} representation of the header.
     *
     * @return stringHEAD
     */
    public String toString() {
        return toExternalForm();
    }

    /**
     * Returns an array of {@link HeaderElement}s
     * constructed from my value.
     *
     * @see HeaderElement#parse
     * @throws HttpException if the header cannot be parsed
     * @return an array of header elements
     * 
     * @deprecated Use #getElements
     */
    public HeaderElement[] getValues() throws HttpException {
        return HeaderElement.parse(getValue());
    }

    /**
     * Returns an array of {@link HeaderElement}s
     * constructed from my value.
     *
     * @see HeaderElement#parseElements(String)
     * 
     * @return an array of header elements
     * 
     * @since 3.0
     */
    public HeaderElement[] getElements() {
        return HeaderElement.parseElements(getValue());
    }

    /**
     * Returns the value of the auto-generated header flag.
     * 
     * @return <tt>true</tt> if the header is autogenerated,
     *  <tt>false</tt> otherwise.
     * 
     * @since 3.0
     */
    public boolean isAutogenerated() {
        return isAutogenerated;
    }

}
