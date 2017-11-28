/*
 * ! ******************************************************************************
 *
 *  Pentaho Data Integration
 *
 *  Copyright (C) 2002-2017 by Hitachi Vantara : http://www.pentaho.com
 *
 * ******************************************************************************
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with
 *  the License. You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 * *****************************************************************************
 */
package org.pentaho.di.trans.ael.websocket;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.pentaho.di.core.KettleClientEnvironment;
import org.pentaho.di.core.Props;
import org.pentaho.di.core.logging.KettleLogStore;
import org.pentaho.di.core.logging.LogChannelInterface;
import org.pentaho.di.core.logging.LogChannelInterfaceFactory;
import org.pentaho.di.core.plugins.PluginRegistry;
import org.pentaho.di.core.plugins.StepPluginType;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.StepMetaDataCombi;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;

@RunWith( MockitoJUnitRunner.class )
public class TransWebSocketEngineAdapterTest {
  @Mock private LogChannelInterfaceFactory logChannelFactory;
  @Mock private LogChannelInterface logChannel;

  @BeforeClass
  public static void init() throws Exception {
    KettleClientEnvironment.init();
    PluginRegistry.addPluginType( StepPluginType.getInstance() );
    PluginRegistry.init();
    if ( !Props.isInitialized() ) {
      Props.init( 0 );
    }
  }

  @Before
  public void setUp() throws Exception {
    KettleLogStore.setLogChannelInterfaceFactory( logChannelFactory );
    Mockito.when( logChannelFactory.create( any(), any() ) ).thenReturn( logChannel );
  }

  @Test
  public void testOpsIncludeSubTrans() throws Exception {
    TransMeta transMeta = new TransMeta( getClass().getResource( "grid-to-subtrans.ktr" ).getPath() );
    TransWebSocketEngineAdapter adapter =
      new TransWebSocketEngineAdapter( transMeta, "", "", false );
    adapter.prepareExecution( new String[]{} );
    List<StepMetaDataCombi> steps = adapter.getSteps();
    assertEquals( 4, steps.size() );
  }
}
