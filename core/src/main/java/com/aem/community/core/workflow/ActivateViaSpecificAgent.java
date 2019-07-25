package com.aem.community.core.workflow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.jcr.Session;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.day.cq.replication.Agent;
import com.day.cq.replication.AgentIdFilter;
import com.day.cq.replication.AgentManager;
import com.day.cq.replication.ReplicationActionType;
import com.day.cq.replication.ReplicationException;
import com.day.cq.replication.ReplicationOptions;
import com.day.cq.replication.Replicator;
import org.osgi.framework.Constants;

@Component(immediate = true, service = WorkflowProcess.class, property = {
		Constants.SERVICE_DESCRIPTION + "=Activate AEM Workflow payload via specific replication agent, which is provided as argument in wokflow step",
		Constants.SERVICE_VENDOR + "=Anupam Maiti", "process.label=Activate Via Specific Agent" })
public class ActivateViaSpecificAgent implements WorkflowProcess {

	@Reference
	AgentManager lAgentManager;
	@Reference
	Replicator lReplicator;

	private static final Logger log = LoggerFactory.getLogger(ActivateViaSpecificAgent.class);

	@Override
	public void execute(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap processArguments)
			throws WorkflowException {
		log.debug("The string I got was ..." + processArguments.get("PROCESS_ARGS", "string").toString());
		String[] lProcessArgs = processArguments.get("PROCESS_ARGS", "string").toString().split(",");
		log.debug("Workflow Step arguments :" + Arrays.toString(lProcessArgs));
		String lPayloadPath = workItem.getWorkflowData().getPayload().toString();
		log.debug("Workflow payload path :" + lPayloadPath);
		log.debug("List of Workflow agents:" + Arrays.toString(getReplicationAgents()));

		if (Arrays.asList(getReplicationAgents()).contains(lProcessArgs[0])) {
			ReplicationOptions lReplicationOptions = new ReplicationOptions();
			lReplicationOptions.setFilter(new AgentIdFilter(lProcessArgs[0]));
			try {
				lReplicator.replicate(workflowSession.adaptTo(Session.class), ReplicationActionType.ACTIVATE,
						lPayloadPath, lReplicationOptions);
			} catch (ReplicationException e) {
				log.error("ReplicationException while activating payload, Reason :" + e);
			}
		}

	}

	public final String[] getReplicationAgents() {
		final List<String> lReplicationAgentsName = new ArrayList<String>();
		for (final Agent agent : lAgentManager.getAgents().values()) {
			lReplicationAgentsName.add(agent.getId());
		}
		return lReplicationAgentsName.toArray(new String[lReplicationAgentsName.size()]);
	}

}