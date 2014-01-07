/**
 * The Accord Project, http://accordproject.org
 * Copyright (C) 2005-2013 Rafael Marins, http://rafaelmarins.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.neociclo.odetteftp.camel;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.impl.ScheduledPollConsumer;

/**
 * @author Rafael Marins
 */
public class OftpClientConsumer extends ScheduledPollConsumer implements IOftpConsumer {

	private OftpOperations operations;

	public OftpClientConsumer(OftpClientEndpoint endpoint, Processor processor) {
		super(endpoint, processor);
		this.operations = endpoint.getOperations();

//		setPollStrategy(new DefaultOdettePollingStrategy());
		setUseFixedDelay(true);
	}

	@Override
	protected int poll() throws Exception {

		OftpClientEndpoint endpoint = (OftpClientEndpoint) getEndpoint();
		endpoint.setHasIn();

		try {
			operations.runClient();
		} catch (Exception e) {
			getExceptionHandler().handleException(e);
		}
		
		// TODO: not really correct, should return the number of messages polled
		//  but I am not sure the current design allows that; let's get it to compile first
		return 0;
	}

	public void process(Exchange exchange) {
		try {
			getProcessor().process(exchange);
		} catch (Exception e) {
			getExceptionHandler().handleException(e);
		}
	}

}
