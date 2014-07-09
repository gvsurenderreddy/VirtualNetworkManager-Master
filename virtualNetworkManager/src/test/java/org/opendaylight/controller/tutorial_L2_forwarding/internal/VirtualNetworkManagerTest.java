
/*
 * Copyright (c) 2013 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.controller.tutorial_L2_forwarding.internal;


import junit.framework.TestCase;

import org.junit.Assert;
import org.junit.Test;
import org.opendaylight.controller.virtualNetworkManager.VirtualNetworkManager;

public class VirtualNetworkManagerTest extends TestCase {

        @Test
        public void testTutorialL2ForwardingCreation() {

                VirtualNetworkManager ah = null;
                ah = new VirtualNetworkManager();
                Assert.assertTrue(ah != null);

        }

}
